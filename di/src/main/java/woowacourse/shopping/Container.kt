package woowacourse.shopping

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import woowacourse.shopping.annotation.Singleton
import woowacourse.shopping.model.Key
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

typealias Provider<T> = () -> T

class Container {
    private val providers = mutableMapOf<Key, Provider<*>>()
    private val singletons = mutableMapOf<Key, Any>()
    private val creating = ThreadLocal.withInitial { mutableSetOf<Key>() }

    fun <T : Any> bind(
        type: KClass<T>,
        provider: Provider<T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = Key.of(type, qualifier)
        providers[key] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = Key.of(type, qualifier)
        singletons[key]?.let { instance -> return instance as T }
        providers[key]?.let { provider -> return (provider as Provider<T>).invoke() }
        return createAndReturn(type, qualifier)
    }

    inline fun <reified T : Any> get(): T = get(T::class)

    fun installModule(module: Any) {
        module::class.declaredMemberFunctions.forEach { function ->
            val returnType = function.returnType.classifier as? KClass<*>
            if (returnType !is KClass<*>) return@forEach

            val qualifier = findQualifierAnnotation(function)
            val key = Key.of(returnType, qualifier)

            val provider = createProvider(module, function)
            if (function.hasAnnotation<Singleton>()) {
                providers[key] = {
                    singletons[key] ?: synchronized(this) {
                        singletons[key] ?: (provider.invoke() as Any).also { instance ->
                            singletons[key] = instance
                        }
                    }
                }
            } else {
                providers[key] = provider
            }
        }
    }

    fun <T : Any> injectField(instance: T) {
        instance::class.java
            .declaredFields
            .filter { field -> field.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                val type = field.type.kotlin
                val qualifier =
                    field.annotations
                        .map { it.annotationClass }
                        .firstOrNull { it != Inject::class }
                field.isAccessible = true
                field.set(instance, get(type, qualifier))
            }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createAndReturn(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = Key.of(type, qualifier)
        val creatingSet = creating.get()

        if (key in creatingSet) throw DependencyCycleException("순환 참조가 발생하였습니다.")

        try {
            creatingSet += key

            val constructor =
                findInjectableConstructor(type)
                    ?: throw NoProviderException("@Inject 생성자를 찾을 수 없습니다: ${type.simpleName}")
            val args =
                constructor.parameters
                    .map { param ->
                        val paramClass =
                            param.type.classifier as? KClass<*>
                                ?: throw NoProviderException("지원하지 않는 타입입니다: ${param.type}")
                        val paramQualifier = findQualifierAnnotation(param)
                        get(paramClass, paramQualifier)
                    }.toTypedArray()
            constructor.isAccessible = true
            val instance = constructor.call(*args)
            injectField(instance)

            if (type.hasAnnotation<Singleton>()) singletons[key] = instance
            return instance
        } finally {
            creatingSet.remove(key)
        }
    }

    private fun <T> createProvider(
        module: Any,
        function: KFunction<T>,
    ): Provider<T> =
        {
            val args =
                function.parameters
                    .drop(1)
                    .map { param ->
                        val paramClass =
                            param.type.classifier as? KClass<*>
                                ?: throw NoProviderException("지원하지 않는 타입입니다: ${param.type}")
                        val paramQualifier = findQualifierAnnotation(param)
                        get(paramClass, paramQualifier)
                    }.toTypedArray()
            function.isAccessible = true
            function.call(module, *args)
        }

    private fun findQualifierAnnotation(element: KAnnotatedElement): KClass<out Annotation>? =
        element.annotations
            .map { annotation -> annotation.annotationClass }
            .find { annotationClass -> annotationClass.hasAnnotation<Qualifier>() }

    private fun <T : Any> findInjectableConstructor(type: KClass<T>): KFunction<T>? =
        type.constructors.firstOrNull { it.hasAnnotation<Inject>() } ?: type.primaryConstructor
}
