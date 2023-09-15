package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.re4rk.arkdi.DiContainer

class DiViewModel : ViewModel() {
    var ownerRetainedDiContainer: DiContainer? = null
    var ownerDiContainer: DiContainer? = null
    var viewModelDiContainer: DiContainer? = null
}
