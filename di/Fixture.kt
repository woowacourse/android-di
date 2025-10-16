package woowacourse

import androidx.lifecycle.ViewModel
import com.example.di.Inject
import com.example.domain.repository.CartRepository
import com.example.domain.repository.ProductRepository

private class TestMainVm : ViewModel() {
    @field:Inject
    lateinit var productRepository: ProductRepository
    var title: String = "Main"
}

private class TestBothVm : ViewModel() {
    @field:Inject
    lateinit var productRepository: ProductRepository

    @field:Inject
    lateinit var cartRepository: CartRepository
}