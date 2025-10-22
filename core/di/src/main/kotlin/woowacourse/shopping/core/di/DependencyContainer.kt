package woowacourse.shopping.core.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class DependencyContainer {
    private val dependencies: MutableMap<Pair<KClass<*>, KClass<out Annotation>?>, Any> =
        mutableMapOf()

    fun <T : Any> register(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        provider: () -> T,
    ) {
        checkHasQualifierAnnotation(qualifier)
        checkNotDuplicatedQualifier(clazz, qualifier)

        dependencies[clazz to qualifier] = provider()
    }

    fun <T : Any> instance(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        @Suppress("UNCHECKED_CAST")
        registeredInstance(clazz, qualifier)?.let { value: T -> return value }

        val constructor: KFunction<T> = clazz.constructorForInject
        val arguments: Array<Any> = constructor.arguments
        val instance = constructor.call(*arguments)
        instance.injectFields()

        dependencies[clazz to qualifier] = instance
        return instance
    }

    private fun <T : Any> checkNotDuplicatedQualifier(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ) {
        val alreadyExists =
            dependencies.keys.any { (registeredClass, registeredQualifier) ->
                registeredClass == clazz && registeredQualifier == qualifier
            }

        if (alreadyExists) {
            error(
                "Dependency already registered for ${clazz.simpleName} " +
                    "(qualifier=${qualifier?.simpleName}). Duplicate registration is not allowed.",
            )
        }
    }

    private fun checkHasQualifierAnnotation(qualifier: KClass<out Annotation>?) {
        if (qualifier != null && !qualifier.hasAnnotation<Qualifier>()) {
            error(
                "Annotation ${qualifier.simpleName} is not a valid Qualifier. " +
                    "All qualifiers must be annotated with @Qualifier.",
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> registeredInstance(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>?,
    ): T? =
        dependencies.entries
            .firstOrNull { (key: Pair<KClass<*>, KClass<out Annotation>?>, _: Any) ->
                key.first == clazz && key.second == qualifier
            }?.value as? T

    private val <T : Any> KClass<T>.constructorForInject: KFunction<T>
        get() =
            constructors.find { it.hasAnnotation<Inject>() }
                ?: primaryConstructor
                ?: error("No injectable constructor found for $simpleName")

    private val <T : Any> KFunction<T>.arguments: Array<Any>
        get() =
            parameters
                .map { parameter: KParameter ->
                    val clazz: KClass<*> = parameter.type.kClass
                    val qualifier: KClass<out Annotation>? = parameter.qualifier
                    this@DependencyContainer.instance(clazz, qualifier)
                }.toTypedArray()

    private fun <T : Any> T.injectFields() {
        this::class
            .memberProperties
            .filter { it.hasAnnotation<Inject>() }
            .forEach { property: KProperty1<out T, Any?> ->
                property as? KMutableProperty1<T, Any?>
                    ?: error("@Inject property '${property.name}' must be declared as 'var'.")

                val propertyClass: KClass<*> = property.returnType.kClass
                val qualifier: KClass<out Annotation>? = property.qualifier
                val value = instance(propertyClass, qualifier)

                property.set(this, value)
            }
    }

    private val KType.kClass: KClass<*>
        get() =
            when (val classifier = classifier) {
                is KClass<*> -> classifier
                is KTypeParameter ->
                    error("Generic type parameter ${classifier.name} cannot be resolved at runtime.")

                else -> error("Unsupported classifier type: $classifier")
            }

    private val KAnnotatedElement.qualifier: KClass<out Annotation>?
        get() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }?.annotationClass
}
