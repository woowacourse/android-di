package io.hyemdooly.di

import io.hyemdooly.di.annotation.Singleton

class FakeChildModule(parentModule: Module) : Module(parentModule) {
    @Singleton
    fun provideFakeRepository(dao: FakeDao): FakeRepository = FakeRepositoryImpl(dao)
}
