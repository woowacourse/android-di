package woowacourse.shopping

import woowacourse.shopping.di.AppModule
import woowacourse.shopping.hashdi.DiApplication

class ShoppingApplication : DiApplication(listOf(AppModule()))
