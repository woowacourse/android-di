package com.zzang.di.testfixture

import com.zzang.di.DIContainer
import com.zzang.di.annotation.QualifierType
import com.zzang.di.module.DIModule

class FakeModule : DIModule {
    override fun register(container: DIContainer) {
        container.registerModuleInstance(
            type = TestRepository::class,
            instance = FakeDatabaseRepository(),
            qualifier = QualifierType.DATABASE,
        )

        container.registerModuleInstance(
            type = TestRepository::class,
            instance = FakeInMemoryRepository(),
            qualifier = QualifierType.IN_MEMORY,
        )
    }
}
