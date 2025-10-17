package woowacourse.shopping.fake

import android.app.Application

class FakeApplication : Application() {
    val appContainer: FakeAppContainer = FakeAppContainer()
}
