package di

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
import junit.framework.Assert.assertSame
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.hashdi.AppContainer
import woowacourse.shopping.hashdi.Injector
import woowacourse.shopping.hashdi.annotation.Inject

class InjectorTest {

    private lateinit var injector: Injector

    @Before
    fun setup() {
        injector = Injector(AppContainer(listOf(FakeModule)))
    }

    @Test
    fun `의존성 주입해 인스턴스 생성에 성공한다`() {
        // when
        val vm = injector.inject(FakeViewModel::class)

        // then
        Assertions.assertThat(vm).isNotNull
        assertEquals(vm.productFakeRepository, ProductFakeRepository)
        assertEquals(vm.cartFakeRepository, FakeCartDefaultRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `모듈이 정의되지 않아 의존성 주입에 필요한 인스턴스가 없어 인스턴스 생성에 실패한다`() {
        // given
        injector = Injector(AppContainer(listOf()))

        // when
        val vm = injector.inject(FakeViewModel::class)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Inject 어노테이션을 붙이지 않아 인스턴스 생성에 실패한다`() {
        // when
        val vm = injector.inject(FakeViewModelMissingAnnotation::class)
    }

    @Test
    fun `의존성 주입이 필요없는 필드를 가진 인스턴스를 생성한다`() {
        // when
        val vm = injector.inject(FakeViewModelWithDefaultValue::class)

        // then
        Assertions.assertThat(vm).isNotNull
        assertEquals(vm.productFakeRepository, ProductFakeRepository)
    }

    @Test
    fun `필드 주입하여 인스턴스를 생성한다`() {
        // when
        val vm = injector.inject(FakeViewModelWithFieldInjection::class)

        // then
        Assertions.assertThat(vm).isNotNull
        assertEquals(vm.cartFakeRepository, FakeCartDefaultRepository)
    }

    @Test
    fun `재귀 의존성 주입으로 인스턴스 생성한다`() {
        // when
        val vm = injector.inject(FakeViewModelWithRecursiveDI::class)

        // then
        Assertions.assertThat(vm).isNotNull
    }

    @Test
    fun `Qualifier로 구분하여 Room 사용하는 데이터소스 주입한다`() {
        // when
        val vm = injector.inject(FakeViewModelWithRecursiveDI::class)

        // then
        val actualDatasource = vm.fakeRepositoryWithDataSource.fakeDatasource
        assertSame(FakeRoomDataSource, actualDatasource)
        assertNotSame(FakeInMemoryDataSource, actualDatasource)
    }

    @Test
    fun `필드 주입 시 Qualifier로 구분해 Room 사용하는 데이터소스를 주입한다`() {
        // when
        val vm = injector.inject(FakeViewModelWithFieldInjectionAndQualifier::class)

        // then
        val actualRepository = vm.fakeCartInMemoryRepository
        assertSame(FakeCartInMemoryRepository, actualRepository)
        assertNotSame(FakeCartDefaultRepository, actualRepository)
    }

    @Test
    fun `싱글톤 어노테이션을 붙이면 인스턴스가 싱글톤이다`() {
        // given
        val vm1 = injector.inject(FakeViewModelWithSingletonRepository::class)
        val repositoryBefore = vm1.fakeRepository

        // when
        val vm2 = injector.inject(FakeViewModelWithSingletonRepository::class)
        val repositoryAfter = vm2.fakeRepository

        // then
        assertSame(repositoryAfter, repositoryBefore)
    }

    @Test
    fun `싱글톤 어노테이션이 없으면 인스턴스가 새로 생성된다`() {
        // given
        val vm1 = injector.inject(FakeViewModelWithDisposableRepository::class)
        val repositoryBefore = vm1.fakeRepository

        // when
        val vm2 = injector.inject(FakeViewModelWithDisposableRepository::class)
        val repositoryAfter = vm2.fakeRepository

        // then
        assertNotSame(repositoryAfter, repositoryBefore)
    }
}

class FakeViewModel(
    @Inject val productFakeRepository: ProductFakeRepository,
    @Inject val cartFakeRepository: FakeCartRepository,
)

class FakeViewModelMissingAnnotation(
    @Inject val productFakeRepository: ProductFakeRepository,
    val cartFakeRepository: FakeCartDefaultRepository,
)

class FakeViewModelWithDefaultValue(
    @Inject val productFakeRepository: ProductFakeRepository,
    val someString: String = "",
)

class FakeViewModelWithFieldInjection() {
    @Inject
    lateinit var cartFakeRepository: FakeCartRepository
}

class FakeViewModelWithFieldInjectionAndQualifier() {
    @Inject
    @InMemory
    lateinit var fakeCartInMemoryRepository: FakeCartRepository
}

class FakeViewModelWithRecursiveDI(
    @Inject val fakeRepositoryWithDataSource: FakeRepositoryWithDataSource,
)

class FakeViewModelWithSingletonRepository(
    @Inject val fakeRepository: FakeSingletonRepository,
)

class FakeViewModelWithDisposableRepository(
    @Inject val fakeRepository: FakeDisposableRepository,
)
