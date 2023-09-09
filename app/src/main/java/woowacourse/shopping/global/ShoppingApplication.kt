package woowacourse.shopping.global

import woowacourse.shopping.di.application.DiApplication
import woowacourse.shopping.di.module.DefaultApplicationModule

class ShoppingApplication :
    DiApplication(DefaultApplicationModule::class.java)
