package com.re4rk.arkdiAndroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

open class DiAppCompatActivity : AppCompatActivity() {
    private lateinit var diViewModel: DiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diViewModel = (application as DiApplication).getActivityDiContainer(this)
        diViewModel.ownerDiContainer.inject(this)
    }

    fun <T : Any> injectViewModel(clazz: KClass<T>) =
        diViewModel.viewModelDiContainer.createInstance(clazz)
}
