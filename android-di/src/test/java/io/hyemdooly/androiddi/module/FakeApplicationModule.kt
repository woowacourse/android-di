package io.hyemdooly.androiddi.module

import android.content.Context
import io.hyemdooly.androiddi.element.FakeDatabase
import io.hyemdooly.androiddi.element.FakeRepositoryInApplication
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Singleton

class FakeApplicationModule(private val applicationContext: Context) : Module() {
    @Singleton
    fun provideContext() = applicationContext

    @Singleton
    fun provideDatabase() = FakeDatabase()

    @Singleton
    fun provideRepository(db: FakeDatabase) = FakeRepositoryInApplication(db)
}
