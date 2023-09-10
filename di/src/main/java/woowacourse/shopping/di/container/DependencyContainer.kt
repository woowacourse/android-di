package woowacourse.shopping.di.container

import woowacourse.shopping.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object DependencyContainer {
    private val instances: HashMap<Pair<KClass<*>, List<Annotation>>, Any> = hashMapOf()

    fun setInstance(kClass: KClass<*>, instance: Any) {
        val key = Pair(
            kClass,
            kClass.annotations.filter { it.annotationClass.findAnnotation<Qualifier>() != null },
        )
        instances[key] = instance
    }

    fun getInstance(kClass: KClass<*>, annotations: List<Annotation>): Any? {
        val key = Pair(kClass, annotations)
        return instances[key]
    }
}
