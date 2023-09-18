package com.example.di.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.di.application.DiApplication
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ViewModelModule
import kotlin.reflect.full.primaryConstructor

abstract class DiEntryPointActivity(
    private val activityModuleClassType: Class<out ActivityModule>,
    private val activityRetainedModuleClassType: Class<out ActivityRetainedModule>,
) : AppCompatActivity() {

    lateinit var activityRetainedModule: ActivityRetainedModule
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diApplication = application as DiApplication
        val previousHashCode = savedInstanceState?.getInt(ACTIVITY_RETAINED_MODULE_KEY)
        activityRetainedModule =
            diApplication.diActivityRetainedContainer.provideActivityRetainedModule(
                this.hashCode(),
                previousHashCode,
                activityRetainedModuleClassType,
            )

        val primaryConstructor =
            activityModuleClassType.kotlin.primaryConstructor ?: throw NullPointerException()
        val activityModule = primaryConstructor.call(
            this,
            activityRetainedModule,
        ) // 액티비티 모듈은 액티비티 리테인트 모듈과 달리, 액티비티가 onCreate될 때마다 새로 생긴다.
        activityModule.inject(this) // DiEntryPointActivity 객체에 대한 필드 주입
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            ACTIVITY_RETAINED_MODULE_KEY,
            this.hashCode(),
        ) // 객체가 사라지기 전에 해시코드값 저장. 강제 재생성시 모듈 다시 불러오기 위해.
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            val diApplication = application as DiApplication
            diApplication.diActivityRetainedContainer.removeModule(this.hashCode())
        }
    }

    @MainThread
    inline fun <reified VM : ViewModel> ComponentActivity.viewModel(viewModelModuleClazz: Class<out ViewModelModule>): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            {
                viewModelFactory {
                    initializer {
                        val primaryConstructor = viewModelModuleClazz.kotlin.primaryConstructor
                            ?: throw NullPointerException()
                        val viewModelModule = primaryConstructor.call(activityRetainedModule)
                        viewModelModule.createViewModel(VM::class.java)
                    }
                }
            },
        )
    }

    companion object {
        private const val ACTIVITY_RETAINED_MODULE_KEY = "di_activity_injector_key"
    }
}
