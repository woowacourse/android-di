package io.hyemdooly.di

import io.hyemdooly.di.annotation.Inject
import io.hyemdooly.di.annotation.Singleton

interface FakeDao
class FakeDaoImpl : FakeDao
class FakeFamily()
class FakeName
class FakePerson(val name: FakeName)
interface FakeRepository
class FakeRepositoryImpl(val dao: FakeDao) : FakeRepository
class FakeViewModel(val repository: FakeRepository) {
    @Inject
    lateinit var dao: FakeDao
}

class FakeParentModule : Module() {
    @Singleton
    fun provideFakeDao(): FakeDao = FakeDaoImpl()

    fun provideFamily(): FakeFamily = FakeFamily()
}

class FakeChildModule(parentModule: Module) : Module(parentModule) {
    @Singleton
    fun provideFakeRepository(dao: FakeDao): FakeRepository = FakeRepositoryImpl(dao)
}
