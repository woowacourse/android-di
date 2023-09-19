package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModel

open class ViewModelModule(
    val applicationContext: Context,
) : Module {
    fun <VM : ViewModel> createViewModel(modelClass: Class<VM>): VM {
        return Injector(this).inject(modelClass)
    }
}
