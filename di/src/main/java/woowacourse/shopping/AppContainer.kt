package woowacourse.shopping

import woowacourse.shopping.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class AppContainer {
    private val instances = mutableMapOf<QualifierKey, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(
        clazz: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        findInstance(clazz, qualifier)?.let { instance -> return instance as T }
        return createInstance(clazz).also { instance -> bind(clazz, instance, qualifier) }
    }

    fun injectField(instance: Any) =
        instance::class.java
            .declaredFields
            .filter { field -> field.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                val depClass = field.type.kotlin
                val depQualifier =
                    field.annotations
                        .map { it.annotationClass }
                        .firstOrNull { it != Inject::class }
                val dependency = getInstance(depClass, depQualifier)
                field.isAccessible = true
                field.set(instance, dependency)
            }

    fun <T : Any> bind(
        clazz: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) = instances.putIfAbsent(QualifierKey(clazz, qualifier), instance)

    private fun findInstance(
        clazz: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): Any? = instances[QualifierKey(clazz, qualifier)] ?: instances[QualifierKey(clazz, null)]

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor =
            requireNotNull(clazz.primaryConstructor) {
                "${clazz.qualifiedName} 클래스에 생성자가 존재하지 않습니다."
            }

        val args =
            constructor.parameters
                .map { param ->
                    val depClass =
                        requireNotNull(param.type.classifier as KClass<*>) {
                            "지원하지 않는 타입입니다: ${param.type}"
                        }
                    val depQualifier =
                        param.annotations
                            .map { it.annotationClass }
                            .firstOrNull { it != Inject::class }
                    getInstance(depClass, depQualifier)
                }.toTypedArray()

        return constructor.call(*args)
    }

    private data class QualifierKey(
        val clazz: KClass<*>,
        val qualifier: KClass<out Annotation>? = null,
    )
}
