package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.full.primaryConstructor

open class DIActivity(
    private val activityModuleClazz: Class<out ActivityModule>,
) : AppCompatActivity() {
    private lateinit var activityModule: ActivityModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val primaryConstructor =
            activityModuleClazz.kotlin.primaryConstructor ?: throw NullPointerException()

        activityModule = primaryConstructor.call(this)
        activityModule.inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
