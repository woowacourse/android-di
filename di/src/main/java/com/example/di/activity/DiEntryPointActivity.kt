package com.example.di.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.di.application.DiApplication
import com.example.di.module.ActivityModule
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ViewModelModule
import kotlin.reflect.KClass

abstract class DiEntryPointActivity(
    private val activityModuleClassType: Class<out ActivityModule>,
    private val activityRetainedModuleClassType: Class<out ActivityRetainedModule>,
) : AppCompatActivity() {

    lateinit var activityRetainedModule: ActivityRetainedModule
        private set

    private val diApplication by lazy { application as DiApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val previousHashCode = savedInstanceState?.getInt(ACTIVITY_RETAINED_MODULE_KEY)
        activityRetainedModule = diApplication.getActivityRetainedModule(
            this.hashCode(),
            previousHashCode,
            activityRetainedModuleClassType,
        )

        val primaryConstructor =
            ActivityModule.validatePrimaryConstructor(activityModuleClassType)
        val activityModule = primaryConstructor.call(this, activityRetainedModule)
        activityModule.inject(this) // DiEntryPointActivity 객체에 대한 필드 주입
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 객체가 사라지기 전에 해시코드값 저장. 강제 재생성시 모듈 다시 불러오기 위해
        outState.putInt(ACTIVITY_RETAINED_MODULE_KEY, this.hashCode())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) diApplication.removeActivityRetainedModule(this.hashCode())
    }

    inline fun <reified VM : ViewModel> ComponentActivity.viewModel(viewModelModuleClazz: KClass<out ViewModelModule>): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            {
                viewModelFactory {
                    initializer {
                        val primaryConstructor =
                            ViewModelModule.validatePrimaryConstructor(viewModelModuleClazz.java)
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
