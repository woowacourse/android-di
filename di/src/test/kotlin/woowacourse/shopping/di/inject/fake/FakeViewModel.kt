package woowacourse.shopping.di.inject.fake

import woowacourse.shopping.di.annotation.CustomInject
import woowacourse.shopping.di.annotation.InMemoryMode
import woowacourse.shopping.repository.CartRepository

class FakeViewModel {

    @CustomInject
    @InMemoryMode
    lateinit var cartRepository: CartRepository

    @CustomInject
    lateinit var datas: List<String>

    lateinit var items: List<Int>
}
