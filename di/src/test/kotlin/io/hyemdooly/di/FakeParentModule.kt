package io.hyemdooly.di

import io.hyemdooly.di.annotation.Singleton

class FakeParentModule : Module() {
    @Singleton
    fun provideFakeDao(): FakeDao = FakeDaoImpl()

    fun provideFamily(): FakeFamily = FakeFamily()
}
