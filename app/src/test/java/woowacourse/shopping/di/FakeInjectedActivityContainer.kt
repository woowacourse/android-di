package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class FakeInjectedActivityContainer(
    private val components: MutableList<com.example.sh1mj1.InjectedComponent.InjectedActivityComponent> = mutableListOf(),
) : com.example.sh1mj1.InjectedActivityContainer {
    override fun add(component: com.example.sh1mj1.InjectedComponent.InjectedActivityComponent) {
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
