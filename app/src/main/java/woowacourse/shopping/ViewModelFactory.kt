package woowacourse.shopping

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.di.Scope

object ViewModelFactory {
    fun viewModelFactory(context: Context): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = context.applicationContext as MyApplication
                val scope = Scope("viewModel", app.appContainer.appScope)
                try {
                    val viewModel =
                        app.appContainer.di.resolve(modelClass.kotlin, scope = scope) as T
                    app.appContainer.di.injectFields(viewModel, scope)
                    require(viewModel is ScopedViewModel) { "DI로 생성하는 ViewModel은 ScopedViewModel을 상속해야 합니다" }
                    viewModel.attachScope(scope)
                    return viewModel
                } catch (exception: Throwable) {
                    scope.close()
                    throw exception
                }
            }
        }
}
