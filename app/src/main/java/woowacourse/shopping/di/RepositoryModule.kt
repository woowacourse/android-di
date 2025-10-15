package woowacourse.shopping.di

import com.daedan.compactAndroidDi.DiApplication
import com.daedan.compactAndroidDi.module
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

fun DiApplication.repositoryModule() =
    module {
        factory<CartRepository>(annotated<RoomDBCartRepository>()) {
            DefaultCartRepository(
                get(),
            )
        }
        factory<ProductRepository>(named("productRepository")) {
            DefaultProductRepository()
        }
    }
