package woowacourse.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.di.module.AndroidModule

abstract class DIActivity(private val module: AndroidModule) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        module.setModuleContext(this)
        val container = Container(module)

        injectProperties(container)
    }
}
