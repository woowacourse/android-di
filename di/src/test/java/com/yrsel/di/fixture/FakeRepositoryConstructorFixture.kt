package com.yrsel.di.fixture

import com.yrsel.di.annotation.Inject

data class FakeRepositoryConstructorFixture
    @Inject
    constructor(
        private val dataSource: FakeDataSource,
    ) : FakeRepository
