package woowacourse.shopping.util.autoDI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.util.autoDI.autoDIContainer.AutoDIModuleContainer
import kotlin.reflect.full.starProjectedType

@Suppress("UNCHECKED_CAST")
object AutoDiViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>, extras: CreationExtras): VM {
        return AutoDIModuleContainer.searchViewModelBundle(modelClass.kotlin.starProjectedType)
            .getInstance() as VM
    }
}
