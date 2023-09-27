package woowacourse.shopping.di

import com.angrypig.autodi.autoDIModule.autoDIModule
import woowacourse.shopping.ui.cart.DateFormatter

val androidModule = autoDIModule("android") {
    disposable<DateFormatter>("disposable") { DateFormatter(injectApplicationContext()) }
}
