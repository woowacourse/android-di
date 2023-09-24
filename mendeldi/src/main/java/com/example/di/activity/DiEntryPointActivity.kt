package com.example.di.activity

import android.os.Bundle
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

abstract class DiEntryPointActivity : AppCompatActivity() {

    lateinit var activityRetainedModule: ActivityRetainedModule // DiActivityRetainedModuleContainer에서 액티비티의 생명주기를 관찰하고 제거시킴.
        private set

    private lateinit var activityModule: ActivityModule

    private val diApplication by lazy { application as DiApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val previousHashCode = savedInstanceState?.getInt(ACTIVITY_RETAINED_MODULE_KEY)
        activityRetainedModule =
            diApplication.getActivityRetainedModule(this, previousHashCode)

        activityModule = diApplication.getActivityModule(this, activityRetainedModule)
        activityModule.inject(this) // DiEntryPointActivity 객체에 대한 필드 주입
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 객체가 사라지기 전에 해시코드값 저장. 강제 재생성시 모듈 다시 불러오기 위해
        outState.putInt(ACTIVITY_RETAINED_MODULE_KEY, this.hashCode())
    }

    @MainThread
    inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            {
                viewModelFactory {
                    initializer {
                        val viewModelModule: ViewModelModule =
                            (application as DiApplication).getViewModelModule(activityRetainedModule)
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
