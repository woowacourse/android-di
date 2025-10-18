package woowacourse.shopping.core.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
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
    private val dependencies: MutableMap<Pair<KClass<*>, String?>, Any> = mutableMapOf()

    fun <T : Any> register(
        clazz: KClass<T>,
        provider: () -> T,
    ) {
        dependencies[clazz to null] = provider()
    }

    fun <T : Any> register(
        clazz: KClass<T>,
        qualifier: String,
        provider: () -> T,
    ) {
        dependencies[clazz to qualifier] = provider()
    }

    fun <T : Any> instance(
        clazz: KClass<T>,
        qualifier: String? = null,
    ): T {
        @Suppress("UNCHECKED_CAST")
        (dependencies[clazz to qualifier] as? T)?.let { return it }

        val constructor: KFunction<T> = clazz.constructorForInject
        val arguments: Array<Any> = constructor.arguments
        val instance = constructor.call(*arguments)
        instance.injectFields()

        dependencies[clazz to qualifier] = instance
        return instance
    }

    private val <T : Any> KClass<T>.constructorForInject
        get() =
            constructors.find { it.hasAnnotation<Inject>() }
                ?: primaryConstructor
                ?: error("No injectable constructor found for $simpleName")

    private val <T : Any> KFunction<T>.arguments: Array<Any>
        get() =
            parameters
                .map { parameter: KParameter ->
                    val parameterClass: KClass<*> = parameter.type.kClass
                    this@DependencyContainer.instance(parameterClass, parameter.qualifier)
                }.toTypedArray()

    private fun <T : Any> T.injectFields() {
        this::class
            .memberProperties
            .filter { it.hasAnnotation<Inject>() }
            .forEach { property: KProperty1<out T, *> ->
                @Suppress("UNCHECKED_CAST")
                val mutableProperty: KMutableProperty1<Any, Any?> =
                    property as? KMutableProperty1<Any, Any?>
                        ?: error("@Inject property '${property.name}' must be declared as 'var'.")
                val propertyClass: KClass<*> = property.returnType.kClass
                mutableProperty.set(this, instance(propertyClass, property.qualifier))
            }
    }

    private val KType.kClass: KClass<*>
        get() {
            val classifier: KClassifier? = this.classifier

            return when (classifier) {
                is KClass<*> -> classifier

                is KTypeParameter ->
                    error(
                        "Generic type parameter ${classifier.name} cannot be resolved at runtime. " +
                            "Use a concrete type instead.",
                    )

                else -> error("Unsupported classifier type: $classifier")
            }
        }

    private val KAnnotatedElement.qualifier: String?
        get() =
            annotations
                .filterIsInstance<Qualifier>()
                .firstOrNull()
                ?.name
}
