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

abstract class DiEntryPointActivity(private val activityModuleClassType: Class<out ActivityModule>) :
    AppCompatActivity() {

    lateinit var activityModule: ActivityModule
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diApplication = application as DiApplication
        val previousHashCode = savedInstanceState?.getInt(ACTIVITY_INJECTOR_KEY)
        this.activityModule = diApplication.diContainer.provideActivityModule(
            this,
            previousHashCode,
            activityModuleClassType,
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            ACTIVITY_INJECTOR_KEY,
            this.hashCode(),
        ) // 객체가 사라지기 전에 해시코드값 저장. 강제 재생성시 모듈 다시 불러오기 위해.
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            val diApplication = application as DiApplication
            diApplication.diContainer.removeModule(this.hashCode())
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
                        activityModule.provideInstance(VM::class.java)
                    }
                }
            },
        )
    }

    companion object {
        private const val ACTIVITY_INJECTOR_KEY = "di_activity_injector_key"
    }
}
