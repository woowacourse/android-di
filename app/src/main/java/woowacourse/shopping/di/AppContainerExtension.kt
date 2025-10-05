package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.ShoppingApplication

val Context.appContainer: AppContainer
    get() = (applicationContext as ShoppingApplication).container
