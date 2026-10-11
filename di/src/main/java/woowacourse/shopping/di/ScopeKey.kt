package woowacourse.shopping.di

interface ScopeType

data class ScopeKey(
    val type: ScopeType,
    val id: String,
)