package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object InjectedActivityContainer {
    private val components: MutableList<InjectedComponent.InjectedActivityComponent> = mutableListOf()

    fun add(component: InjectedComponent.InjectedActivityComponent) {
        components.add(component)
    }

    fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance

    fun clear() {
        components.clear() // Activity 소멸 시점에서 Activity-scoped 객체들 제거
    }
}
