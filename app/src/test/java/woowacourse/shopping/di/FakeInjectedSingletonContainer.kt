package woowacourse.shopping.di

import com.example.sh1mj1.ComponentKey
import com.example.sh1mj1.InjectedComponent
import com.example.sh1mj1.InjectedSingletonContainer
import com.example.sh1mj1.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class FakeInjectedSingletonContainer(
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf(),
) : InjectedSingletonContainer {
    override fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any? =
        components.find { component ->
            clazz.isSuperclassOf(component.injectedClass) && qualifier.value == component.qualifier?.value
        }?.instance


    override fun findWithKey(componentKey: ComponentKey): Any {
        TODO("Not yet implemented")
    }
}
