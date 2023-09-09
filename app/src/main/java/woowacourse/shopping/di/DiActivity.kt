package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass

open class DiActivity : AppCompatActivity() {
    private lateinit var diContainer: DiContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        diContainer = DiActivityModule(
            (application as? DiApplication)?.diContainer
                ?: throw IllegalStateException(ERROR_MESSAGE_NO_DI_APPLICATION),
            this,
        )

        diContainer.inject(this)
    }

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        return diContainer.createInstance(clazz)
    }

    companion object {
        const val ERROR_MESSAGE_NO_DI_APPLICATION = "DiApplication이 아닙니다."
    }
}
