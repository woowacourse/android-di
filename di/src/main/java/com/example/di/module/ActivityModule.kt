package com.example.di.module

import android.content.Context

abstract class ActivityModule(activityContext: Context, applicationModule: ApplicationModule) :
    Module(activityContext, applicationModule)
