package com.example.di.module

import android.content.Context

// 액티비티가 onDestory되어도 구성변경에 살아남는 모듈임. 때문에, 액티비티 컨텍스트를 가질 수 없음.
abstract class ActivityRetainedModule(applicationModule: ApplicationModule) :
    Module(applicationModule) {
    val applicationContext: Context = applicationModule.applicationContext
}
