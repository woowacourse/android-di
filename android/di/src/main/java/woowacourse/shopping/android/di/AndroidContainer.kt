package woowacourse.shopping.android.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.core.di.DependencyContainer

object AndroidContainer {
    private val appContainer = DependencyContainer()
    private val activityContainers = mutableMapOf<Activity, DependencyContainer>()
    private val viewModelContainers = mutableMapOf<ViewModel, DependencyContainer>()

    fun ofApplication(): DependencyContainer = appContainer

    fun ofActivity(activity: Activity): DependencyContainer = activityContainers.getOrPut(activity) { DependencyContainer() }

    fun ofViewModel(viewModel: ViewModel): DependencyContainer = viewModelContainers.getOrPut(viewModel) { DependencyContainer() }

    fun clear(activity: Activity) {
        activityContainers.remove(activity)
    }

    fun clear(viewModel: ViewModel) {
        viewModelContainers.remove(viewModel)
    }
}
