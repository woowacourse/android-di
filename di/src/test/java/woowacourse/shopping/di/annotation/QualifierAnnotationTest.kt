package woowacourse.shopping.di.annotation

import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.di.ViewModelFactoryInjector
import woowacourse.shopping.di.fake.FakeDependencyModule
import woowacourse.shopping.di.fake.FakeViewModel

class QualifierAnnotationTest {
    private lateinit var dependencyContainer: DependencyContainer

    @Before
    fun setup() {
        dependencyContainer = DependencyContainer(listOf(FakeDependencyModule()))
    }

    @Test
    fun `Qualifier가 붙은 동일 타입 필드에 서로 다른 의존성이 주입된다`() {
        // given
        val viewModel = ViewModelFactoryInjector(dependencyContainer).create(FakeViewModel::class.java)

        // when
        dependencyContainer.inject(viewModel)

        // then
        assertNotEquals(viewModel.myCartRepository, viewModel.othersCartRepository)
    }
}
