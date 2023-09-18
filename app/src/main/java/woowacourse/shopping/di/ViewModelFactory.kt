package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.now.androdi.activity.ActivityInjectable

class ViewModelFactory(private val activity: ActivityInjectable) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return activity.injector.inject(modelClass.kotlin)
    }
}
