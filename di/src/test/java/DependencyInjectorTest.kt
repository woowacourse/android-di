package com.example.di

import com.example.di.test.fixture.FakeCartRepositoryImpl
import com.example.di.test.fixture.FakeCartViewModel
import com.example.di.test.fixture.FakeProductRepositoryImpl
import org.assertj.core.api.SoftAssertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DependencyInjectorTest {
    private lateinit var fakeCartRepository: FakeCartRepositoryImpl
    private lateinit var fakeProductRepository: FakeProductRepositoryImpl

    @Before
    fun setUp() {
        fakeCartRepository = FakeCartRepositoryImpl()
        fakeProductRepository = FakeProductRepositoryImpl()
    }

    @Test
    fun `인스턴스 생성 시 필드가 주입된다`() {
        // given
        DependencyInjector.setInstance(
            FakeCartRepositoryImpl::class,
            fakeCartRepository,
            DatabaseLogger::class,
        )

        // when
        val viewModel =
            DependencyInjector
                .getInstance(FakeCartViewModel::class)
        DependencyInjector
            .injectAnnotatedProperties(FakeCartViewModel::class, viewModel)

        // then
        val field = viewModel::class.java.getDeclaredField("fakeCartRepository")
        field.isAccessible = true
        val actual = field.get(viewModel)
        Assert.assertEquals(fakeCartRepository, actual)
    }

    @Test
    fun `어노테이션에 따라 올바른 구현체를 주입 받는다`() {
        // given
        val softly = SoftAssertions()

        DependencyInjector.setInstance(
            FakeCartRepositoryImpl::class,
            fakeCartRepository,
            DatabaseLogger::class,
        )
        DependencyInjector.setInstance(
            FakeProductRepositoryImpl::class,
            fakeProductRepository,
            InMemoryLogger::class,
        )

        // when
        val databaseLogger =
            DependencyInjector.getInstance(
                FakeCartRepositoryImpl::class,
                qualifier = DatabaseLogger::class,
            )
        val inMemoryLogger =
            DependencyInjector.getInstance(
                FakeProductRepositoryImpl::class,
                qualifier = InMemoryLogger::class,
            )

        // then
        softly.assertThat(databaseLogger).isSameAs(fakeCartRepository)
        softly.assertThat(inMemoryLogger).isSameAs(fakeProductRepository)
        softly.assertAll()
    }
}
