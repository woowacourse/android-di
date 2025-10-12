package woowacourse.shopping.app.di

import android.content.Context
import woowacourse.shopping.app.ShoppingApplication
import woowacourse.shopping.di.Container

val Context.appContainer: Container
    get() = (applicationContext as ShoppingApplication).container
