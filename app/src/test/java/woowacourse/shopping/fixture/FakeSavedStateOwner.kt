package woowacourse.shopping.fixture

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import io.mockk.mockk

class FakeSavedStateOwner : SavedStateRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val mockSavedStateRegistry = mockk<SavedStateRegistry>(relaxed = true)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = mockSavedStateRegistry
}
