package woowacourse.shopping

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.woowacourse.di.DependencyContainer

class LifecycleTracker(
    private val viewModel: ViewModel,
) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        DependencyContainer.clearActivityInstances()
        observeViewModelCloseable()
    }

    private fun observeViewModelCloseable() {
        viewModel.addCloseable {
            DependencyContainer.clearViewModelInstances()
        }
    }
}
