package woowacourse.shopping.ui

import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.DIModule

open class DIAppCompatActivity : AppCompatActivity() {
    private val activityRetainedModule: DIModule by lazy { (application as DIApplication).getActivityRetainedModule() }
    private val activityModule: DIModule by lazy {
        (application as DIApplication).getActivityModule(
            activityRetainedModule,
        )
    }
}
