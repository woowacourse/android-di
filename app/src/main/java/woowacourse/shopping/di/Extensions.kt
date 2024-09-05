package woowacourse.shopping.di

import androidx.lifecycle.ViewModel

inline fun <reified VM : ViewModel> injectViewModel(): VM {
    return DIContainer.inject()
}
