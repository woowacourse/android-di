package woowacourse.shopping.di.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boogiwoogi.di.DiComponent
import com.boogiwoogi.di.InstanceContainer
import woowacourse.shopping.ShoppingApplication

open class DiActivity : AppCompatActivity(), DiComponent {

    override val instanceContainer: InstanceContainer = ActivityInstanceContainer().apply {
        lifecycle.addObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInjector()
    }

    private fun setupInjector() {
        ShoppingApplication.injector.inject(
            container = instanceContainer,
            modules = ActivityModules(this),
            target = this
        )
    }
}
