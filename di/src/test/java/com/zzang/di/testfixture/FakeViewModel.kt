package com.zzang.di.testfixture

import com.zzang.di.annotation.Inject
import com.zzang.di.annotation.QualifierType
import com.zzang.di.base.DIViewModel

class FakeViewModel : DIViewModel() {
    @Inject(qualifier = QualifierType.IN_MEMORY)
    lateinit var fakeInMemoryRepository: TestRepository

    @Inject(qualifier = QualifierType.DATABASE)
    lateinit var fakeDatabaseRepository: TestRepository
}
