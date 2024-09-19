package com.example.seogi.di

import com.example.seogi.di.annotation.FieldInject
import com.example.seogi.fixture.FakeViewModel
import com.google.common.truth.Truth
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties

class FieldInjectionTest {
    @Test
    fun `FiledInject어노테이션이 붙은 필드에 접근할 수 있다`() {
        val viewModelClass = FakeViewModel::class
        val fields =
            viewModelClass.declaredMemberProperties.filter { it.annotations.contains(FieldInject()) }

        Truth.assertThat(fields).hasSize(1)
    }
}
