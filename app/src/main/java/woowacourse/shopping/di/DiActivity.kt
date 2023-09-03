package woowacourse.shopping.di

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

open class DiActivity : AppCompatActivity() {
    private val diContainer: DiContainer by lazy {
        DiActivityModule(
            (application as? DiApplication)?.diContainer
                ?: throw IllegalStateException(ERROR_MESSAGE_NO_DI_APPLICATION)
        )
    }

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        return diContainer.createInstance(clazz)
    }

    companion object {
        const val ERROR_MESSAGE_NO_DI_APPLICATION = "DiApplication이 아닙니다."
    }
}
