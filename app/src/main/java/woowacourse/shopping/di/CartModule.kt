package woowacourse.shopping.di

import com.kmlibs.supplin.annotations.Abstract
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.annotations.Within
import com.kmlibs.supplin.model.Scope
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DBCartRepository
import woowacourse.shopping.data.InMemoryCartRepository

@Module
@Within(Scope.Application::class)
interface CartModule {
    @Abstract
    fun provideInMemoryCartRepository(impl: InMemoryCartRepository): CartRepository

    @Abstract
    fun provideDatabaseCartRepository(impl: DBCartRepository): CartRepository
}
