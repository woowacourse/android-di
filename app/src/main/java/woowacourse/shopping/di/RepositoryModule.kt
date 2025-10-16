package woowacourse.shopping.di

import com.daedan.di.DiApplication
import com.daedan.di.module
import com.daedan.di.util.annotated
import com.daedan.di.util.named
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel

fun DiApplication.repositoryModule() =
    module {
        single<CartRepository>(annotated<RoomDBCartRepository>()) {
            DefaultCartRepository(
                get(),
            )
        }
        scope<MainViewModel> {
            scoped<ProductRepository>(named("productRepository")) {
                DefaultProductRepository()
            }
        }
    }
