package woowacourse.shopping.util.autoDI

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.util.autoDI.dependencyContainer.AutoDiViewModelFactory

const val UNSUPPORTED_COMPONENT_ERROR = "지원하지않는 컴포넌트에 ViewModel을 주입하셨습니다."

inline fun <reified VM : ViewModel> ViewModelStoreOwner.injectViewModel(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
): Lazy<VM> =
    when (this) {
        is ComponentActivity -> {
            when (extrasProducer == null) {
                true -> this.viewModels { AutoDiViewModelFactory }
                false -> this.viewModels(extrasProducer) { AutoDiViewModelFactory }
            }
        }
        is Fragment -> {
            when (extrasProducer == null) {
                true -> this.viewModels(ownerProducer) { AutoDiViewModelFactory }
                false -> this.viewModels(ownerProducer, extrasProducer) { AutoDiViewModelFactory }
            }
        }
        else -> throw IllegalStateException(UNSUPPORTED_COMPONENT_ERROR)
    }

inline fun <reified VM : ViewModel> Fragment.injectActivityViewModel(noinline extrasProducer: (() -> CreationExtras)? = null): Lazy<VM> =
    this.activityViewModels(extrasProducer) { AutoDiViewModelFactory }
