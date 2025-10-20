package test.fixture

import androidx.lifecycle.ViewModel
import com.example.di.DatabaseLogger
import com.example.di.RequireInjection
import com.example.di.scope.AppScope
import com.example.di.scope.ViewModelScope

@ViewModelScope
class FakeCartViewModel : ViewModel() {
    @RequireInjection(impl = FakeCartRepositoryImpl::class, scope = AppScope::class)
    @DatabaseLogger
    lateinit var fakeCartRepository: FakeCartRepository

    @RequireInjection(impl = FakeProductRepositoryImpl::class, scope = ViewModelScope::class)
    lateinit var fakeProductRepository: FakeProductRepository
}
