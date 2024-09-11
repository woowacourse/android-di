package woowacourse.shopping.di

import com.google.common.truth.Truth
import org.junit.Test
import woowacourse.shopping.ui.MainViewModel

class FieldInjectionTest {
    @Test
    fun `FiledInject어노테이션이 붙은 필드에 접근할 수 있다`() {
        val mainViewModelClass = MainViewModel::class
        val fields = mainViewModelClass.java.fields.filter { it.annotations.contains(FieldInject()) }

        Truth.assertThat(fields).hasSize(1)
    }
}
