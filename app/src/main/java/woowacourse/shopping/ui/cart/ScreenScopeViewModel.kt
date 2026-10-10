package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import com.example.di.Scope

/** NavBackStackEntry의 ViewModelStore가 정리될 때 화면 스코프도 종료한다. */
class ScreenScopeViewModel(
    appScope: Scope,
) : ViewModel() {
    val scope = Scope("screen", appScope)

    override fun onCleared() {
        scope.close()
        super.onCleared()
    }
}
