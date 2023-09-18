package io.hyemdooly.androiddi.module

import io.hyemdooly.androiddi.element.FakeRepositoryInViewModel
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Singleton

class FakeViewModelModule(parentModule: Module) : Module(parentModule) {
    @Singleton
    fun provideRepository() = FakeRepositoryInViewModel()
}
