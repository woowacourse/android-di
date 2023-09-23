package woowacourse.shopping.di.module

import android.content.Context
import com.ssu.di.module.Module
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.ProductRepository

class MainActivityModule(private val context: Context) : Module {
    fun provideProductRepository(): ProductRepository = DefaultProductRepository()
}
