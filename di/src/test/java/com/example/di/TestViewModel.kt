package com.example.di

class TestViewModel(
    @Inject
    @ProvideRemoteTestRepository
    val remoteTestRepository: TestRepository,
) {
    @Inject
    @ProvideLocalTestRepository
    var localTestRepository: TestRepository? = null

    var unInjectedTestRepository: TestRepository? = null
}
