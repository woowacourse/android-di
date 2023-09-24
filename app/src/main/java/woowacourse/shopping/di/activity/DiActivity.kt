package woowacourse.shopping.di.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.boogiwoogi.di.InstanceContainer
import woowacourse.shopping.di.DiApplication
import woowacourse.shopping.di.module.ShoppingActivityModule

open class DiActivity : AppCompatActivity() {

    val instanceContainer: InstanceContainer = ActivityInstanceContainer()
    val module by lazy { ShoppingActivityModule(this@DiActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInjector()
    }

    override fun onPause() {
        if (isFinishing) {
            instanceContainer.clear()
        }
        super.onPause()
    }

    private fun setupInjector() {
        DiApplication.injector.injectMemberProperty(
            activityInstanceContainer = instanceContainer,
            activityModule = module,
            target = this
        )
    }
}
