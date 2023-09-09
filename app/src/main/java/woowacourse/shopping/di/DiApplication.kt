package woowacourse.shopping.di

import android.app.Application

open class DiApplication : Application() {
    lateinit var diContainer: DiContainer

    override fun onCreate() {
        super.onCreate()

        diContainer = DiApplicationModule(applicationContext)
    }
}
