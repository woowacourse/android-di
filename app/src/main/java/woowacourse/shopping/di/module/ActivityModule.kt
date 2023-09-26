package woowacourse.shopping.di.module

import android.content.Context
import com.example.bbottodi.di.Module
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.model.repository.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ActivityModule(private val context: Context) : Module {
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
