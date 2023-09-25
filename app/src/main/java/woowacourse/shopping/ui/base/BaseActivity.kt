package woowacourse.shopping.ui.base

import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.ActivityInjectManager
import woowacourse.shopping.DefaultActivityInjectManager
import woowacourse.shopping.DefaultViewModelFactoryDelegate
import woowacourse.shopping.ViewModelFactoryDelegate

abstract class BaseActivity :
    AppCompatActivity(),
    ViewModelFactoryDelegate by DefaultViewModelFactoryDelegate(),
    ActivityInjectManager by DefaultActivityInjectManager()
