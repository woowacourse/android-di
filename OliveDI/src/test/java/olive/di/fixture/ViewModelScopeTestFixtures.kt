package olive.di.fixture

import olive.di.DIActivity
import olive.di.DIModule
import olive.di.DIViewModel
import olive.di.annotation.Inject
import olive.di.annotation.ViewModelScope
import olive.di.injectViewModel

class Baz

class ViewModelScopeTestModule : DIModule {
    @ViewModelScope
    fun bindBaz(): Baz = Baz()
}

class ViewModelScopeTestViewModel1 : DIViewModel() {
    @Inject
    lateinit var baz: Baz
}

class ViewModelScopeTestActivity1 : DIActivity() {
    val viewModel: ViewModelScopeTestViewModel1 by injectViewModel()
}

class ViewModelScopeTestViewModel2 : DIViewModel() {
    @Inject
    lateinit var baz: Baz
}

class ViewModelScopeTestActivity2 : DIActivity() {
    val viewModel: ViewModelScopeTestViewModel2 by injectViewModel()
}
