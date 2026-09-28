package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object ViewModelFactory : ViewModelProvider.Factory {
    private val injector = Injector()

    override fun <T : ViewModel> create(modelClass: Class<T>): T = injector.create(modelClass.kotlin)
}
