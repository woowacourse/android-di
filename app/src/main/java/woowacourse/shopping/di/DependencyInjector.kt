package woowacourse.shopping.di

import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

typealias qualifier = KClass<*>?
typealias instance = Any

object DependencyInjector {
    private val instances = mutableMapOf<Pair<KClass<*>, qualifier>, instance>()

    fun <T : Any> findInstance(
        clazz: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        return instances[clazz to qualifier] as? T ?: createInstance(clazz, qualifier)
    }

    fun <T : Any> addInstance(
        clazz: KClass<T>,
        instance: T,
        qualifier: KClass<*>? = null,
    ) {
        instances[clazz to qualifier] = instance
    }

    fun <T : Any> createInstance(
        clazz: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        val constructor = clazz.primaryConstructor ?: findInstance(clazz, qualifier)
        val dependencies: List<Any?> = (constructor as KFunction<T>).extractDependencies() // 생성자가 필요로 하는 의존성들을 추출해
        val instance = constructor.call(*dependencies.toTypedArray())

        injectFields(instance)

        return instance
    }

    private fun <T : Any> KFunction<T>.extractDependencies(): List<Any?> {
        return parameters.map { parameter ->
            val classifier: KClass<*> = parameter.type.classifier as KClass<*>
            findInstance(classifier)
        }
    }

    private fun <T : Any> injectFields(instance: T) {
        val properties =
            instance::class.declaredMemberProperties.filter { kProperty ->
                kProperty.hasAnnotation<Inject>()
            }

        properties.forEach { kProperty ->
            val classifier: KClass<*> = kProperty.returnType.classifier as KClass<*>
            val qualifier: Annotation = kProperty.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() } ?: return
            val qualifierClass: KClass<out Annotation> = qualifier.annotationClass

            val dependency = findInstance(classifier, qualifierClass)

            kProperty as KMutableProperty1
            kProperty.setter.call(instance, dependency)
        }
    }
}
