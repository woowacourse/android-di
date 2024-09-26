package com.woowacourse.di

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> DiActivity.viewModels(
    diModule: DiModule,
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        inject(this, diModule)
    },
): Lazy<VM> {
    return this.viewModels(
        extrasProducer = extrasProducer,
        factoryProducer = factoryProducer,
    )
}

fun inject(
    diActivity: DiActivity,
    diModule: DiModule,
): ViewModelProvider.Factory = ViewModelComponent(diActivity, diModule)

class ViewModelComponent(
    private val diActivity: DiActivity,
    private val diModule: DiModule,
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val diInjector = diActivity.diInjector
        diActivity.diInjector.addModule(diModule)

        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException()

        val parameters =
            constructor.parameters.map { parameter ->
                diInjector.diContainer.match(parameter.type.kotlin)
            }.toTypedArray()

        val viewModel = constructor.newInstance(*parameters) as T
        diInjector.diContainer.injectFields(viewModel)

        return viewModel
    }
}
