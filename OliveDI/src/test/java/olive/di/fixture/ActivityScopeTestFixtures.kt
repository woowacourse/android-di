package olive.di.fixture

import olive.di.DIActivity
import olive.di.DIModule
import olive.di.annotation.ActivityScope

class Bar

class ActivityScopeTestModule : DIModule {
    @ActivityScope
    fun bindBar(): Bar = Bar()
}

class ActivityScopeTestActivity1 : DIActivity() {
    private val application: TestApplication by lazy { applicationContext as TestApplication }
    val bar: Bar = application.diContainer.instance(Bar::class)
}

class ActivityScopeTestActivity2 : DIActivity() {
    private val application: TestApplication by lazy { applicationContext as TestApplication }
    val bar: Bar = application.diContainer.instance(Bar::class)
}
