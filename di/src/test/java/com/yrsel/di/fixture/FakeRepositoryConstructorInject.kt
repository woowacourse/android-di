package com.yrsel.di.fixture

import com.yrsel.di.annotation.Inject

data class FakeRepositoryConstructorInject
    @Inject
    constructor(
        private val dataSource: FakeDataSource,
    ) : FakeRepository
