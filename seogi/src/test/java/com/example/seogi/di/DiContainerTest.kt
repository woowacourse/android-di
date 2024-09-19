package com.example.seogi.di

import com.example.seogi.fixture.Bar
import com.example.seogi.fixture.Child1
import com.example.seogi.fixture.Child2
import com.example.seogi.fixture.ChildFoo1
import com.example.seogi.fixture.ChildFoo2
import com.example.seogi.fixture.FakeModule
import com.example.seogi.fixture.ParentFoo
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DiContainerTest {
    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터X, 어노테이션X)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(Bar::class, null)

        // then
        assertThat(actual).isInstanceOf(Bar::class.java)
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터X, , 어노테이션O)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(ParentFoo::class, Child1())

        // then
        assertThat(actual).isInstanceOf(ChildFoo1::class.java)
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터O, , 어노테이션O)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(ParentFoo::class, Child2())

        // then
        assertThat(actual).isInstanceOf(ChildFoo2::class.java)
    }
}
