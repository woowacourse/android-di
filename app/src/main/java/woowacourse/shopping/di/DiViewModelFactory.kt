package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harodi.DiManager

class DiViewModelFactory(
    private val diManager: DiManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = diManager.fieldInject(modelClass)
}
