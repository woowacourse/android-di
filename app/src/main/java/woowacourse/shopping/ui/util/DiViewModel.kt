package woowacourse.shopping.ui.util

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

inline fun <reified VM : ViewModel> ComponentActivity.diViewModel(noinline defaultArgs: () -> Bundle? = { null }): Lazy<VM> =
    ViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = { DIViewModelFactory(this, defaultArgs()) },
        extrasProducer = { this.defaultViewModelCreationExtras },
    )
