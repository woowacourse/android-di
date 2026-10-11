package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DIResolver
import java.util.concurrent.atomic.AtomicLong

class DIViewModelFactory(
    private val parentResolver: DIResolver,
    private val scopeIdProvider: (Class<out ViewModel>) -> String = ::nextScopeId,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelScope = parentResolver.openScope(ViewModelScope, scopeIdProvider(modelClass))
        return try {
            viewModelScope.create(modelClass.kotlin).also { viewModel ->
                viewModel.addCloseable(viewModelScope)
            }
        } catch (throwable: Throwable) {
            viewModelScope.close()
            throw throwable
        }
    }

    companion object {
        private val nextId: AtomicLong = AtomicLong()

        private fun nextScopeId(modelClass: Class<out ViewModel>): String = "${modelClass.name}:${nextId.getAndIncrement()}"
    }
}
