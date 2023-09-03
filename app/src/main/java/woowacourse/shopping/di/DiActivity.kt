package woowacourse.shopping.di

import androidx.appcompat.app.AppCompatActivity

open class DiActivity : AppCompatActivity() {
    private val diApplication: DiApplication
        get() = application as? DiApplication
            ?: throw IllegalStateException(ERROR_MESSAGE_NO_DI_APPLICATION)

    fun <T> createInstance(clazz: Class<T>): T {
        return diApplication.createInstance(clazz)
    }

    companion object {
        const val ERROR_MESSAGE_NO_DI_APPLICATION = "DiApplication이 아닙니다."
    }
}
