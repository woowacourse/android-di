package woowacourse.shopping.di

internal class ScopeCloseable(
    private val scopeId: String,
    private val closeScope: (String) -> Unit,
) : AutoCloseable {
    override fun close() {
        closeScope(scopeId)
    }
}
