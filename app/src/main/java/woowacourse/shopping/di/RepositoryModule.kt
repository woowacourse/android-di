package woowacourse.shopping.di

import com.on.di_library.di.MySingleTon
import com.on.di_library.di.ViewmodelScope
import com.on.di_library.di.annotation.MyModule
import com.on.di_library.di.annotation.MyProvider
import com.on.di_library.di.annotation.MyQualifier
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

@MyModule
object RepositoryModule {
    @MyProvider
    @MySingleTon
    @MyQualifier("default")
    fun defaultCartRepository(dao: CartProductDao): CartRepository = DefaultCartRepository(dao)

    @MyProvider
    @MySingleTon
    @MyQualifier("inMemory")
    fun inMemoryCartRepository(): CartRepository = InMemoryCartRepository()

    @MyProvider
    @ViewmodelScope
    fun productRepository(): ProductRepository = DefaultProductRepository()
}
