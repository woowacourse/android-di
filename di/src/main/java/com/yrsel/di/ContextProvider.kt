package com.yrsel.di

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object ContextProvider {
    private lateinit var applicationContext: Context
    private var context: Context? = null

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }

    fun register(activity: Activity) {
        context = activity
    }

    fun unRegister(activity: Activity) {
        if (context == activity) context = null
    }

    fun getContext(): Context = context ?: applicationContext

    fun getApplicationContext(): Context = applicationContext
}
