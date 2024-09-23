package com.example.seogi.di

import android.content.Context
import com.example.seogi.fixture.Bar
import com.example.seogi.fixture.ChildFoo1
import com.example.seogi.fixture.ChildFoo2
import com.example.seogi.fixture.FakeModule
import com.example.seogi.fixture.FakeViewModel
import com.example.seogi.fixture.FakeViewModel2
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Test

class DiContainerTest {
    private val context: Context = mockk()

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터X, 어노테이션X)`() {
        // given
        val diContainer = DiContainer(FakeModule, context)

        // when
        val actual = diContainer.instance(Bar::class)

        // then
        assertThat(actual).isInstanceOf(Bar::class.java)
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터X, , 어노테이션O)`() {
        // given
        val diContainer = DiContainer(FakeModule, context)

        // when
        val actual = diContainer.instance(FakeViewModel::class)

        // then
        assertThat(actual.childFoo).isInstanceOf(ChildFoo1::class.java)
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터O, , 어노테이션O)`() {
        // given
        val diContainer = DiContainer(FakeModule, context)

        // when
        val actual = diContainer.instance(FakeViewModel2::class)

        // then
        assertThat(actual.childFoo).isInstanceOf(ChildFoo2::class.java)
    }
}
