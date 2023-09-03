package woowacourse.shopping.di

import android.app.Application

open class DiApplication : Application() {
    val apiModule = ApiModule()

    inline fun <reified T : Any> createInstance(clazz: Class<T>): T {
        return apiModule.createInstance(clazz)
    }
}
