package woowacourse.shopping.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.annotation.ActivityContext
import woowacourse.shopping.module.DIActivityModule
import woowacourse.shopping.module.DIActivityRetainedModule
import woowacourse.shopping.util.getParcelableCompat

open class DIAppCompatActivity : AppCompatActivity() {
    private lateinit var activityRetainedModule: DIActivityRetainedModule
    private lateinit var activityModule: DIActivityModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val diApplication = application as DIApplication
        activityRetainedModule = savedInstanceState?.getParcelableCompat(
            KEY_ACTIVITY_RETAINED_MODULE,
        ) ?: diApplication.getActivityRetainedModule()
        activityModule = diApplication.getActivityModule(activityRetainedModule)

        // activity context 저장
        val key = (Context::class to ActivityContext())
        activityModule.addInstance(key, this)

        // activity 필드 주입
        activityModule.injectFields(this::class, this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!isFinishing) {
            outState.putParcelable(KEY_ACTIVITY_RETAINED_MODULE, activityRetainedModule)
        }
    }

    companion object {
        private const val KEY_ACTIVITY_RETAINED_MODULE = "activity_retained_module"
    }
}
