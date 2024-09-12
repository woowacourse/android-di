package woowacourse.shopping.di

import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

object DefaultInjectedSingletonContainer : InjectedSingletonContainer {
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf()

    override fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    override fun find(clazz: KClass<*>): Any? =
        components.find {
            clazz.isSuperclassOf(it.injectedClass)
        }?.instance

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any? {
        return components.find {
            clazz.isSuperclassOf(it.injectedClass) && qualifier.value == it.qualifier?.value
        }?.instance.also {
            Log.d(TAG, "find with kclass and qualifier: $it")
        }
    }
}

private const val TAG = "DefaultInjectedSingleto"
