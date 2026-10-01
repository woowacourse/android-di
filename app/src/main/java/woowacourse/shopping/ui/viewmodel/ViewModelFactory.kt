package woowacourse.shopping.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ShoppingApplication
import kotlin.reflect.KClass

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val application = extras[APPLICATION_KEY] as ShoppingApplication
        val autoDi = application.autoDi

        return autoDi.createInstance(modelClass)
    }
}
