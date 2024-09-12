package woowacourse.shopping.di

import com.example.seogi.di.annotation.FieldInject
import com.google.common.truth.Truth
import org.junit.Test
import woowacourse.shopping.ui.MainViewModel
import kotlin.reflect.full.declaredMemberProperties

class FieldInjectionTest {
    @Test
    fun `FiledInject어노테이션이 붙은 필드에 접근할 수 있다`() {
        val mainViewModelClass = MainViewModel::class
        val fields = mainViewModelClass.declaredMemberProperties.filter { it.annotations.contains(FieldInject()) }

        Truth.assertThat(fields).hasSize(1)
    }
}
