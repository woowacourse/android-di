package io.hyemdooly.androiddi.component

import android.content.Context
import io.hyemdooly.androiddi.base.HyemdoolyApplication
import io.hyemdooly.androiddi.element.FakeDatabase
import io.hyemdooly.androiddi.element.FakeRepositoryInApplication
import io.hyemdooly.androiddi.module.FakeActivityModule
import io.hyemdooly.androiddi.module.FakeApplicationModule
import io.hyemdooly.androiddi.module.FakeViewModelModule
import io.hyemdooly.androiddi.module.Modules
import io.hyemdooly.di.Module
import io.hyemdooly.di.annotation.Inject

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
