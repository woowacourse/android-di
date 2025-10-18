package woowacourse.shopping.android.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            owner = this,
            factory =
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        AndroidContainer
                            .ofActivity(this@viewModel)
                            .instance(modelClass.kotlin)
                },
        )[VM::class.java]
    }
