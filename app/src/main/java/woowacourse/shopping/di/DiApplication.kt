package woowacourse.shopping.di

import android.app.Application
import kotlin.reflect.KClass

open class DiApplication : Application() {
    private val apiModule = ApiModule()

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        return apiModule.createInstance(clazz)
    }
}
