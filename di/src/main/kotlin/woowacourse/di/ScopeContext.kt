package woowacourse.di

data class ScopeContext(
    val defaultScopeId: String? = null,
    val scopeIds: Map<ScopeType, String>,
)
