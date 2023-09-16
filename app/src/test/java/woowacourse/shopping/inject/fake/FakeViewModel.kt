package woowacourse.shopping.inject.fake

import com.lope.di.annotation.CustomInject
import com.lope.di.annotation.InMemoryMode
import woowacourse.shopping.repository.CartRepository

class FakeViewModel {

    @CustomInject
    @InMemoryMode
    lateinit var cartRepository: CartRepository

    @CustomInject
    lateinit var datas: List<String>

    lateinit var items: List<Int>
}
