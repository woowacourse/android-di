package woowacourse.shopping.android.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.core.di.NewDependencyContainer.instance

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            owner = this,
            factory = ViewModelFactory,
        )[VM::class.java]
    }

@PublishedApi
internal object ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T = instance(modelClass.kotlin)
}
