package woowacourse.shopping.data.di

import android.content.Context
import com.woowacourse.di.Module
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(private val context: Context) : Module {
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
