package woowacourse.shopping.dependency

import woowacourse.shopping.Qualifier
import kotlin.reflect.KClass

class DependencyContainer {
    private val container = mutableMapOf<KClass<*>, MutableList<DependencyValue>>()

    fun addInstance(clazz: KClass<*>, annotations: List<Annotation>, instance: Any) {
        if (container[clazz] == null) {
            container[clazz] = mutableListOf(DependencyValue(instance, annotations))
            return
        }
        container[clazz]!!.add(DependencyValue(instance, annotations))
    }

    fun getInstance(clazz: KClass<*>, qualifierTag: String? = null): Any? {
        if (qualifierTag == null) {
            return container[clazz]?.first()?.instance
        }
        return container[clazz]?.find {
            val qualifier = it.annotations.filterIsInstance<Qualifier>().firstOrNull()
            qualifier?.className == qualifierTag
        }?.instance
    }

    fun clear() {
        container.clear()
    }

    companion object {
        private var Instance: DependencyContainer? = null
        fun getSingletonInstance(): DependencyContainer {
            return Instance ?: synchronized(this) {
                return Instance ?: DependencyContainer().also { Instance = it }
            }
        }
    }
}
