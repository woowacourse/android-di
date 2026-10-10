package woowacourse.shopping

import androidx.lifecycle.ViewModel
import com.example.di.Scope

/** ViewModel이 정리될 때 자신이 소유한 DI 스코프도 정리한다. */
open class ScopedViewModel : ViewModel() {
    lateinit var diScope: Scope
        private set

    fun attachScope(scope: Scope) {
        check(!this::diScope.isInitialized) { "스코프가 이미 연결되어 있습니다" }
        diScope = scope
    }

    override fun onCleared() {
        if (this::diScope.isInitialized) {
            diScope.close()
        }
        super.onCleared()
    }
}
