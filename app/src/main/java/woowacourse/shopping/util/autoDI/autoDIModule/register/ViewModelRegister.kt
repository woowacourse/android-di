package woowacourse.shopping.util.autoDI.autoDIModule.register

import androidx.lifecycle.ViewModel
import woowacourse.shopping.util.autoDI.autoDIModule.ViewModelBundles

class ViewModelRegister(private val viewModels: ViewModelBundles) {
    internal fun <VM : ViewModel> register(initializeMethod: () -> VM) {
        viewModels.add(initializeMethod)
    }
}
