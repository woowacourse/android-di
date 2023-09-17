package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

interface FakeDataSource

interface FakeRepository

class DefaultFakeDataSource : FakeDataSource

// 주 생성자에 주입할 객체가 있는 Repository 구현체
class InDiskFakeRepository(
    fakeDataSource: FakeDataSource,
) : FakeRepository

// 추상화된 Repository 구현체 B (동일한 인터페이스를 가질 때 Qualifier로 구분)
class InMemoryFakeRepository : FakeRepository

// 생성자 주입이 필요없는 구현체 1, 주입이 필요한 구현체 1을 생성자로 받는 ViewModel
class FakeDoubleViewModel(
    fakeDataSource: FakeDataSource,
    fakeRepository: FakeRepository,
)

// 재귀(InDiskFakeRepository)
class FakeInDiskViewModel(
    @Qualifier(InDiskFakeRepository::class)
    val fakeRepository: FakeRepository,
)

// 필드 주입
class FakeFieldInjectViewModel() {
    @Inject
    lateinit var fakeRepository: FakeRepository
}

class FakeQualifierModule : Module {
    fun provideDefaultDataSource(): FakeDataSource = DefaultFakeDataSource()

    // 사용하진 않지만 모듈에서 동일한 추상 객체를 반환하는 경우 Qualifier 를 통해 구분함
    fun provideInMemoryFakeRepository(): FakeRepository = InMemoryFakeRepository()

    fun provideInDiskRepository(fakeDataSource: FakeDataSource): FakeRepository =
        InDiskFakeRepository(fakeDataSource)
}

class FakeModule : Module {
    fun provideDefaultDataSource(): FakeDataSource = DefaultFakeDataSource()

    fun provideInDiskRepository(fakeDataSource: FakeDataSource): FakeRepository =
        InDiskFakeRepository(fakeDataSource)
}

class InjectorTest {
    private lateinit var injector: Injector

    @Before
    fun setup() {
        injector = Injector(FakeModule())
    }

    @Test
    fun `주 생성자가 없으면 NullPointException이 발생한다`() {
        val actualException = assertThrows(NullPointerException::class.java) {
            injector.inject(FakeRepository::class.java)
        }
        assertEquals("[ERROR] 주 생성자가 없습니다.", actualException.message)
    }

    @Test
    fun `추상화된 주 생성자 하나가 있는 InDiskFakeRepository의 DataSource 의존성을 주입한다`() {
        val instance = injector.inject(InDiskFakeRepository::class.java)
        assertThat(instance).isInstanceOf(InDiskFakeRepository::class.java)
    }

    @Test
    fun `생성자가 있는 클래스와 디폴트 생성자만 존재하는 클래스를 주 생성자로 갖는 클래스의 의존성을 주입한다`() {
        val instance = injector.inject(FakeDoubleViewModel::class.java)
        assertThat(instance).isInstanceOf(FakeDoubleViewModel::class.java)
    }

    @Test
    fun `@Inject 어노테이션이 붙은 필드를 갖는 클래스의 의존성을 주입한다`() {
        val instance = injector.inject(FakeFieldInjectViewModel::class.java)
        assertThat(instance).isInstanceOf(FakeFieldInjectViewModel::class.java)
    }

    @Test
    fun `추상화 된 객체를 @Qualifier 어노테이션으로 구분하여 FakeInDiskViewModel 의존성을 주입한다`() {
        // given
        // 어노테이션 Qualifier 테스트를 위한 모듈 적용
        injector = Injector(FakeQualifierModule())

        // when
        val instance = injector.inject(FakeInDiskViewModel::class.java)

        // then
        assertThat(instance).isInstanceOf(FakeInDiskViewModel::class.java)
        // 실제로 주입된 객체가 어노테이션으로 지정한 InDiskFakeRepository 클래스가 맞는지 검증
        assertThat(instance.fakeRepository).isInstanceOf(InDiskFakeRepository::class.java)
    }
}
