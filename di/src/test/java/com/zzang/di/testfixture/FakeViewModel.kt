package com.zzang.di.testfixture

import androidx.lifecycle.ViewModel
import com.zzang.di.DIContainer
import com.zzang.di.DependencyInjector
import com.zzang.di.annotation.Inject
import com.zzang.di.annotation.QualifierType

class FakeViewModel : ViewModel() {
    @Inject(qualifier = QualifierType.IN_MEMORY)
    lateinit var inMemoryRepository: TestRepository

    @Inject(qualifier = QualifierType.DATABASE)
    lateinit var databaseRepository: TestRepository

    init {
        injectDependencies()
    }

    private fun injectDependencies() {
        DependencyInjector.injectDependencies(this, this)
    }

    public override fun onCleared() {
        super.onCleared()
        DIContainer.clearViewModelScopedInstances(this)
    }
}
