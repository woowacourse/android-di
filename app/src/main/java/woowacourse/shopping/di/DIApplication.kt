package woowacourse.shopping.di

import android.app.Application

abstract class DIApplication : Application() {

    val container: Container = Container()

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    abstract fun inject()
}
