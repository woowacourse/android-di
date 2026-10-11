package woowacourse.shopping.di

import woowacourse.di.ScopeContext
import woowacourse.di.ScopeType

internal object ShoppingScopes {
    val application = ScopeType("application")
    val viewModel = ScopeType("view-model")
    val screen = ScopeType("screen")

    const val APPLICATION_SCOPE_ID = "application"

    val applicationContext =
        ScopeContext(
            scopeIds = mapOf(application to APPLICATION_SCOPE_ID),
        )

    fun viewModelContext(scopeId: String): ScopeContext =
        ScopeContext(
            scopeIds =
                mapOf(
                    application to APPLICATION_SCOPE_ID,
                    viewModel to scopeId,
                ),
        )

    fun screenContext(scopeId: String): ScopeContext =
        ScopeContext(
            scopeIds =
                mapOf(
                    application to APPLICATION_SCOPE_ID,
                    screen to scopeId,
                ),
        )
}
