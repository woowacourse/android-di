package woowacourse.shopping.ui

import android.content.Context
import io.hyemdooly.androiddi.base.HyemdoolyApplication
import io.hyemdooly.androiddi.module.Modules
import io.hyemdooly.di.Module
import woowacourse.shopping.ui.di.ActivityModule
import woowacourse.shopping.ui.di.ApplicationModule
import woowacourse.shopping.ui.di.ViewModelModule

val modules = object : Modules {
    override val applicationModule: (Context) -> Module
        get() = { context -> ApplicationModule(context) }
    override val activityModule: (Module, Context) -> Module
        get() = { module, context -> ActivityModule(module, context) }
    override val viewModelModule: (Module) -> Module
        get() = { module -> ViewModelModule(module) }
}

class ShoppingApplication : HyemdoolyApplication(modules)
