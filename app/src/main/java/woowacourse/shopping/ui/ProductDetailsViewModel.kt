package woowacourse.shopping.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProductDetailsViewModel : DefaultLifecycleObserver {

    private val viewModelScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main.immediate + SupervisorJob()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        fetchProducts()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        viewModelScope.coroutineContext.cancel()

        super.onDestroy(owner)
    }

    private fun fetchProducts() {
        // ...
        viewModelScope.launch {

        }
    }
}
