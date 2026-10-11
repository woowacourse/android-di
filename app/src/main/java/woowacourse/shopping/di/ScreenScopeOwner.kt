package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.di.Scope

class ScreenScopeOwner internal constructor(
    val scope: Scope,
) : ViewModel() {
    init {
        addCloseable(scope)
    }
}
