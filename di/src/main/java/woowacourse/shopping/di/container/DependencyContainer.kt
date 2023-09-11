package woowacourse.shopping.di.container

import woowacourse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass

object DependencyContainer {
    private val instances: HashMap<Pair<KClass<*>, List<Annotation>>, Any> = hashMapOf()

    fun setInstance(kClass: KClass<*>, instance: Any) {
        val annotationWithQualifier = instance::class.annotations.filter {
            it.annotationClass.java.isAnnotationPresent(
                Qualifier::class.java,
            )
        }
        val key = Pair(
            kClass,
            annotationWithQualifier,
        )
        instances[key] = instance
    }

    fun getInstance(kClass: KClass<*>, annotations: List<Annotation> = emptyList()): Any? {
        val key = Pair(kClass, annotations)
        return instances[key]
    }

    fun clear() {
        instances.clear()
    }
}
