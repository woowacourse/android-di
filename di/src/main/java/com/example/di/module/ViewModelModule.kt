package com.example.di.module

import android.content.Context
import androidx.lifecycle.ViewModel

// 뷰모델 모듈도 액티비티보다 생명주기가 길기 때문에, 액티비티 컨텍스트를 참조하지 못한다.
class ViewModelModule(activityRetainedModule: ActivityRetainedModule) :
    Module(activityRetainedModule) {
    val applicationContext: Context = activityRetainedModule.applicationContext

    fun <VM : ViewModel> createViewModel(clazz: Class<VM>): VM {
        return provideInstance(clazz)
    }
}
