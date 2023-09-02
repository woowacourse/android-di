package woowacourse.shopping.di.container

import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.LocalProductRepository

class RepositoryContainer {
    val postRepository: LocalProductRepository by lazy {
        LocalProductRepository()
    }
    val cartRepository: LocalCartRepository by lazy {
        LocalCartRepository()
    }
}
