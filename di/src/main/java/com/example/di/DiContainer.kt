package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField

object DiContainer {
    private data class Key(
        val type: KClass<*>,
        val qualifier: KClass<out Annotation>?,
    )

    private data class Binding(
        val implementationOrType: KClass<*>,
        val componentConstructor: (Any?) -> Component,
        val isScoped: Boolean,
        val factoryFunction: ((Any?) -> Any)? = null,
        val isInterfaceBinding: Boolean = false,
    )

    private val bindingMap = mutableMapOf<Key, Binding>()
    private val singletonCache = mutableMapOf<Key, Lazy<Any>>()
    private val activityCacheMap = mutableMapOf<Any, MutableMap<Key, Lazy<Any>>>()
    private val viewModelCacheMap = mutableMapOf<Any, MutableMap<Key, Lazy<Any>>>()

    fun <InterfaceType : Any> bindBinds(
        fromInterface: KClass<InterfaceType>,
        toImplementation: KClass<out InterfaceType>,
        installIn: (Any?) -> Component,
        isScoped: Boolean = true,
        qualifier: KClass<out Annotation>? = null,
    ) {
        require(
            fromInterface != toImplementation &&
                fromInterface.java.isAssignableFrom(
                    toImplementation.java,
                ),
        )
        bindingMap[Key(fromInterface, qualifier)] =
            Binding(
                implementationOrType = toImplementation,
                componentConstructor = installIn,
                isScoped = isScoped,
                factoryFunction = null,
                isInterfaceBinding = true,
            )
    }

    fun <Type : Any> bindProvides(
        type: KClass<Type>,
        installIn: (Any?) -> Component,
        isScoped: Boolean = true,
        qualifier: KClass<out Annotation>? = null,
        factoryFunction: (Any?) -> Type,
    ) {
        bindingMap[Key(type, qualifier)] =
            Binding(
                implementationOrType = type,
                componentConstructor = installIn,
                isScoped = isScoped,
                factoryFunction = factoryFunction,
                isInterfaceBinding = false,
            )
    }

    fun openActivityComponent(activityOwner: Any) {
        activityCacheMap.getOrPut(activityOwner) { mutableMapOf() }
    }

    fun closeActivityComponent(activityOwner: Any) {
        activityCacheMap.remove(activityOwner)
    }

    fun openViewModelComponent(viewModelOwner: Any) {
        viewModelCacheMap.getOrPut(viewModelOwner) { mutableMapOf() }
    }

    fun closeViewModelComponent(viewModelOwner: Any) {
        viewModelCacheMap.remove(viewModelOwner)
    }

    fun <Type : Any> get(
        requestedType: KClass<Type>,
        ownerComponent: Component,
        qualifier: KClass<out Annotation>? = null,
    ): Type {
        val key = Key(requestedType, qualifier)
        val binding = bindingMap[key] ?: return createUnbound(requestedType, ownerComponent) as Type

        val targetComponent = binding.componentConstructor(getOwnerFrom(ownerComponent))
        require(targetComponent::class == ownerComponent::class || targetComponent is Component.Singleton)

        val implementationClass = binding.implementationOrType
        val isScoped = binding.isScoped

        val currentCache =
            when (ownerComponent) {
                is Component.Singleton -> singletonCache
                is Component.Activity -> activityCacheMap[ownerComponent.owner]
                is Component.ViewModel -> viewModelCacheMap[ownerComponent.owner]
            }

        if (!isScoped) {
            return createNewInstance(implementationClass, ownerComponent) as Type
        }

        val lazyInstance =
            when (ownerComponent) {
                is Component.Singleton ->
                    currentCache!!.getOrPut(Key(implementationClass, qualifier)) {
                        lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                            provideOrCreate(binding, ownerComponent)
                        }
                    }

                is Component.Activity -> {
                    val activityScopedCache = currentCache ?: throw IllegalStateException()
                    activityScopedCache.getOrPut(Key(implementationClass, qualifier)) {
                        lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                            provideOrCreate(binding, ownerComponent)
                        }
                    }
                }

                is Component.ViewModel -> {
                    val viewModelScopedCache = currentCache ?: throw IllegalStateException()
                    viewModelScopedCache.getOrPut(Key(implementationClass, qualifier)) {
                        lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
                            provideOrCreate(binding, ownerComponent)
                        }
                    }
                }
            }
        return lazyInstance.value as Type
    }

    private fun getOwnerFrom(component: Component): Any? =
        when (component) {
            is Component.Singleton -> null
            is Component.Activity -> component.owner
            is Component.ViewModel -> component.owner
        }

    private fun <Type : Any> createUnbound(
        type: KClass<Type>,
        ownerComponent: Component,
    ): Any {
        val primaryConstructor =
            type.primaryConstructor
                ?: throw IllegalStateException()

        if (!primaryConstructor.annotations.any { it is Inject }) {
            throw IllegalStateException()
        }

        return callWithInjectedArguments(type, ownerComponent)
    }

    private fun provideOrCreate(
        binding: Binding,
        ownerComponent: Component,
    ): Any {
        val owner = getOwnerFrom(ownerComponent)
        return binding.factoryFunction?.invoke(owner)
            ?: createNewInstance(binding.implementationOrType, ownerComponent)
    }

    private fun createNewInstance(
        implementationClass: KClass<*>,
        ownerComponent: Component,
    ): Any = callWithInjectedArguments(implementationClass, ownerComponent)

    private fun callWithInjectedArguments(
        targetClass: KClass<*>,
        ownerComponent: Component,
    ): Any {
        val injectableConstructor = findInjectableConstructor(targetClass)
        val argumentMap =
            injectableConstructor.parameters
                .filter { it.kind == KParameter.Kind.VALUE }
                .associateWith { parameter ->
                    val dependencyClass =
                        parameter.type.classifier as? KClass<*>
                            ?: throw IllegalStateException()
                    val qualifierAnnotation = findQualifier(parameter.annotations)
                    get(dependencyClass, ownerComponent, qualifierAnnotation)
                }

        val instance = injectableConstructor.callBy(argumentMap)

        for (property in targetClass.declaredMemberProperties) {
            val javaField = property.javaField ?: continue
            val hasInjectAnnotation = javaField.isAnnotationPresent(Inject::class.java)
            if (!hasInjectAnnotation) continue

            javaField.isAccessible = true
            val dependencyClass = javaField.type.kotlin
            val qualifierAnnotation = findQualifier(javaField.annotations.toList())
            val dependencyInstance = get(dependencyClass, ownerComponent, qualifierAnnotation)
            javaField.set(instance, dependencyInstance)
        }

        return instance!!
    }

    private fun findInjectableConstructor(targetClass: KClass<*>): KFunction<*> {
        val constructorWithInjectAnnotation =
            targetClass.constructors.firstOrNull { constructor ->
                constructor.annotations.any { it is Inject }
            }
        return constructorWithInjectAnnotation
            ?: targetClass.primaryConstructor
            ?: throw IllegalStateException()
    }

    private fun findQualifier(annotations: List<Annotation>): KClass<out Annotation>? {
        return annotations.firstOrNull { annotation ->
            annotation.annotationClass.annotations.any { metaAnnotation ->
                metaAnnotation is Qualifier
            }
        }?.annotationClass
    }
}
