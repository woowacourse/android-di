package woowacourse.shopping.activity.viewmodel

import androidx.lifecycle.ViewModel
import io.hyemdooly.di.annotation.InDisk
import woowacourse.shopping.data.CartRepository

class InMemoryCartViewModel constructor(
    @InDisk private val cartRepository: CartRepository,
) : ViewModel()
