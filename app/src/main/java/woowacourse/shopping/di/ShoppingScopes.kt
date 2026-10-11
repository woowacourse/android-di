package woowacourse.shopping.di

import woowacourse.di.ScopeType

object ShoppingScopes {
    val ViewModel = ScopeType("viewmodel")
    val Screen = ScopeType("screen")
}
