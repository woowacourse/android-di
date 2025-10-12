package woowacourse.shopping

import android.app.Application
import androidx.annotation.VisibleForTesting
import woowacourse.bibi_di.AppContainer
import woowacourse.bibi_di.ContainerBuilder
import woowacourse.shopping.di.installAllBindings

class ShoppingApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val builder = ContainerBuilder()
        installAllBindings(builder, this)
        container = builder.build()
    }

    @VisibleForTesting
    fun overrideContainerForTest(testContainer: AppContainer) {
        container = testContainer
    }
}
