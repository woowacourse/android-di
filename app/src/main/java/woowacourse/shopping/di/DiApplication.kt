package woowacourse.shopping.di

import android.app.Application

open class DiApplication : Application() {
    var diContainer: DiContainer = DiApplicationModule()
}
