package woowacourse.shopping.util.autoDI.lifecycleTypeRegister

import androidx.lifecycle.ViewModel
import woowacourse.shopping.util.autoDI.dependencyContainer.ViewModelBundles

class ViewModelRegister(private val viewModels: ViewModelBundles) {
    fun <VM : ViewModel> register(initializeMethod: () -> VM) {
        viewModels.add(initializeMethod)
    }
}
