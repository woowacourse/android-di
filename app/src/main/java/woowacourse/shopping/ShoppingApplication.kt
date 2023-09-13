package woowacourse.shopping

import android.app.Application
import com.ki960213.sheath.SheathApplication

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SheathApplication.run(applicationContext)
    }
}
