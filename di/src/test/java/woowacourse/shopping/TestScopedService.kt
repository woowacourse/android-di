package woowacourse.shopping

import shopping.di.Inject
import shopping.di.Scope
import shopping.di.ScopeAnnotation

interface ScopedService {
    fun getScopeMessage(): String
}

class AppScopedService : ScopedService {
    override fun getScopeMessage(): String = "App Scoped Service"
}

class ActivityScopedService : ScopedService {
    override fun getScopeMessage(): String = "Activity Scoped Service"
}

class ViewModelScopedService : ScopedService {
    override fun getScopeMessage(): String = "ViewModel Scoped Service"
}

class ScopedTestClass {

    @Inject
    @ScopeAnnotation(Scope.APP)
    lateinit var appScopedService: ScopedService

    @Inject
    @ScopeAnnotation(Scope.ACTIVITY)
    lateinit var activityScopedService: ScopedService

    @Inject
    @ScopeAnnotation(Scope.VIEWMODEL)
    lateinit var viewModelScopedService: ScopedService
}
