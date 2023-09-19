package woowacourse.shopping.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.Injector

open class DiAppCompatActivity : AppCompatActivity() {
    private val injector: Injector by lazy { (application as DiApplication).injector }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.addDependency(
            dependency = RELEASED_DEPENDENCY.format(this::class.simpleName),
            clazz = Context::class,
            instance = this,
        )
        injector.injectFields(this::class, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            releaseDependency(RETAINED_DEPENDENCY.format(this::class.simpleName))
        }
        releaseDependency(RELEASED_DEPENDENCY.format(this::class.simpleName))
    }

    private fun releaseDependency(dependency: String) {
        injector.releaseDependency(dependency)
    }

    companion object {
        private const val RETAINED_DEPENDENCY = "Retained%sContainer"
        private const val RELEASED_DEPENDENCY = "Released%sContainer"
    }
}
