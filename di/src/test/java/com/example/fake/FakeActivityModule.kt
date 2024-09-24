package com.example.fake

import com.example.di.DIModule
import com.example.di.annotation.LifeCycle
import com.example.di.annotation.LifeCycleScope

class FakeActivityModule : DIModule {
    @LifeCycle(LifeCycleScope.ACTIVITY)
    fun provideFakeFieldRepository(): FakeFieldRepository {
        return FakeFieldRepository()
    }

    @LifeCycle(LifeCycleScope.VIEW_MODEL)
    fun provideFakeProductRepository(): FakeProductRepository {
        return FakeProductRepository()
    }
}
