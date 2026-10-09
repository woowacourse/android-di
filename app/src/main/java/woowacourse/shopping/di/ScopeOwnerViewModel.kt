package woowacourse.shopping.di

import androidx.lifecycle.ViewModel

internal class ScopeCloseable(
    private val scopeId: String,
    private val closeScope: (String) -> Unit,
) : AutoCloseable {
    override fun close() {
        closeScope(scopeId)
    }
}

internal class ScreenScopeOwnerViewModel(
    closeable: ScopeCloseable,
) : ViewModel(closeable)
