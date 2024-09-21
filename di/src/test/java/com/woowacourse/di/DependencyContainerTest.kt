package com.woowacourse.di

import com.google.common.truth.Truth.assertThat
import com.woowacourse.di.annotation.Inject
import com.woowacourse.di.annotation.Qualifier
import org.junit.Test

class FakeCartRepository {
    @Inject
    @Qualifier("field")
    val fakeFieldRepository: FakeFieldRepository? = null
}

class FakeFieldRepository

class DependencyContainerTest {
    @Test
    fun `Container에 instance가 없으면 instance를 만들어서 반환한다`() {
        // when
        val actual = DependencyContainer.instance<FakeCartRepository>(FakeCartRepository::class)

        // then
        assertThat(actual).isNotNull()
    }

    @Test
    fun `Container에 instance가 있으면 해당 instance를 반환한다`() {
        // given
        val fakeCartRepository = FakeCartRepository()
        DependencyContainer.addInstance(FakeCartRepository::class, fakeCartRepository)

        // when
        val actual = DependencyContainer.instance<FakeCartRepository>(FakeCartRepository::class)

        // then
        assertThat(actual).isEqualTo(fakeCartRepository)
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
