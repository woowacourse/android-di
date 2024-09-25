package woowacourse.shopping.di

import android.content.Context
import com.example.yennydi.di.DependencyContainer
import com.example.yennydi.di.DependencyProvider
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingActivityModule(private val context: Context) : DependencyProvider {
    override fun register(container: DependencyContainer) {
        addDateFormatter(container)
    }

    private fun addDateFormatter(container: DependencyContainer) {
        container.addInstance(DateFormatter::class, DateFormatter(context))
    }
}
