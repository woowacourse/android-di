package woowacourse.shopping.di.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.ShoppingApplication

open class DiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInjector()
    }

    private fun setupInjector() {
        ShoppingApplication.injector.inject(
            container = ActivityDiContainer().apply {
                lifecycle.addObserver(this)
            },
            modules = ActivityModule(this),
            target = this
        )
    }
}
