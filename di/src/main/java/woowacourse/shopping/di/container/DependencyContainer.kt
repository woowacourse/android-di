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

    fun getInstance(kClass: KClass<*>, annotations: List<Annotation>): Any? {
        return instances.keys.firstOrNull { (clazz, annotationList) ->
            clazz == kClass && annotationList.containsAll(annotations)
        }?.let { instances[it] }
    }

    fun clear() {
        instances.clear()
    }
}
