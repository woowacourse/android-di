package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DIContainer

class MainViewModelProvider(private val diContainer: DIContainer) {
    fun factory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    create()
                } else {
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }

    private fun<T> create(): T {
        val instance = diContainer.get(MainViewModel::class, listOf(DefaultProductRepository(), DefaultCartRepository()))
        return instance as T
    }
}
