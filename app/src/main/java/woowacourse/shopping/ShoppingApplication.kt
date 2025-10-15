package woowacourse.shopping

import android.app.Application
import androidx.annotation.VisibleForTesting
import woowacourse.bibi.di.core.AppContainer
import woowacourse.bibi.di.core.ContainerBuilder
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
