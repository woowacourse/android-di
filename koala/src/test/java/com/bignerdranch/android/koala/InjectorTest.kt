package com.bignerdranch.android.koala

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class InjectorTest {

    private lateinit var injector: Injector

    @Before
    fun setUp() {
        injector = Injector(FakeModule())
    }

    @Test
    fun `필드가 하나가 있는 클래스의 의존성을 주입한다`() {
        // given
        class FakeViewModel {
            @KoalaFieldInject
            lateinit var fakeProductRepository: FakeProductRepository
        }

        // when
        val instance = injector.inject(FakeViewModel::class)

        // then
        assertThat(instance).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `주 생성자 하나가 있는 클래스의 의존성을 주입한다`() {
        // given
        class FakeViewModel(
            private val fakeProductRepository: FakeProductRepository,
        )

        // when
        val instance = injector.inject(FakeViewModel::class)

        // then
        assertThat(instance).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `주 생성자가 여러개인 클래스의 의존성을 주입한다`() {
        // given
        class FakeViewModel(
            private val fakeProductRepository: FakeProductRepository,
            @FakeRoomDBRepository private val fakeCartRepository: FakeCartRepository,
        )

        // when
        val instance = injector.inject(FakeViewModel::class)

        // then
        assertThat(instance).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `구현체가 2개인 추상화된 주 생성자가 있는 클래스의 의존성을 주입할 때 Quilfier로 구분하여 주입한다`() {
        // given
        class FakeViewModel(
            @FakeInMemoryRepository val fakeInMemoryRepository: FakeCartRepository,
            @FakeRoomDBRepository val fakeRoomDBRepository: FakeCartRepository,
        )

        // when
        val instance = injector.inject(FakeViewModel::class)

        // then
        assertThat(instance.fakeInMemoryRepository).isInstanceOf(FakeImMemoryCartRepository::class.java)
        assertThat(instance.fakeRoomDBRepository).isInstanceOf(FakeDefaultCartRepository::class.java)
    }

    @Test
    fun `생성자가 재귀적으로 생성해야 하는 생성자인 경우 재귀적으로 주입하여 생성한다`() {
        // given
        class FakeViewModel(
            @FakeRoomDBRepository
            val fakeCartRepository: FakeCartRepository,
        )

        // when
        val instance = injector.inject(FakeViewModel::class)

        // then
        assertThat(instance).isInstanceOf(FakeViewModel::class.java)
        assertThat(instance.fakeCartRepository).isInstanceOf(FakeDefaultCartRepository::class.java)
    }

    @Test
    fun `구현체가 2개인 추상화된 주 생성자가 있는 클래스의 의존성을 주입할 때 Quilfier를 붙이지 않으면 주입에 실패한다`() {
        // given
        class FakeViewModel(
            val fakeCartRepository: FakeCartRepository,
        )

        // then
        assertThrows(IllegalStateException::class.java) {
            injector.inject(FakeViewModel::class)
        }.also {
            assertEquals("여러개 일치할 수 없습니다", it.message)
        }
    }

    @Test
    fun `필드 주입 시 어노테이션을 붙이지 않으면 필드 주입에실패한다`() {
        // given
        class FakeViewModel {
            lateinit var fakeProductRepository: FakeProductRepository
        }

        // when
        val instance = injector.inject(FakeViewModel::class)

        // then
        assertFailsWith<UninitializedPropertyAccessException> {
            instance.fakeProductRepository
        }
    }

    @Test
    fun `singleton 어노테이션이 붙은 경우 해당 클래스의 인스턴스를 매번 새로 생성하지 않는다`() {
        // given
        class FakeViewModel(
            val repository: FakeSingletonRepository,
        )

        // when
        val firstCall = injector.inject(FakeViewModel::class)
        val secondCall = injector.inject(FakeViewModel::class)

        // then
        assertThat(firstCall.repository).isEqualTo(secondCall.repository)
    }

    @Test
    fun `singleton 어노테이션이 붙지 않은 경우 해당 클래스의 인스턴스를 매번 새로 생성한다`() {
        // given
        class FakeViewModel(
            val repository: FakeNonSingletonRepository,
        )

        // when
        val firstCall = injector.inject(FakeViewModel::class)
        val secondCall = injector.inject(FakeViewModel::class)

        // then
        assertThat(firstCall.repository).isNotEqualTo(secondCall.repository)
    }
}
