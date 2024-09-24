package com.example.yennydi.fixture

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.yennydi.activity.DiActivity
import com.example.yennydi.application.DiApplication
import com.example.yennydi.di.DependencyProvider
import com.example.yennydi.di.Injected
import com.example.yennydi.viewmodel.injectedViewModels

class FakeApplication : DiApplication() {
    override val dependencyProvider: DependencyProvider = FakeApplicationDependencyProvider()

    override fun onCreate() {
        super.onCreate()
    }
}

class FakeActivity : DiActivity() {
    val fakeViewModel by injectedViewModels<FakeViewModel>()

    override val dependencyProvider: DependencyProvider = FakeDependencyProvider()

    @Injected
    lateinit var fieldInjected: FakeInjectedComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fieldInjected
    }
}

class FakeInjectedComponent()

class FakeViewModel(
    @Injected val fakeRepository: FakeRepository,
) : ViewModel()

interface FakeRepository

class FakeRepositoryImpl() : FakeRepository
