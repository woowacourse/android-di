package woowacourse.shopping.ui.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity :
    AppCompatActivity(),
    ViewModelFactoryDelegate by DefaultViewModelFactoryDelegate()
