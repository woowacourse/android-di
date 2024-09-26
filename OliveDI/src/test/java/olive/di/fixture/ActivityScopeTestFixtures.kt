package olive.di.fixture

import olive.di.DIActivity
import olive.di.DIModule
import olive.di.annotation.ActivityScope
import olive.di.annotation.Inject

class Bar

class ActivityScopeTestModule : DIModule {
    @ActivityScope
    fun bindBar(): Bar = Bar()
}

class ActivityScopeTestActivity1 : DIActivity() {
    @Inject
    lateinit var bar: Bar
}

class ActivityScopeTestActivity2 : DIActivity() {
    @Inject
    lateinit var bar: Bar
}
