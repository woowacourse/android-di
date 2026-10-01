package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.SmileDi

class DiApplication : Application() {
    lateinit var smileDi: SmileDi
        private set

    override fun onCreate() {
        super.onCreate()

        smileDi = SmileDi(AppModule(this), AppContainer())
    }
}
