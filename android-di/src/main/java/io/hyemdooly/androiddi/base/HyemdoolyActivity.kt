package io.hyemdooly.androiddi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

abstract class HyemdoolyActivity : AppCompatActivity() {
    private val hyemdoolyViewModel: HyemdoolyViewModel by lazy {
        (application as HyemdoolyApplication).getHyemdoolyViewModel(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hyemdoolyViewModel.activityModule.injectFields(this)
    }

    fun <T : Any> createViewModel(modelClass: KClass<T>) = hyemdoolyViewModel.viewModelModule.getInstance(modelClass)
}
