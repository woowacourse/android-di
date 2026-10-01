package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> viewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer<VM> {
            val smileDi = (this[APPLICATION_KEY] as DiApplication).smileDi
            smileDi.createDependency(VM::class)
        }
    }
