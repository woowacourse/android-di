package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity

interface ActivityInjectManager {
    val activity: AppCompatActivity
    fun registerActivity(activity: AppCompatActivity)
    fun addProvider(vararg provider: Any)
    annotation class ActivityLifecycle
}
