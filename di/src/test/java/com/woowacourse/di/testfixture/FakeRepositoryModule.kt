package com.woowacourse.di.testfixture

import com.woowacourse.di.Module

@Module
class FakeRepositoryModule {
    fun provideFakeProductRepository(): FakeProductRepository = FakeProductRepositoryImpl()
}
