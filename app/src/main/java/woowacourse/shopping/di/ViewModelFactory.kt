package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.di.Injector
import woowacourse.shopping.ShoppingApplication
import java.util.UUID

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val application =
            requireNotNull(extras[APPLICATION_KEY] as? ShoppingApplication) {
                "ViewModel을 생성하려면 ShoppingApplication이 필요합니다."
            }
        return application.injector.createViewModel(modelClass)
    }
}

internal const val VIEW_MODEL_SCOPE_KEY = "shopping.di.viewmodel"

internal fun <T : ViewModel> Injector.createViewModel(modelClass: Class<T>): T {
    val scope = openScope("viewmodel:${UUID.randomUUID()}", ShoppingScopes.ViewModel)
    return try {
        scope.create(modelClass.kotlin).also { it.addCloseable(VIEW_MODEL_SCOPE_KEY, scope) }
    } catch (failure: Throwable) {
        try {
            scope.close()
        } catch (cleanupFailure: Throwable) {
            failure.addSuppressed(cleanupFailure)
        }
        throw failure
    }
}
