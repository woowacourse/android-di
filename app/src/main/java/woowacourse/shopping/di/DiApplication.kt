package woowacourse.shopping.di

import android.app.Application

open class DiApplication : Application() {
    private val apiModule = ApiModule()

    fun <T> createInstance(clazz: Class<T>): T {
        return apiModule.createInstance(clazz)
    }
}
