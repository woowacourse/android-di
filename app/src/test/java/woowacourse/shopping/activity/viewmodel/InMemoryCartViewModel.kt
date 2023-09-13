package woowacourse.shopping.activity.viewmodel

import androidx.lifecycle.ViewModel
import io.hyemdooly.di.annotation.Qualifier
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCartRepository

class InMemoryCartViewModel constructor(
    @Qualifier(InMemoryCartRepository::class) private val cartRepository: CartRepository,
) : ViewModel()
