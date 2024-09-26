package com.example.yennydi.fixture

import android.os.Bundle
import com.example.yennydi.activity.DiActivity
import com.example.yennydi.application.DiApplication
import com.example.yennydi.di.DependencyProvider
import com.example.yennydi.di.Injected
import com.example.yennydi.viewmodel.DiViewModel
import com.example.yennydi.viewmodel.injectedViewModels

class FakeApplication : DiApplication() {
    override val dependencyProvider: DependencyProvider = FakeApplicationDependencyProvider()

    override fun onCreate() {
        super.onCreate()
    }
}

class FakeActivity : DiActivity() {
    val fakeViewModel by injectedViewModels<FakeViewModel>(FakeViewModelDependencyProvider())

    override val dependencyProvider: DependencyProvider = FakeActivityDependencyProvider()

    @Injected
    lateinit var fieldInjected: ActivityScopeComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fieldInjected
    }
}

class ActivityScopeComponent()

class ViewModelScopeComponent()

class FakeViewModel(
    @Injected val fakeRepository: ApplicationScopeComponent,
    @Injected val viewModelScopeComponent: ViewModelScopeComponent,
) : DiViewModel() {
    public override fun onCleared() {
        super.onCleared()
    }
}

interface ApplicationScopeComponent

class ApplicationScopeComponentImpl() : ApplicationScopeComponent
