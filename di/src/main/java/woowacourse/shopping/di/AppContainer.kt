package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.annotation.Inject
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()
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
        provider: () -> T,
    ) {
        providers[type] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(serviceClass: KClass<T>): T {
        if (instances.containsKey(serviceClass)) {
            return instances[serviceClass] as T
        }

        val provider = providers[serviceClass]
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
        val constructor =
            concreteClass.constructors.first()
        val args =
            constructor.parameters
                .map { parameter ->
                    val parameterType = parameter.type.classifier as KClass<*>
                    get(parameterType)
                }.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        return constructor.call(*args)
    }

    private fun <T : Any> injectFields(instance: T): T {
        val klass = instance::class
        klass.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>() && property is KMutableProperty1<*, *>) {
                property.isAccessible = true
                val dependencyType = property.returnType.classifier as KClass<*>
                val dependencyInstance = get(dependencyType)
                (property as KMutableProperty1<T, Any>).set(instance, dependencyInstance)
            }
        }
        @Suppress("UNCHECKED_CAST")
        return instance
    }
}
