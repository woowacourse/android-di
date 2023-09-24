package io.hyemdooly.androiddi

import android.content.Context
import androidx.lifecycle.ViewModel
import io.hyemdooly.androiddi.base.HyemdoolyActivity
import io.hyemdooly.androiddi.base.HyemdoolyApplication
import io.hyemdooly.androiddi.module.Modules
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Inject
import io.hyemdooly.di.annotation.Qualifier
import io.hyemdooly.di.annotation.Singleton

class FakeDatabase
class FakeFormatterInActivity
interface FakeRepository
class FakeRepositoryInApplication(db: FakeDatabase) : FakeRepository
class FakeRepositoryInViewModel : FakeRepository

class FakeActivity : HyemdoolyActivity() {
    @Inject
    lateinit var formatter: FakeFormatterInActivity
}

class FakeApplication : HyemdoolyApplication(object : Modules {
    override val applicationModule: (Context) -> Module
        get() = { context -> FakeApplicationModule(context) }
    override val activityModule: (Module, Context) -> Module
        get() = { module, context -> FakeActivityModule(module, context) }
    override val viewModelModule: (Module) -> Module
        get() = { module -> FakeViewModelModule(module) }
}) {
    @Inject
    lateinit var database: FakeDatabase

    @Inject
    lateinit var repository: FakeRepositoryInApplication
}

class FakeViewModel(@Qualifier(FakeRepositoryInViewModel::class) repository: FakeRepository) :
    ViewModel()

class FakeApplicationModule(private val applicationContext: Context) : Module() {
    @Singleton
    fun provideContext() = applicationContext

    @Singleton
    fun provideDatabase() = FakeDatabase()

    @Singleton
    fun provideRepository(db: FakeDatabase) = FakeRepositoryInApplication(db)
}

class FakeActivityModule(parentModule: Module, private val activityContext: Context) :
    Module(parentModule) {
    @Singleton
    fun provideContext() = activityContext

    @Singleton
    fun provideFormatter() = FakeFormatterInActivity()
}

class FakeViewModelModule(parentModule: Module) : Module(parentModule) {
    @Singleton
    fun provideRepository() = FakeRepositoryInViewModel()
}
