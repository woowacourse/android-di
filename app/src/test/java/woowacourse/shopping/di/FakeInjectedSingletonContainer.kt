package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class FakeInjectedSingletonContainer(
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf(),
) : InjectedSingletonContainer {
    override fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance
}
