package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import java.lang.reflect.Field

abstract class DIActivity : AppCompatActivity() {

    private val repositoryContainer by lazy {
        (application as ShoppingApplication).repositoryContainer
    }

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                    productRepository = repositoryContainer.productRepository,
                    cartRepository = repositoryContainer.cartRepository,
                )

                modelClass.isAssignableFrom(CartViewModel::class.java) -> CartViewModel(
                    cartRepository = repositoryContainer.cartRepository,
                )

                else -> throw IllegalArgumentException("ViewModel 타입 오류 : ${modelClass.name}")
            } as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModelField =
            this::class.java.declaredFields.first { ViewModel::class.java.isAssignableFrom(it.type) }
        viewModelField.isAccessible = true
        viewModelField.set(this, getNewViewModelByType(viewModelField))

        super.onCreate(savedInstanceState)
    }

    private fun getNewViewModelByType(field: Field) = when (field.type) {
        MainViewModel::class.java -> getViewModel<MainViewModel>()
        CartViewModel::class.java -> getViewModel<CartViewModel>()
        else -> throw IllegalArgumentException("ViewModel 타입 오류 : ${field.type.name}")
    }

    private inline fun <reified T : ViewModel> getViewModel(): T {
        return ViewModelProvider(this@DIActivity, viewModelFactory)[T::class.java]
    }
}
