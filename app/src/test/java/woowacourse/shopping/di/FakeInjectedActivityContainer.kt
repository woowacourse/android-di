package woowacourse.shopping.di

import com.example.sh1mj1.InjectedActivityContainer
import com.example.sh1mj1.InjectedComponent
import com.example.sh1mj1.Qualifier
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

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass) &&
                it::class.annotations.find { it.annotationClass == qualifier.annotationClass } != null
        }?.instance?.let {
            find(clazz)
        }

    override fun clear() {
        components.clear()
    }
}
