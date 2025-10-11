package woowacourse.shopping.ui.util

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.diViewModels() = viewModels<VM> { DIViewModelFactory }
