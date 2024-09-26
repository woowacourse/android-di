package com.woowacourse.di.testfixture

import com.woowacourse.di.DiModule

@DiModule
class FakeRepositoryModule {
    fun provideFakeProductRepository(): FakeProductRepository = FakeProductRepositoryImpl()
}
