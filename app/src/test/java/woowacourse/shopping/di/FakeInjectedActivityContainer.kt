package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class FakeInjectedActivityContainer(
    private val components: MutableList<InjectedComponent.InjectedActivityComponent> = mutableListOf(),
) : InjectedActivityContainer {
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
