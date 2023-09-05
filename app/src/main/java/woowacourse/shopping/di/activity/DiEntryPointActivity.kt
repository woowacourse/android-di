package woowacourse.shopping.di.activity

import android.os.Bundle
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

abstract class DiEntryPointActivity<T : ActivityModule>(private val activityModuleClassType: Class<T>) :
    AppCompatActivity() {

    val autoInjector: Injector
        get() = DiApplication.getInjectorForInstance(this.hashCode())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val previousHashCode = savedInstanceState?.getInt(ACTIVITY_INJECTOR_KEY)
        val activityModule =
            activityModuleClassType.kotlin.primaryConstructor?.call(DiApplication.applicationModule)
                ?: throw NullPointerException("주생성자 없음")

        // 이전에 만들었던 인젝터 객체가 있는지 검사
        if (previousHashCode == null) {
            addInjectorToDiApplication(this.hashCode(), AutoInjector(activityModule))
        } else {
            if (!DiApplication.hasInjectorForInstance(previousHashCode)) {
                addInjectorToDiApplication(this.hashCode(), AutoInjector(activityModule))
            } else {
                val previousInjector = DiApplication.getInjectorForInstance(previousHashCode)
                DiApplication.removeInjectorForInstance(previousHashCode)
                addInjectorToDiApplication(this.hashCode(), previousInjector) // 새 해시값으로 다시 등록
            }
        }
    }

    // 애플리케이션 모듈에 대한 참조를 갖고 있는 액티비티 모듈을 생성해서, 이걸 인젝터 객체 생성자에 넘겨서 객체 생성하고 애플리케이션이 가진 맵에 저장시킴. 이 액티비티가 살아있는 한 같은 인젝터를 얻게 된다.
    private fun addInjectorToDiApplication(hashCode: Int, injector: Injector) {
        DiApplication.addInjectorForInstance(hashCode, injector)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            ACTIVITY_INJECTOR_KEY,
            this.hashCode(),
        ) // 객체가 사라지기 전에 해시코드값 저장. 강제 재생성시 인젝터 다시 불러오기 위해.
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            DiApplication.removeInjectorForInstance(this.hashCode())
        }
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

    companion object {
        private const val ACTIVITY_INJECTOR_KEY = "di_activity_injector_key"
    }
}
