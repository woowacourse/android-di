package woowacourse.shopping.data.di

import androidx.lifecycle.ViewModel

class FakeFieldInjectViewModel : ViewModel() {
    @Inject
    lateinit var productRepository: FakeRepository
    lateinit var cartRepository: FakeRepository
}