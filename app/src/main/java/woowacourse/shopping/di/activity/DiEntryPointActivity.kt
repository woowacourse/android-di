package woowacourse.shopping.di.activity

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.module.ActivityModule
import woowacourse.shopping.global.ShoppingApplication
import kotlin.reflect.full.primaryConstructor

abstract class DiEntryPointActivity<T : ActivityModule>(activityModuleClassType: Class<T>) :
    AppCompatActivity() {
    private var _autoInjector: Injector? = null
    protected val autoInjector: Injector
        get() = _autoInjector!!

    init {
        // 애플리케이션 모듈에 대한 참조를 갖고 있는 액티비티 모듈을 생성해서, 이걸 인젝터 객체 생성자에 넘겨서 객체 생성.
        val activityModule =
            activityModuleClassType.kotlin.primaryConstructor?.call(ShoppingApplication.applicationModule)
                ?: throw NullPointerException("주생성자 없음")
        _autoInjector = AutoInjector(activityModule)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            _autoInjector = null
        }
    }

    @MainThread
    inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            ::viewModelFactory,
        )
    }

    fun viewModelFactory() = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return autoInjector.inject(modelClass)
        }
    }
}
