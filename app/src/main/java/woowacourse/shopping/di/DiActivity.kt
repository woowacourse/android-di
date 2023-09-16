package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class DiActivity : AppCompatActivity() {

    private val diInjector: ActivityDiInjector by lazy { ActivityDiInjector(this.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInjector()
    }

    private fun setupInjector() {
        diInjector.inject(this)
        lifecycle.addObserver(diInjector.lifeCycleInstances)
    }
}
