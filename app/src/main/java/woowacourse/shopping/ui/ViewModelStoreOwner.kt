package woowacourse.shopping.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.instance

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            owner = this,
            factory =
                object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(
                        modelClass: Class<T>,
                        extras: CreationExtras,
                    ): T {
                        val application: Application = checkNotNull(extras[APPLICATION_KEY])
                        val appContainer: AppContainer = application as AppContainer

                        return appContainer.instance<VM>() as T
                    }
                },
        )[VM::class.java]
    }
