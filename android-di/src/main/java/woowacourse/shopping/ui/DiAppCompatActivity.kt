package woowacourse.shopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.Injector
import woowacourse.shopping.Module
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class DiAppCompatActivity : AppCompatActivity() {
    private val injector: Injector by lazy { (application as DiApplication).injector }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.injectFields(this::class, this)
    }

    fun <T : Any> registerModule(module: KClass<out T>) {
        injector.addModule(module.primaryConstructor?.call(this) as Module)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            releaseDependency(this::class.simpleName.toString())
        }
        releaseDependency(this::class.simpleName.toString())
    }

    private fun releaseDependency(dependency: String) {
        injector.releaseDependency(dependency)
    }

    companion object {
        private const val RETAINED_DEPENDENCY = "Retained%sContainer"
        private const val RELEASED_DEPENDENCY = "Released%sContainer"
    }
}
