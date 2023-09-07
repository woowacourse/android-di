package woowacourse.shopping.util.autoDI.dependencyContainer

import androidx.lifecycle.ViewModel
import woowacourse.shopping.util.autoDI.ViewModelBundle
import kotlin.reflect.KType

class ViewModelBundles(value: MutableList<ViewModelBundle<*>>) {
    private val _value: MutableList<ViewModelBundle<*>> = value
    val value: List<ViewModelBundle<*>> get() = _value.toList()

    fun <VM : ViewModel> add(initializeMethod: () -> VM) {
        _value.add(ViewModelBundle(initializeMethod))
    }

    fun search(kType: KType): ViewModelBundle<*>? {
        return value.find { it.type == kType }
    }
}
