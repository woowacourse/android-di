package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.ProductDefaultRepository
import kotlin.reflect.KClass

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(ProductDefaultRepository(), CartDefaultRepository()) as T
        } else throw IllegalArgumentException("알 수 없는 ViewModel 생성자입니다.")
    }
}
