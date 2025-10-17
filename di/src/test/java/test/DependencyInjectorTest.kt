package test

import com.example.di.DatabaseLogger
import com.example.di.DependencyInjector
import com.example.di.InMemoryLogger
import org.assertj.core.api.SoftAssertions
import org.checkerframework.checker.units.qual.A
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import test.fixture.FakeAppContainer
import test.fixture.FakeCartRepositoryImpl
import test.fixture.FakeCartViewModel
import test.fixture.FakeProductRepository
import test.fixture.NoAnnotationViewModel

class DependencyInjectorTest {
    private lateinit var fakeCartRepository: FakeCartRepositoryImpl
    private lateinit var fakeProductRepository: FakeProductRepository
    private lateinit var fakeProductRepository2: FakeProductRepository
    private lateinit var fakeAppContainer: FakeAppContainer

    @Before
    fun setUp() {
        fakeAppContainer = FakeAppContainer()
        fakeCartRepository = fakeAppContainer.cartRepository
        fakeProductRepository = fakeAppContainer.productRepository
        fakeProductRepository2 = fakeAppContainer.productRepository2
    }

    @Test
    fun `인스턴스 생성 시 필드가 주입된다`() {
        // given
        DependencyInjector.setInstance(fakeAppContainer)

        // when
        val viewModel =
            DependencyInjector.getInstance(FakeCartViewModel::class)
        DependencyInjector.injectAnnotatedProperties(FakeCartViewModel::class, viewModel)

        // then
        Assert.assertEquals(fakeCartRepository, viewModel.fakeCartRepository)
    }

    @Test
    fun `어노테이션에 따라 올바른 구현체를 주입 받는다`() {
        // given
        val softly = SoftAssertions()
        DependencyInjector.setInstance(fakeAppContainer)

        // when
        val databaseLogger =
            DependencyInjector.getInstance(
                FakeProductRepository::class,
                qualifier = DatabaseLogger::class,
            )
        val inMemoryLogger =
            DependencyInjector.getInstance(
                FakeProductRepository::class,
                qualifier = InMemoryLogger::class,
            )

        // then
        softly.assertThat(inMemoryLogger).isSameAs(fakeProductRepository)
        softly.assertThat(databaseLogger).isSameAs(fakeProductRepository2)
        softly.assertAll()
    }

    @Test
    fun `viewModel에 ViewModelScope어노테이션이 존재하지 않으면 예외가 발생한다`() {
        // given

        // when

        // then
        Assert.assertThrows(IllegalStateException::class.java) {
            DependencyInjector.getInstance(
                NoAnnotationViewModel::class,
            )
        }
    }
}
