package woowacourse.shopping

import android.app.Application
import com.ki960213.sheath.SheathApplication1

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SheathApplication1.run(applicationContext)
    }
}
