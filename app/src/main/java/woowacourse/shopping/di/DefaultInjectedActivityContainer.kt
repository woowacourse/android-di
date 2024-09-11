package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object DefaultInjectedActivityContainer : InjectedActivityContainer {
    private val components: MutableList<InjectedComponent.InjectedActivityComponent> = mutableListOf()

    override fun add(component: InjectedComponent.InjectedActivityComponent) {
        components.add(component)
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance

    override fun clear() {
        components.clear()
    }
}
