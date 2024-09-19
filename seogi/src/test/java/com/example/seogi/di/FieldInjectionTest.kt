package com.example.seogi.di

import com.example.seogi.di.annotation.FieldInject
import com.example.seogi.fixture.FakeViewModel
import com.google.common.truth.Truth
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties

class FieldInjectionTest {
    @Test
    fun `FieldInject어노테이션이 붙은 필드에 접근할 수 있다`() {
        // given
        val viewModelClass = FakeViewModel::class

        // when
        val fields =
            viewModelClass.declaredMemberProperties.filter { it.annotations.contains(FieldInject()) }

        // then
        Truth.assertThat(fields).hasSize(1)
    }

    @Test
    fun `FieldInject어노테이션이 붙지 않은 필드를 구분할 수 있다`() {
        // given
        val viewModelClass = FakeViewModel::class
        val fields =
            viewModelClass.declaredMemberProperties

        // when
        val fieldInjectFields = fields.filter { it.annotations.contains(FieldInject()) }
        val nonFieldInjectFields = fields.filter { !it.annotations.contains(FieldInject()) }
        val expected = fields.size - fieldInjectFields.size

        // then
        Truth.assertThat(nonFieldInjectFields).hasSize(expected)
    }
}
