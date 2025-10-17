package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

abstract class BaseViewModel : ViewModel() {
    private val scopedInstances = ConcurrentHashMap<KClass<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getScoped(serviceClass: KClass<T>): T {
        if (scopedInstances.containsKey(serviceClass)) {
            return scopedInstances[serviceClass] as T
        }
        val instance = AppContainer.get(serviceClass)
        scopedInstances[serviceClass] = instance
        return instance
    }

    override fun onCleared() {
        super.onCleared()
        scopedInstances.clear()
    }
}
