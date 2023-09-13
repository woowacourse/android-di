package com.bignerdranch.android.koala

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class InjectorTest {

    private lateinit var injector: Injector

    @Before
    fun setUp() {
        injector = Injector(FakeContainer())
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
    fun `추상화된 주 생성자 하나(실제로는 Room과 연결된 구현체)가 있는 클래스의 의존성을 주입한다`() {
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
}
