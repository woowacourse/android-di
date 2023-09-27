package woowacourse.shopping.fake.activity

import android.content.Context
import android.os.Bundle
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.ReleasedActivityLifecycle
import woowacourse.shopping.annotation.RetainedActivityLifecycle
import woowacourse.shopping.fake.viewmodel.FakeCartViewModel
import woowacourse.shopping.fake.viewmodel.FakeMainViewModel
import woowacourse.shopping.ui.DiAppCompatActivity
import woowacourse.shopping.ui.viewModels

const val FAKE_ACTIVITY_NAME = "FakeActivity"

class FakeDateFormatter(context: Context)

class FakeActivity : DiAppCompatActivity() {
    val mainViewModel by viewModels<FakeMainViewModel>()
    val cartViewModel by viewModels<FakeCartViewModel>()

    @Inject
    @RetainedActivityLifecycle(FAKE_ACTIVITY_NAME)
    lateinit var dateFormatter1: FakeDateFormatter

    @Inject
    @ReleasedActivityLifecycle(FAKE_ACTIVITY_NAME)
    lateinit var dateFormatter2: FakeDateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
    }
}

fun createFakeDateFormatter(@ReleasedActivityLifecycle(FAKE_ACTIVITY_NAME) context: Context): FakeDateFormatter =
    FakeDateFormatter(context)
