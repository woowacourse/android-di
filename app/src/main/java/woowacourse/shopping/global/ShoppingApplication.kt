package woowacourse.shopping.global

import com.example.di.application.DiApplication
import woowacourse.shopping.di.module.DefaultApplicationModule

class ShoppingApplication : DiApplication(DefaultApplicationModule::class.java)
