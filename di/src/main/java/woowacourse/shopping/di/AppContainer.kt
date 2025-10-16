package woowacourse.shopping.di

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.qualifier.Database
import woowacourse.shopping.di.qualifier.InMemory
import woowacourse.shopping.di.qualifier.Qualifiers
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, MutableMap<Annotation?, () -> Any>>()
    private val instances = ConcurrentHashMap<KClass<*>, Any>()

    private val implementationMap = mutableMapOf<KClass<*>, KClass<*>>()

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        DIViewModelFactory(this)
    }

    fun <T : Any> registerImplementation(
        interfaceClass: KClass<T>,
        implementationClass: KClass<out T>,
    ) {
        implementationMap[interfaceClass] = implementationClass
    }

    fun <T : Any> registerProvider(
        type: KClass<T>,
        annotation: Annotation?,
        provider: () -> T,
    ) {
        providers.getOrPut(type) { mutableMapOf() }[annotation] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        serviceClass: KClass<T>,
        annotation: Annotation?,
    ): T {
        if (instances.containsKey(serviceClass)) {
            return instances[serviceClass] as T
        }

        val provider = providers[serviceClass]?.get(annotation)
        Log.d("test", provider.toString())
        val instance =
            if (provider != null) {
                provider()
            } else {
                val concreteClass = implementationMap[serviceClass] ?: serviceClass
                injectFields(createInstance(concreteClass))
            }

        instances[serviceClass] = instance
        return instance as T
    }

    fun <T : Any> createInstance(concreteClass: KClass<T>): T {
        Log.d("concreteClass", "$concreteClass")
        val constructor =
            concreteClass.constructors.first()
        val args =
            constructor.parameters
                .map { parameter ->
                    val parameterType = parameter.type.classifier as KClass<*>
                    val qualifier =
                        when {
                            parameter.annotations.any { it is Database } -> Qualifiers.DATABASE
                            parameter.annotations.any { it is InMemory } -> Qualifiers.IN_MEMORY
                            else -> null
                        }
                    get(parameterType, qualifier)
                }.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        return constructor.call(*args)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> injectFields(instance: T): T {
        val klass = instance::class
        klass.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>() && property is KMutableProperty1<*, *>) {
                property.isAccessible = true
                val dependencyType = property.returnType.classifier as KClass<*>
                val dependencyInstance =
                    get(
                        dependencyType,
                        when {
                            property.annotations.any { it is Database } -> Qualifiers.DATABASE
                            property.annotations.any { it is InMemory } -> Qualifiers.IN_MEMORY
                            else -> null
                        },
                    )
                (property as KMutableProperty1<T, Any>).set(instance, dependencyInstance)
            }
        }
        return instance
    }
}
