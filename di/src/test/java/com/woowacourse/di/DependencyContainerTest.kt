package com.woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import com.woowacourse.di.annotation.Inject
import com.woowacourse.di.annotation.Qualifier
import com.woowacourse.di.DependencyContainer

class FakeRepository(
    @Inject val fakeCartRepository: FakeCartRepository,
    val fakeProductRepository: FakeProductRepository,
)

class FakeCartRepository {
    @Inject
    @Qualifier("field")
    val fakeFieldRepository: FakeFieldRepository? = null
}

class FakeProductRepository

class FakeFieldRepository

class DependencyContainerTest {
    @Test
    fun `Container에 instance가 없으면 에러를 던져야 한다`() {
        // when,then
        assertThrows(IllegalArgumentException::class.java) {
            DependencyContainer.instance<FakeCartRepository>(FakeCartRepository::class)
        }
    }

    @Test
    fun `Container에 instance가 있으면 instance를 반환해야 한다`() {
        // given
        val fakeCartRepository = FakeCartRepository()
        DependencyContainer.addInstance(FakeCartRepository::class, fakeCartRepository)

        // when
        val actual = DependencyContainer.instance<FakeCartRepository>(FakeCartRepository::class)

        // then
        assertThat(actual).isEqualTo(fakeCartRepository)
    }

    @Test
    fun `Container에 qualifier와 일치하는 instance가 없으면 에러를 던져야 한다`() {
        // given
        val cartRepository = FakeCartRepository()
        DependencyContainer.addInstance(FakeCartRepository::class, cartRepository, "?")

        // when,then
        assertThrows(IllegalArgumentException::class.java) {
            DependencyContainer.instance<FakeCartRepository>(FakeCartRepository::class, "???")
        }
    }

    @Test
    fun `Container에 qualifier와 일치하는 instance가 있으면 instance를 반환해야 한다`() {
        // given
        val cartRepository = FakeCartRepository()
        DependencyContainer.addInstance(FakeCartRepository::class, cartRepository, "field")

        // when
        val actual =
            DependencyContainer.instance<FakeCartRepository>(FakeCartRepository::class, "field")

        // then
        assertThat(actual).isEqualTo(cartRepository)
    }
}
