package woowacourse.shopping.di

import android.app.Application
import kotlin.reflect.full.primaryConstructor

open class DIApplication(
    private val applicationModuleClazz: Class<out ApplicationModule>,
) : Application() {
    private lateinit var applicationModule: ApplicationModule

    override fun onCreate() {
        super.onCreate()

        val primaryConstructor =
            applicationModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()

        applicationModule = primaryConstructor.call(this)
        applicationModule.inject(this)
    }
}
