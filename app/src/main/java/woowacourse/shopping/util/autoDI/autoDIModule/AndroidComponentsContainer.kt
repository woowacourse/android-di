package woowacourse.shopping.util.autoDI.autoDIModule

import androidx.lifecycle.ViewModel
import woowacourse.shopping.util.autoDI.ViewModelBundle
import woowacourse.shopping.util.autoDI.autoDIModule.register.ViewModelRegister
import kotlin.reflect.KType

internal class AndroidComponentsContainer {
    private val viewModelBundles: ViewModelBundles = ViewModelBundles(mutableListOf())
    private val viewModelRegister: ViewModelRegister = ViewModelRegister(viewModelBundles)

    internal fun <VM : ViewModel> registerViewModel(registerBlock: () -> VM) {
        viewModelRegister.register(registerBlock)
    }

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*>? =
        viewModelBundles.search(kType)
}
