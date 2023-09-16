package com.mission.androiddi.util.viewModel

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import com.mission.androiddi.component.viewModel.ViewModelDependencyInjector
import com.woowacourse.bunadi.cache.Cache
import com.woowacourse.bunadi.cache.DefaultCache

inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory { createViewModel<VM>(this) } },
    )
}

inline fun <reified VM : ViewModel> createViewModel(
    activity: Activity,
): VM {
    val parentCache = activity.application
    if (parentCache is Cache) {
        return ViewModelDependencyInjector(DefaultCache(parentCache)).inject(VM::class)
    }
    return ViewModelDependencyInjector().inject(VM::class)
}
