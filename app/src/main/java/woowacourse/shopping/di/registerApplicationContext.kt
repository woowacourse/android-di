package woowacourse.shopping.di

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.example.di.Component
import com.example.di.DiContainer
import com.example.di.Injector
import woowacourse.shopping.di.annotation.ApplicationContext

fun registerApplicationContext(appContext: Context) {
    DiContainer.bindProvides(
        type = Context::class,
        installIn = { Component.Singleton },
        isScoped = true,
        qualifier = ApplicationContext::class,
    ) { appContext }
}

fun ComponentActivity.openActivityComponent() = DiContainer.openActivityComponent(this)

fun ComponentActivity.closeActivityComponent() = DiContainer.closeActivityComponent(this)

fun ViewModel.openViewModelComponent() = DiContainer.openViewModelComponent(this)

fun ViewModel.closeViewModelComponent() = DiContainer.closeViewModelComponent(this)

fun ViewModel.injectViewModel() = Injector.inject(this, Component.ViewModel(this))
