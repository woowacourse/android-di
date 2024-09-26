package olive.di.fixture

import olive.di.DIModule
import olive.di.DIViewModel
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

    fun onClearedTest() {
        super.onCleared()
    }
}

class ViewModelScopeTestViewModel2 : DIViewModel() {
    @Inject
    lateinit var baz: Baz

    fun onClearedTest() {
        super.onCleared()
    }
}
