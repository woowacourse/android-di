package woowacourse.shopping.di

import androidx.appcompat.app.AppCompatActivity

open class DiActivity : AppCompatActivity() {
    val apiModule: ApiModule = ApiModule

    inline fun <reified T : Any> createInstance(clazz: Class<T>): T {
        return apiModule.createInstance(clazz)
    }
}
