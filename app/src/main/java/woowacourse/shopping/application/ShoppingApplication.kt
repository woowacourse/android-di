package woowacourse.shopping.application

import com.bignerdranch.android.koala.DiApplication
import woowacourse.shopping.di.module.ApplicationDiModule

class ShoppingApplication : DiApplication(ApplicationDiModule())
