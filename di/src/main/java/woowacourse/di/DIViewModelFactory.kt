package woowacourse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DIViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DIFactory.create(modelClass.kotlin)
    }
}
