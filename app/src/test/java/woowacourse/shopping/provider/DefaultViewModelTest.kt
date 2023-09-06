package woowacourse.shopping.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

/**
 * LiveData와 Coroutine Test를 할 수 있도록 기능을 제공합니다.
 */
abstract class DefaultViewModelTest {

    // LiveData에 대한 테스트를 지원합니다.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Cotoutine에 대한 테스트를 지원합니다.
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
}
