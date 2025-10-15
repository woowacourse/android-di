package woowacourse.di

import androidx.lifecycle.ViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class AutoInjectingViewModelFactoryTest {
    class TestRepository

    class AnotherRepository

    class MainViewModel : ViewModel() {
        @InjectField
        lateinit var repository: TestRepository
    }

    class CartViewModel : ViewModel() {
        @InjectField
        lateinit var repository: TestRepository
    }

    class MixedViewModel : ViewModel() {
        @InjectField
        lateinit var repository: TestRepository

        @InjectField
        lateinit var anotherRepository: AnotherRepository
    }

    @Test
    fun `ViewModel이_정상적으로_주입된다`() {
        // given
        val container =
            Container().apply {
                bindSingleton(TestRepository::class) { TestRepository() }
                bindSingleton(AnotherRepository::class) { AnotherRepository() }
            }
        val factory = AutoInjectingViewModelFactory(container)

        // when
        val viewModel = factory.create(MixedViewModel::class.java)

        // then
        assertThat(extractPrivateField(viewModel, "repository"))
            .isInstanceOf(TestRepository::class.java)
        assertThat(extractPrivateField(viewModel, "anotherRepository"))
            .isInstanceOf(AnotherRepository::class.java)
    }

    @Test
    fun `두_ViewModel이_같은_의존성을_주입받는다`() {
        // given
        val container =
            Container().apply {
                bindSingleton(TestRepository::class) { TestRepository() }
            }
        val factory = AutoInjectingViewModelFactory(container)

        // when
        val mainViewModel = factory.create(MainViewModel::class.java)
        val cartViewModel = factory.create(CartViewModel::class.java)

        // then
        val mainRepository = extractPrivateField(mainViewModel, "repository")
        val cartRepository = extractPrivateField(cartViewModel, "repository")
        assertThat(mainRepository === cartRepository).isTrue()
    }

    private fun extractPrivateField(
        viewModel: ViewModel,
        fieldName: String,
    ): Any? {
        val property = viewModel::class.declaredMemberProperties.first { it.name == fieldName }
        property.isAccessible = true
        return property.getter.call(viewModel)
    }
}
