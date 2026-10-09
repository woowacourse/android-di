package woowacourse.shopping.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.cksckckcks.di.AutoDi
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.ShoppingScopes
import java.util.UUID
import kotlin.reflect.KClass

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val application = extras[APPLICATION_KEY] as ShoppingApplication
        val scope = application.container.diContainer.openScope(ShoppingScopes.viewModel, UUID.randomUUID().toString())

        return try {
            AutoDi(scope).createInstance(modelClass).also { it.addCloseable(scope) }
        } catch (error: Throwable) {
            scope.close()
            throw error
        }
    }
}
