package io.hyemdooly.androiddi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.hyemdooly.di.Module
import kotlin.reflect.KClass

abstract class HyemdoolyActivity : AppCompatActivity() {
    private val hyemdoolyViewModel: HyemdoolyViewModel by lazy {
        (application as HyemdoolyApplication).getHyemdoolyViewModel(this)
    }
    private val activityModule: Module by lazy {
        (application as HyemdoolyApplication).getActivityModule(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityModule.injectFields(this)
    }

    fun <T : Any> createViewModel(modelClass: KClass<T>) =
        hyemdoolyViewModel.viewModelModule.getInstance(modelClass)
}
