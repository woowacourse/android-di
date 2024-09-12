package woowacourse.shopping

import android.app.Application
import com.bignerdranch.android.haeum2.ModuleInjector
import woowacourse.shopping.di.Module

class DIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ModuleInjector.initializeModuleInjector(this, Module::class)
    }
}
