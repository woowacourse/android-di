package woowacourse.shopping.di

import android.app.Application

abstract class DIApplication : Application() {

    lateinit var repositoryContainer: RepositoryContainer
        protected set

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    abstract fun inject()
}
