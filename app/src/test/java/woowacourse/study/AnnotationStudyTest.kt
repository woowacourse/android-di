package woowacourse.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import javax.inject.Qualifier
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

@Qualifier
annotation class AlsongAnnotation1

annotation class AlsongAnnotation2

class Cat {
    @AlsongAnnotation1
    val name = "alsong"

    @AlsongAnnotation2
    val age = 10
}

class AnnotationStudyTest {
    @Test
    fun `프로퍼티에 특정 어노테이션이 붙어있으면 hasAnnotation함수로 확인할 수 있다`() {
        // given
        val alsongCat = Cat()

        // when
        val properties = alsongCat::class.memberProperties
        val actual = properties.filter { it.hasAnnotation<AlsongAnnotation1>() }
        val expected = listOf(alsongCat::class.memberProperties.find { it.name == "name" })

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `annotationClass의 findAnnotation함수로 특정 어노테이션에 붙은 어노테이션을 찾을 수 있다`() {
        // given
        val alsongCat = Cat()

        // when
        val properties = alsongCat::class.memberProperties
        val actual =
            properties.filter { property ->
                property.annotations.any { it.annotationClass.findAnnotation<Qualifier>() != null }
            }
        val expected = listOf(alsongCat::class.memberProperties.find { it.name == "name" })

        // then
        assertThat(actual).isEqualTo(expected)
    }
}
