package com.medandro.di

import com.medandro.di.annotation.InjectField
import com.medandro.di.annotation.LifecycleScope
import com.medandro.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    vararg registerClasses: KClass<*>,
) {
    private val applicationInstances = mutableMapOf<DependencyKey, Any>()
    private val activityInstances = mutableMapOf<DependencyKey, Any>()
    private val viewModelInstances = mutableMapOf<DependencyKey, Any>()
    private val interfaceMapping = mutableMapOf<DependencyKey, KClass<*>>()

    init {
        generateInterfaceMapping(registerClasses)
    }

    fun registerSingleton(
        instance: Any,
        qualifier: String? = null,
    ): DIContainer {
        // 구현체 자신의 타입으로 등록
        applicationInstances[DependencyKey(instance::class, qualifier)] = instance

        // 상위 타입들(인터페이스 포함)도 같이 등록
        instance::class.supertypes.forEach { superType ->
            val superClass = superType.classifier as? KClass<*>
            if (superClass != null && superClass != Any::class) {
                applicationInstances[DependencyKey(superClass, qualifier)] = instance
            }
        }
        return this
    }

    fun injectFields(target: Any) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.hasAnnotation<InjectField>() }
            .forEach { property ->
                val annotation = property.findAnnotation<InjectField>()
                val scope = annotation?.scope ?: LifecycleScope.APPLICATION
                injectFieldWithScope(target, property, scope)
            }
    }

    fun getInstance(
        dependencyKey: DependencyKey,
        scope: LifecycleScope = LifecycleScope.APPLICATION,
    ): Any {
        // 인터페이스일 경우 매핑된 클래스로 반환
        interfaceMapping[dependencyKey]?.let { implClass ->
            val implKey = DependencyKey(implClass, dependencyKey.qualifier)
            return getInstance(implKey, scope)
        }

        // DIContainer 내부에서 생성&관리되는 인스턴스에서 반환
        val instances = getInstanceStorage(scope)
        instances[dependencyKey]?.let { return it }

        // 인스턴스가 존재하지 않을 경우 생성
        val createdInstance = createNewInstance(dependencyKey.type, scope)
        instances[dependencyKey] = createdInstance
        return createdInstance
    }

    private fun injectFieldWithScope(
        target: Any,
        property: KMutableProperty1<Any, Any?>,
        scope: LifecycleScope,
    ) {
        try {
            val fieldType = property.returnType.classifier as KClass<*>
            val qualifier = getFieldQualifier(property)
            val dependencyKey = DependencyKey(fieldType, qualifier)

            val instance = getInstance(dependencyKey, scope)

            property.isAccessible = true
            property.set(target, instance)
        } catch (e: Exception) {
            throw IllegalStateException(
                "${target::class.simpleName}의 '${getFieldQualifier(property)} Qualifier의 ${property.name}, 필드 주입 실패 : ${e.message}",
                e,
            )
        }
    }

    private fun getInstanceStorage(scope: LifecycleScope) =
        when (scope) {
            LifecycleScope.APPLICATION -> applicationInstances
            LifecycleScope.ACTIVITY -> activityInstances
            LifecycleScope.VIEWMODEL -> viewModelInstances
        }

    private fun generateInterfaceMapping(registerClasses: Array<out KClass<*>>) {
        registerClasses.forEach { implClass ->
            val qualifier = getQualifier(implClass)
            val interfaces =
                implClass.supertypes
                    .mapNotNull { it.classifier as? KClass<*> }
                    .filter { it.java.isInterface }

            interfaces.forEach { interfaceClass ->
                val key = DependencyKey(interfaceClass, qualifier)
                interfaceMapping[key] = implClass
            }
        }
    }

    private fun createNewInstance(
        kClass: KClass<*>,
        scope: LifecycleScope,
    ): Any {
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalStateException(
                    "${kClass.simpleName} 는 주 생성자가 없어 자동으로 인스턴스를 생성할 수 없습니다." +
                        "DIContainer가 지원하는 타입: 주 생성자가 명시되어 있거나 생략된 클래스",
                )

        val parameterMap =
            constructor.parameters
                .filterNot { it.isOptional }
                .associateWith { param ->
                    val paramType = param.type.classifier as KClass<*>
                    getInstance(DependencyKey(paramType), scope)
                }
        return constructor.callBy(parameterMap)
    }

    private fun getFieldQualifier(property: KMutableProperty1<Any, Any?>): String? = property.findAnnotation<Qualifier>()?.value

    private fun getQualifier(kClass: KClass<*>): String? = kClass.findAnnotation<Qualifier>()?.value
}
