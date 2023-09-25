package woowacourse.shopping.di.module

import android.content.Context
import com.boogiwoogi.di.DefaultModule
import com.boogiwoogi.di.Provides
import com.boogiwoogi.di.Qualifier
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingActivityModule(private val context: Context) : DefaultModule() {

    @Qualifier("ActivityContext")
    @Provides
    fun provideActivityContext(): Context {
        return context
    }

    @Provides
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    @Provides
    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }
}
