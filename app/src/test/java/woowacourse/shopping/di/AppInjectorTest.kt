package woowacourse.shopping.di

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import woowacourse.shopping.di.definition.Qualifier
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fixture.ElectricFixtureCarImpl
import woowacourse.shopping.fixture.EngineFixtureCarImpl
import woowacourse.shopping.fixture.FactoryFixtureCar
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.fixture.FakeSavedStateOwner
import woowacourse.shopping.fixture.FixtureCar
import woowacourse.shopping.fixture.SingletonFixtureCar
import woowacourse.shopping.fixture.ViewModelConstructorInjectFixture
import woowacourse.shopping.fixture.ViewModelFieldInjectFixture
import woowacourse.shopping.ui.common.AppViewModelFactory

class AppInjectorTest :
    StringSpec({
        beforeTest {
            InjectContainer.init(emptyList())
        }

        fun setUpArchTaskExecutor() {
            ArchTaskExecutor.getInstance().setDelegate(
                object : TaskExecutor() {
                    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                    override fun postToMainThread(runnable: Runnable) = runnable.run()

                    override fun isMainThread(): Boolean = true
                },
            )
        }

        fun tearDownArchTaskExecutor() {
            ArchTaskExecutor.getInstance().setDelegate(null)
        }

        "singleton 은 동일한 인스턴스를 반환한다" {
            // given
            val name = "singleton"
            InjectContainer.registerSingleton<SingletonFixtureCar> { _ ->
                Provider { SingletonFixtureCar(name) }
            }

            // when
            val first = InjectContainer.get<SingletonFixtureCar>()
            val second = InjectContainer.get<SingletonFixtureCar>()

            first shouldBeSameInstanceAs second
        }

        "factory 는 매번 새로운 인스턴스를 반환한다" {
            // given
            val name = "factory"
            InjectContainer.registerFactory<FactoryFixtureCar> { _ ->
                Provider {
                    FactoryFixtureCar(
                        name,
                    )
                }
            }

            // when
            val first = InjectContainer.get<FactoryFixtureCar>()
            val second = InjectContainer.get<FactoryFixtureCar>()

            first shouldNotBeSameInstanceAs second
        }

        "qualifier 로 동일 타입을 구분할 수 있다" {
            // given
            InjectContainer.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("engine")) { _ ->
                Provider { EngineFixtureCarImpl("engineCarName") }
            }
            InjectContainer.registerSingleton<FixtureCar>(qualifier = Qualifier.Named("electric")) { _ ->
                Provider { ElectricFixtureCarImpl("electricCarName") }
            }

            // when
            val engineCar = InjectContainer.get<FixtureCar>(qualifier = Qualifier.Named("engine"))
            val electricCar =
                InjectContainer.get<FixtureCar>(qualifier = Qualifier.Named("electric"))

            // then
            engineCar.shouldBeInstanceOf<EngineFixtureCarImpl>()
            electricCar.shouldBeInstanceOf<ElectricFixtureCarImpl>()
        }

        "ViewModel의 경우 필드 주입으로 의존성을 주입할 수 있다" {
            setUpArchTaskExecutor()

            // given
            InjectContainer.registerSingleton<CartRepository> { _ -> Provider { FakeCartRepository() } }
            InjectContainer.registerSingleton<ProductRepository> { _ -> Provider { FakeProductRepository() } }

            // when
            val viewModel =
                AppViewModelFactory(owner = FakeSavedStateOwner()).create(
                    ViewModelFieldInjectFixture::class.java,
                )

            // then
            viewModel.shouldBeInstanceOf<ViewModelFieldInjectFixture>()
            viewModel.cartRepository.shouldBeInstanceOf<CartRepository>()
            viewModel.cartRepository.shouldBeInstanceOf<FakeCartRepository>()
            viewModel.productRepository.shouldBeInstanceOf<ProductRepository>()
            viewModel.productRepository.shouldBeInstanceOf<FakeProductRepository>()

            tearDownArchTaskExecutor()
        }

        "ViewModel의 경우 생성자 주입으로 의존성을 주입할 수 있다" {
            setUpArchTaskExecutor()

            // given
            InjectContainer.registerFactory<ViewModelConstructorInjectFixture> { injector ->
                Provider { ViewModelConstructorInjectFixture(injector.get(), injector.get()) }
            }
            InjectContainer.registerSingleton<CartRepository> { _ -> Provider { FakeCartRepository() } }
            InjectContainer.registerSingleton<ProductRepository> { _ -> Provider { FakeProductRepository() } }

            // when
            val viewModel =
                AppViewModelFactory(owner = FakeSavedStateOwner()).create(
                    ViewModelConstructorInjectFixture::class.java,
                )

            // then
            viewModel.shouldBeInstanceOf<ViewModelConstructorInjectFixture>()
            viewModel.cartRepository.shouldBeInstanceOf<CartRepository>()
            viewModel.cartRepository.shouldBeInstanceOf<FakeCartRepository>()
            viewModel.productRepository.shouldBeInstanceOf<ProductRepository>()
            viewModel.productRepository.shouldBeInstanceOf<FakeProductRepository>()

            tearDownArchTaskExecutor()
        }
    })
