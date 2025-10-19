package woowacourse.shopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.ShoppingApplication

abstract class BaseScopedActivity : AppCompatActivity() {
    private val app by lazy { application as ShoppingApplication }
    private val scopeName = "${this::class.java.name}_${this.hashCode()}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.appContainer.createScope(scopeName)
        app.dependencyInjector.injectFields(this, this::class, scopeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        app.appContainer.clearScope(scopeName)
    }
}
