package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.re4rk.arkdi.DiContainer

class DiViewModel : ViewModel() {
    lateinit var ownerRetainedDiContainer: DiContainer
    lateinit var ownerDiContainer: DiContainer
    lateinit var viewModelDiContainer: DiContainer

    val isInitialized
        get() = ::ownerRetainedDiContainer.isInitialized &&
            ::ownerDiContainer.isInitialized &&
            ::viewModelDiContainer.isInitialized
}
