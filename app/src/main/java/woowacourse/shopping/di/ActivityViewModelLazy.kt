package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import woowacourse.shopping.di.injector.ClassInjector
import woowacourse.shopping.ui.common.viewModelFactory

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModel(): Lazy<VM> {
    return ViewModelLazy(
        VM::class,
        { viewModelStore },
        { viewModelFactory { createViewModel<VM>() } },
    )
}

@MainThread
inline fun <reified VM : ViewModel> createViewModel(): VM {
    return ClassInjector.inject()
}

// class ViewModelLazy<VM : ViewModel>(
//    private val viewModel: VM,
// ) : Lazy<VM> {
//    private var cached: VM? = null
//
//    override val value: VM
//        get() {
//            val cachedViewModel = cached
//            return if (cachedViewModel == null) {
//                ViewModelProvider(
//                    store,
//                    factory,
//                    extrasProducer(),
//                )[viewModelClass.java].also {
//                    cached = it
//                }
//            } else {
//                cachedViewModel
//            }
//        }
//
//    override fun isInitialized(): Boolean = cached != null
// }
