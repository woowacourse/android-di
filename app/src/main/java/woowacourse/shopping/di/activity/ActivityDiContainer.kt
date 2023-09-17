package woowacourse.shopping.di.activity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.boogiwoogi.di.Qualifier
import com.boogiwoogi.di.version2.DiContainer
import com.boogiwoogi.di.version2.Instance
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class ActivityDiContainer : DiContainer, DefaultLifecycleObserver {

    override var value: MutableList<Instance<out Any>>? = mutableListOf()

    override fun add(instance: Instance<*>) {
        value?.add(instance)
    }

    override fun find(clazz: KClass<*>): Any? = value?.find {
        it.clazz == clazz
    }?.value

    override fun find(simpleName: String?): Any? = value?.find {
        it.isSameAs(simpleName)
    }?.value

    override fun find(parameter: KParameter): Any? {
        return when (parameter.hasAnnotation<Qualifier>()) {
            true -> find(parameter.findAnnotation<Qualifier>()?.simpleName)
            false -> find(parameter.type.jvmErasure)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        value = mutableListOf()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        value = null
    }
}
