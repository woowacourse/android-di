package woowacourse.shopping.di

import com.daedan.di.DiApplication
import com.daedan.di.module
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

fun DiApplication.repositoryModule() =
    module {
        single<CartRepository>(annotated<RoomDBCartRepository>()) {
            DefaultCartRepository(
                get(),
            )
        }
        single<ProductRepository>(named("productRepository")) {
            DefaultProductRepository()
        }
    }
