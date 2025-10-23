package woowacourse.shopping.di

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.qualifier.ActivityScope
import woowacourse.shopping.di.qualifier.Database
import woowacourse.shopping.di.qualifier.InMemory
import woowacourse.shopping.di.qualifier.Singleton
import woowacourse.shopping.di.qualifier.ViewModelScope
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, MutableMap<Annotation?, () -> Any>>()
    private val instances = ConcurrentHashMap<Pair<KClass<*>, Annotation?>, Any>()

    private val implementationMap = mutableMapOf<KClass<*>, KClass<*>>()

    val viewModelFactory: ViewModelProvider.Factory by lazy {
        DIViewModelFactory(this)
    }

    fun inject(activity: Activity) {
        injectFields(activity)
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
        qualifier: Annotation? = null,
        isSingleton: Boolean = false,
    ): T {
        if (isSingleton) {
            val key = serviceClass to qualifier
            if (instances.containsKey(key)) {
                return instances[key] as T
            }
        }

        val provider = providers[serviceClass]?.get(qualifier)
        val instance =
            if (provider != null) {
                provider()
            } else {
                val concreteClass = implementationMap[serviceClass] ?: serviceClass
                injectFields(createInstance(concreteClass))
            }

        if (isSingleton) {
            val key = serviceClass to qualifier
            instances[key] = instance
        }
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
                        parameter.annotations.firstOrNull {
                            it is Database || it is InMemory
                        }
                    val isSingleton = parameter.annotations.any { it is Singleton }
                    get(parameterType, qualifier, isSingleton)
                }.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        return constructor.call(*args)
    }

    @SuppressLint("SuspiciousIndentation")
    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> injectFields(instance: T): T {
        val klass = instance::class
        klass.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>() && property is KMutableProperty1<*, *>) {
                property.isAccessible = true
                val dependencyType = property.returnType.classifier as KClass<*>

                if (property.annotations.any { it is ActivityScope }) {
                    val activity =
                        instance as? Activity

                            ?: throw IllegalStateException("엑티비티가 아닌 인스턴스입니다.")

                    val dependencyInstance = ActivityScopeManager.get(activity, dependencyType)

                    (property as KMutableProperty1<T, Any>).set(instance, dependencyInstance)
                } else if (instance is BaseViewModel && property.annotations.any { it is ViewModelScope }) {
                    val dependencyInstance = instance.getScoped(dependencyType)
                    (property as KMutableProperty1<T, Any>).set(instance, dependencyInstance)
                } else {
                    val qualifier =
                        property.annotations.firstOrNull {
                            it is Database || it is InMemory
                        }
                    val isSingleton = property.annotations.any { it is Singleton }
                    val dependencyInstance = get(dependencyType, qualifier, isSingleton)
                    (property as KMutableProperty1<T, Any>).set(instance, dependencyInstance)
                }
            }
        }
        return instance
    }
}
