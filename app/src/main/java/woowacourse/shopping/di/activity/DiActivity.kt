package woowacourse.shopping.di.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.module.ShoppingApplicationModules

open class DiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInjector()
    }

    private fun setupInjector() {
        with(ShoppingApplication) {
            injector.inject(
                modules = ShoppingApplicationModules(this@DiActivity),
                container = container,
                target = this
            )
        }
    }
}
