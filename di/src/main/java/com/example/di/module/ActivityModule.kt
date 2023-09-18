package com.example.di.module

import android.content.Context
import com.example.di.activity.DiEntryPointActivity

// 액티비티가 onDestory될 때마다 죽는 모듈임.
abstract class ActivityModule(
    val activityContext: Context,
    activityRetainedModule: ActivityRetainedModule,
) : Module(activityRetainedModule) {
    val applicationContext: Context = activityRetainedModule.applicationContext
    fun inject(diEntryPointActivity: DiEntryPointActivity) { // 액티비티에 필드 주입해주는 메소드
        provideInjectField(diEntryPointActivity)
    }
}
