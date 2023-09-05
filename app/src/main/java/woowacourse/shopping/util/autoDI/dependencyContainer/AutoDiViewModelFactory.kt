package woowacourse.shopping.util.autoDI.dependencyContainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.full.starProjectedType

object AutoDiViewModelFactory {
    @Suppress("UNCHECKED_CAST")
    val value: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>, extras: CreationExtras): VM {
            return DependencyContainer.searchViewModel(modelClass.kotlin.starProjectedType)
                .getInstance() as VM
        }
    }
}
