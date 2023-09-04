package woowacourse.shopping.di.activity

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.application.DiApplication
import woowacourse.shopping.di.module.ActivityModule
import kotlin.reflect.full.primaryConstructor

abstract class DiEntryPointActivity<T : ActivityModule>(activityModuleClassType: Class<T>) :
    AppCompatActivity() {

    val autoInjector: Injector
        get() = DiApplication.getInjectorForInstance(this.hashCode())

    init {
        // 애플리케이션 모듈에 대한 참조를 갖고 있는 액티비티 모듈을 생성해서, 이걸 인젝터 객체 생성자에 넘겨서 객체 생성하고 애플리케이션이 가진 맵에 저장시킴. 이 액티비티가 살아있는 한 같은 인젝터를 얻게 된다.
        if (!DiApplication.hasInjectorForInstance(this.hashCode())) {
            val activityModule =
                activityModuleClassType.kotlin.primaryConstructor?.call(DiApplication.applicationModule)
                    ?: throw NullPointerException("주생성자 없음")
            DiApplication.addInjectorForInstance(
                this.hashCode(),
                AutoInjector(activityModule),
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) DiApplication.removeInjectorForInstance(this.hashCode())
    }

    @MainThread
    inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            {
                viewModelFactory {
                    initializer {
                        autoInjector.inject(VM::class.java)
                    }
                }
            },
        )
    }
}
