package woowacourse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.Closeable
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

    class ConstructorInjectedViewModel(
        val repository: TestRepository,
        val anotherRepository: AnotherRepository,
    ) : ViewModel()

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
    fun `스코프를_지정하지_않은_경우_두_ViewModel이_같은_의존성을_주입받는다`() {
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

    @Test
    fun `스코프를_지정한_경우_두_ViewModel은_ViewModel_스코프의_의존성을_공유하지_않는다`() {
        // given
        val container =
            Container().apply {
                bindScoped(TestRepository::class, scopeType = ScopeType.VIEW_MODEL) { TestRepository() }
            }
        val factory = AutoInjectingViewModelFactory(container)

        // when
        val mainViewModel = factory.create(MainViewModel::class.java)
        val cartViewModel = factory.create(CartViewModel::class.java)

        // then
        val mainRepository = extractPrivateField(mainViewModel, "repository")
        val cartRepository = extractPrivateField(cartViewModel, "repository")
        assertThat(mainRepository === cartRepository).isFalse()
    }

    @Test
    fun `생성자로_의존성이_주입된다`() {
        // given
        val container =
            Container().apply {
                bindSingleton(TestRepository::class) { TestRepository() }
                bindSingleton(AnotherRepository::class) { AnotherRepository() }
            }
        val factory = AutoInjectingViewModelFactory(container)

        // when
        val viewModel = factory.create(ConstructorInjectedViewModel::class.java)

        // then
        val repositoryFromContainer = container.get(TestRepository::class)
        val anotherFromContainer = container.get(AnotherRepository::class)

        assertThat(viewModel.repository === repositoryFromContainer).isTrue()
        assertThat(viewModel.anotherRepository === anotherFromContainer).isTrue()
    }

    @Test
    fun `ViewModel_해제시_ViewModel_스코프가_정리된다`() {
        // given
        val container =
            Container().apply {
                bindScoped(TestRepository::class, scopeType = ScopeType.VIEW_MODEL) { TestRepository() }
            }
        val factory = AutoInjectingViewModelFactory(container)
        val extras =
            MutableCreationExtras().apply {
                set(ViewModelProvider.NewInstanceFactory.VIEW_MODEL_KEY, "scopedViewModel")
            }

        // when
        val viewModel = factory.create(MainViewModel::class.java, extras)
        val repositoryBeforeClear = extractPrivateField(viewModel, "repository")
        retrieveScopeCloseable(viewModel).close()
        val recreatedViewModel = factory.create(MainViewModel::class.java, extras)
        val repositoryAfterClear = extractPrivateField(recreatedViewModel, "repository")

        // then
        assertThat(repositoryBeforeClear === repositoryAfterClear).isFalse()
    }

    private fun extractPrivateField(
        viewModel: ViewModel,
        fieldName: String,
    ): Any? {
        val property = viewModel::class.declaredMemberProperties.first { it.name == fieldName }
        property.isAccessible = true
        return property.getter.call(viewModel)
    }

    private fun retrieveScopeCloseable(viewModel: ViewModel): Closeable {
        val field = ViewModel::class.java.getDeclaredField("mBagOfTags")
        field.isAccessible = true
        val tags =
            field.get(viewModel) as? MutableMap<*, *>
                ?: error("ViewModel tags are not available")
        return tags[AutoInjectingViewModelFactory.VIEW_MODEL_SCOPE_TAG] as? Closeable
            ?: error("Scope closeable is not registered")
    }
}
