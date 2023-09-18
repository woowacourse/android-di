package woowacourse.shopping.hashdi

import android.app.Application

open class DiApplication(private val modules: List<Module> = listOf()) : Application() {

    lateinit var injector: Injector

    override fun onCreate() {
        super.onCreate()
        injector = Injector(AppContainer(modules, this))
    }
}
