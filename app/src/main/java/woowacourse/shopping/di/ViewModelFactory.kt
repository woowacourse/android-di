package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val application =
            requireNotNull(extras[APPLICATION_KEY] as? ShoppingApplication) {
                "ViewModel을 생성하려면 ShoppingApplication이 필요합니다."
            }
        return application.injector.create(modelClass.kotlin)
    }
}
