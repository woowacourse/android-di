package com.zzang.di.testfixture

import androidx.lifecycle.ViewModel
import com.zzang.di.DIContainer
import com.zzang.di.annotation.Inject
import com.zzang.di.annotation.QualifierType

class FakeViewModel : ViewModel() {
    @Inject(qualifier = QualifierType.IN_MEMORY)
    lateinit var fakeInMemoryRepository: TestRepository

    public override fun onCleared() {
        super.onCleared()
        DIContainer.clearViewModelScopedInstances(this)
    }
}
