package woowa.shopping.di.libs.android.sample

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import woowa.shopping.di.libs.android.ScopeViewModel

internal class SampleViewModel(
    private val sampleRepository: SampleRepository,
) : ScopeViewModel() {
    private val _sample: MutableStateFlow<String> = MutableStateFlow("")
    val sample: StateFlow<String> = _sample.asStateFlow()

    fun init() {
        updateSample()
    }

    fun updateSample() {
        viewModelScope.launch {
            _sample.value = sampleRepository.sample()
        }
    }
}
