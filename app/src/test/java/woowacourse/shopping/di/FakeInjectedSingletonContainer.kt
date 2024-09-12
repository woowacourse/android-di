package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class FakeInjectedSingletonContainer(
    private val components: MutableList<com.example.sh1mj1.InjectedComponent.InjectedSingletonComponent> = mutableListOf(),
) : com.example.sh1mj1.InjectedSingletonContainer {
    override fun add(component: com.example.sh1mj1.InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance
}
