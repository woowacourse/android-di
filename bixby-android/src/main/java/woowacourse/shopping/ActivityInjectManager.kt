package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity

interface ActivityInjectManager {
    val activity: AppCompatActivity
    fun registerActivity(activity: AppCompatActivity)
    annotation class ActivityLifecycle
}
