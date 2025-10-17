package com.daedan.di.util

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModelProvider
import com.daedan.di.DiApplication
import com.daedan.di.scope.SavedHandleViewModel
import com.daedan.di.scope.Scope
import com.daedan.di.scope.TypeScope
import com.daedan.di.scope.UniqueScope

@MainThread
fun ComponentActivity.activityRetainedScope(scope: Scope = TypeScope(this::class)): Lazy<UniqueScope> =
    lazy {
        // 스코프 홀더 전용 ViewModel에 저장
        val viewModel = ViewModelProvider(this)[SavedHandleViewModel::class.java]
        val store = (application as DiApplication).appContainerStore
        if (viewModel.scope == null) {
            val uniqueScope =
                UniqueScope(scope)
            store.createScope(uniqueScope)
            registerCurrentContext(store, uniqueScope)
            viewModel.scope = uniqueScope
            viewModel.addCloseable { store.closeScope(uniqueScope) }
        }
        viewModel.scope!!
    }
