package com.re4rk.arkdiAndroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

open class ArkAppCompatActivity : AppCompatActivity() {
    private lateinit var arkViewModel: ArkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arkViewModel = (application as ArkApplication).getActivityDiContainer(this)
        arkViewModel.ownerArkContainer.inject(this)
    }

    fun <T : Any> injectViewModel(clazz: KClass<T>) =
        arkViewModel.viewModelArkContainer.createInstance(clazz)
}
