package woowacourse.shopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.ShoppingApplication
import java.util.UUID

abstract class BaseScopedActivity : AppCompatActivity() {
    private val app by lazy { application as ShoppingApplication }

    private lateinit var scopeName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeName = savedInstanceState?.getString(KEY_SCOPE_NAME)
            ?: "${this::class.java.name}_${UUID.randomUUID()}"

        app.appContainer.createScope(scopeName)
        app.dependencyInjector.injectFields(this, this::class, scopeName)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SCOPE_NAME, scopeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            app.appContainer.clearScope(scopeName)
        }
    }

    companion object {
        private const val KEY_SCOPE_NAME = "KEY_SCOPE_NAME"
    }
}
