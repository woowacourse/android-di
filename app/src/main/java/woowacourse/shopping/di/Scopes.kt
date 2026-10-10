package woowacourse.shopping.di

import woowacourse.di.ScopeType

internal val AppScope: ScopeType = ScopeType("application")
internal val ViewModelScope: ScopeType = ScopeType("view-model")
internal val ScreenScope: ScopeType = ScopeType("screen")
