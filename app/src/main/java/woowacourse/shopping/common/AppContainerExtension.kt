package woowacourse.shopping.common

import android.content.Context
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.di.AppContainer

val Context.appContainer: AppContainer
    get() = (applicationContext as ShoppingApplication).container
