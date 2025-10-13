package woowacourse.shopping.di

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test

class ViewModelFactoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

//    private lateinit var fakeOwner: SavedStateRegistryOwner
    private lateinit var viewModel: InjectViewModel
    private lateinit var fakeContainer: FakeAppContainer

    @Test
    fun `@Inject 필드 주입이 정상적으로 수행된다`() {
        // given

        // when

        // then
    }
}
