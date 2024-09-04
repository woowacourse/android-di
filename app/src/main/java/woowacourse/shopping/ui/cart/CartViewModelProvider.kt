package woowacourse.shopping.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.di.DIContainer

class CartViewModelProvider(private val diContainer: DIContainer) {
    fun factory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                    create()
                } else {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

    private fun<T> create(): T {
        val instance = diContainer.get(CartViewModel::class, listOf(DefaultCartRepository()))
        return instance as T
    }
}
