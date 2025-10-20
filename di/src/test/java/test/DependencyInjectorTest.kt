package test

import com.example.di.DatabaseLogger
import com.example.di.DependencyInjector
import com.example.di.InMemoryLogger
import org.assertj.core.api.SoftAssertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import test.fixture.FakeAppContainer
import test.fixture.FakeCartRepositoryImpl
import test.fixture.FakeCartViewModel
import test.fixture.FakeProductRepository

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
            DependencyInjector.getOrCreateInstance(FakeCartViewModel::class)

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
            DependencyInjector.getOrCreateInstance(
                FakeProductRepository::class,
                qualifier = DatabaseLogger::class,
            )
        val inMemoryLogger =
            DependencyInjector.getOrCreateInstance(
                FakeProductRepository::class,
                qualifier = InMemoryLogger::class,
            )

        // then
        softly.assertThat(inMemoryLogger).isSameAs(fakeProductRepository)
        softly.assertThat(databaseLogger).isSameAs(fakeProductRepository2)
        softly.assertAll()
    }
}
