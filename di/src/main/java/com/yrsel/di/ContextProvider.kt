package com.yrsel.di

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextProvider {
    private lateinit var applicationContext: Context
    private var currentContext: Context? = null

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }

    fun register(activity: Activity) {
        currentContext = activity
    }

    fun unRegister(activity: Activity) {
        if (currentContext == activity) currentContext = null
    }

    fun getContext(): Context = currentContext ?: applicationContext
}
