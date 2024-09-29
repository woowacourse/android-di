package woowacourse.shopping.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.annotation.ActivityLifecycleAware
import woowacourse.shopping.di.annotation.ApplicationLifecycleAware
import woowacourse.shopping.di.annotation.FieldInject
import woowacourse.shopping.di.annotation.ViewModelLifecycleAware

class ViewModelScopeObject

class FakeViewModel : ViewModel() {
    @FieldInject
    @ViewModelLifecycleAware
    lateinit var viewModelScopeObject: ViewModelScopeObject

    @FieldInject
    @ApplicationLifecycleAware
    lateinit var applicationScopeObject: ApplicationScopeObject
}

class ActivityScopeObject

class FakeActicity : DependencyInjectedActivity() {
    val fakeViewModel: FakeViewModel by injectViewModels<FakeViewModel>()

    @ActivityLifecycleAware
    val activityScopeObject: ActivityScopeObject by lazy {
        dependencyInjector.createInstanceOfProperty(::activityScopeObject)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class ApplicationScopeObject

class FakeApplication : DependencyInjectedApplication() {
    override fun onCreate() {
        super.onCreate()
        applicationDependencyContainer.setDependency(
            ApplicationScopeObject::class,
            ApplicationScopeObject::class,
        )
        applicationDependencyContainer.setDependency(
            ActivityScopeObject::class,
            ActivityScopeObject::class,
        )
        applicationDependencyContainer.setDependency(
            ViewModelScopeObject::class,
            ViewModelScopeObject::class,
        )
    }
}
