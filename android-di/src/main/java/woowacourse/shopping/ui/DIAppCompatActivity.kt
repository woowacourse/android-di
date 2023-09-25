package woowacourse.shopping.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.DIModule
import woowacourse.shopping.annotation.ActivityContext

open class DIAppCompatActivity : AppCompatActivity() {
    private val activityRetainedModule: DIModule by lazy { (application as DIApplication).getActivityRetainedModule() }
    private val activityModule: DIModule by lazy {
        (application as DIApplication).getActivityModule(
            activityRetainedModule,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // activity context 저장
        val key = (Context::class to ActivityContext())
        activityModule.addInstance(key, this)
    }
}
