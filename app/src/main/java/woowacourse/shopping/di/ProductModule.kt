package woowacourse.shopping.di

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Concrete
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import woowacourse.shopping.data.InMemoryProductRepositoryImpl
import woowacourse.shopping.data.ProductRepository

@Module
@Within(Scope.ViewModel::class)
interface ProductModule {
    @Abstract
    fun provideProductRepository(impl: InMemoryProductRepositoryImpl): ProductRepository
}
