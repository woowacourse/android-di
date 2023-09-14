package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.re4rk.arkdi.DiContainer
import kotlin.reflect.KClass

open class DiAppCompatActivity : AppCompatActivity() {
    private val diContainer: DiContainer by lazy {
        (application as? DiApplication)?.getActivityDiContainer(this)
            ?: throw IllegalStateException("Application should be subclass of DiApplication.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diContainer.inject(this)
    }

    fun <T : Any> createInstance(clazz: KClass<T>) = diContainer.createInstance(clazz)
}
