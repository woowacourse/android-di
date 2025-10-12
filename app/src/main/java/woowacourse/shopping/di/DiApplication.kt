package woowacourse.shopping.di

import android.app.Application

abstract class DiApplication : Application() {
    lateinit var appContainerStore: AppContainerStore

    fun register(vararg modules: DependencyModule) {
        val factories = mutableListOf<DependencyFactory<*>>()
        modules.forEach { factories.addAll(it.factories) }
        appContainerStore = AppContainerStore(*factories.toTypedArray())
    }
}
