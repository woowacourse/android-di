package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.typeOf

class DIViewModelFactoryTest {
    interface Repository

    class FakeRepository : Repository

    class InjectViewModel : ViewModel() {
        @Inject
        lateinit var fakeRepository: Repository
    }

    @Test
    fun `Inject_어노테이션이_붙은_필드에_의존성이_정상적으로_주입되어야_한다`() {
        val appContainer = AppContainerImpl()
        val fakeRepository = FakeRepository()

        appContainer.register(typeOf<Repository>()) { fakeRepository }
        val factory = DIViewModelFactory(appContainer)

        val viewModel =
            factory.create(
                InjectViewModel::class.java,
                CreationExtras.Empty,
            )

        assertThat(viewModel.fakeRepository).isEqualTo(fakeRepository)
    }
}
