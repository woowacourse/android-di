package olive.di.fixture

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import olive.di.DIModule
import olive.di.DIViewModel
import olive.di.ViewModelFactory
import olive.di.annotation.Inject
import olive.di.annotation.ViewModelScope

class Baz

class ViewModelScopeTestModule : DIModule {
    @ViewModelScope
    fun bindBaz(): Baz = Baz()
}

class ViewModelScopeTestViewModel1 : DIViewModel() {
    @Inject
    lateinit var baz: Baz
}

class ViewModelScopeTestActivity1 : AppCompatActivity() {
    private val application: TestApplication by lazy { applicationContext as TestApplication }
    val viewModel: ViewModelScopeTestViewModel1 by viewModels { ViewModelFactory(application.diContainer) }
}

class ViewModelScopeTestViewModel2 : DIViewModel() {
    @Inject
    lateinit var baz: Baz
}

class ViewModelScopeTestActivity2 : AppCompatActivity() {
    private val application: TestApplication by lazy { applicationContext as TestApplication }
    val viewModel: ViewModelScopeTestViewModel2 by viewModels { ViewModelFactory(application.diContainer) }
}
