package test.fixture

import androidx.lifecycle.ViewModel
import com.example.di.DatabaseLogger
import com.example.di.RequireInjection
import com.example.di.ViewModelScope

@ViewModelScope
class FakeCartViewModel : ViewModel() {
    @RequireInjection
    @DatabaseLogger
    lateinit var fakeCartRepository: FakeCartRepositoryImpl
}
