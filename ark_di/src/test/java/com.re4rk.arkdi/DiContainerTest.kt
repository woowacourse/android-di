package woowacourse.shopping.di

import com.re4rk.arkdi.ArkInject
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.Qualifier
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DiContainerTest {
    interface FakeDiRepository {
        fun get(): String
    }

    interface FakeDiDataSource {
        fun get(): String
    }

    class FakeViewModel @ArkInject constructor(
        private val diRepository: FakeDiRepository,
    ) {
        fun get(): String {
            return diRepository.get()
        }
    }

    class FakeDiProtoTypeRepository @ArkInject constructor(
        private val diDataSource: FakeDiDataSource,
    ) : FakeDiRepository {
        override fun get(): String {
            return diDataSource.get()
        }
    }

    class FakeDiSingletonRepository : FakeDiRepository {
        override fun get(): String {
            return "FakeDiSingletonRepository"
        }
    }

    class FakeDiProtoTypeDataSource : FakeDiDataSource {
        override fun get(): String {
            return "FakeDiProtoTypeDataSource"
        }
    }

    private class FakeDiContainer : DiContainer() {
        fun provideFakeDiDataSource(): FakeDiDataSource =
            this.createInstance(FakeDiProtoTypeDataSource::class)

        fun provideFakeDiRepository(): FakeDiRepository =
            this.createInstance(FakeDiProtoTypeRepository::class)
    }

    private val fakeDiContainer = FakeDiContainer()

    @Test
    fun `DiContainer안에 있는 객체를 반환한다`() {
        // given & when
        val fakeDiRepository = fakeDiContainer.getInstance(FakeDiRepository::class)

        // then
        assertTrue(fakeDiRepository is FakeDiProtoTypeRepository)
    }

    @Test
    fun `DiContainer안에 있는 객체를 반환한다 2`() {
        // given & when
        val fakeDiDataSource = fakeDiContainer.getInstance(FakeDiDataSource::class)

        // then
        assertTrue(fakeDiDataSource is FakeDiProtoTypeDataSource)
    }

    @Test
    fun `첫번째 생성자 파라미터가 있으면 자동으로 주입하고 객체를 반환한다`() {
        // given & when
        val fakeDiRepository = fakeDiContainer.createInstance(FakeViewModel::class)

        // then
        assertTrue(fakeDiRepository is FakeViewModel)
    }

    @Test
    fun `의존성 부여 순서는 상관 없다`() {
        // given
        val fakeDiObject = object : DiContainer() {
            fun provideFakeDiRepository(): FakeDiRepository =
                this.createInstance(FakeDiProtoTypeRepository::class)

            fun provideFakeDiDataSource(): FakeDiDataSource =
                this.createInstance(FakeDiProtoTypeDataSource::class)
        }

        // when
        val fakeDiDataSource = fakeDiObject.getInstance(FakeDiDataSource::class)

        // then
        assertTrue(fakeDiDataSource is FakeDiProtoTypeDataSource)
    }

    @Test
    fun `DiContainer에서 없는 리포지터리 객체를 요청하면 예외를 발생시킨다`() {
        // given
        class MockRepository

        // when
        runCatching { fakeDiContainer.getInstance(MockRepository::class) }
            // then
            .onSuccess { assertEquals(it, null) }
            .onFailure { assertTrue(it is IllegalArgumentException) }
    }

    @Test
    fun `@ArkInject가 있는 생성자를 찾아서 객체를 생성한다`() {
        // given
        class FakeDiInjectRepository @ArkInject constructor(
            fakeDiInjectDataSource: FakeDiDataSource,
        ) : FakeDiRepository {
            override fun get(): String {
                return "FakeDiInjectRepository"
            }
        }

        // when
        val result = runCatching { fakeDiContainer.createInstance(FakeDiInjectRepository::class) }

        // then
        assertThat(result.isSuccess).isTrue

        // and
        assertThat(result.getOrThrow()).isInstanceOf(FakeDiInjectRepository::class.java)
    }

    @Test
    fun `@ArkInject가 있는 생성자는 주 생성자가 아니어도 된다`() {
        // given
        class FakeViewModel constructor(fakeDiInjectRepository: FakeDiRepository) {
            @ArkInject
            constructor(fakeDiInjectDataSource: FakeDiDataSource) :
                this(FakeDiProtoTypeRepository(fakeDiInjectDataSource))

            fun get(): String {
                return "FakeDiInjectRepository"
            }
        }

        // when
        val result = runCatching { fakeDiContainer.createInstance(FakeViewModel::class) }

        // then
        assertThat(result.isSuccess).isTrue

        // and
        assertThat(result.getOrThrow()).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `@ArkInject가 있는 생성자가 없으면 기본 생성자 찾고 기본 옵션 파라미터 이외에 것이 있으면 에러가 발생된다`() {
        // given
        class FakeDiInjectRepository(fakeDiInjectDataSource: FakeDiDataSource) :
            FakeDiRepository {
            override fun get(): String = "FakeDiInjectRepository"
        }

        // when
        val result = runCatching { fakeDiContainer.createInstance(FakeDiInjectRepository::class) }

        // then
        assertThat(result.isFailure).isTrue
    }

    @Test
    fun `@ArkInject가 있는 생성자가 없으면 기본 생성자를 찾는다`() {
        // given
        class FakeDiInjectRepository : FakeDiRepository {
            override fun get(): String = "FakeDiInjectRepository"
        }

        // when
        val result = runCatching { fakeDiContainer.createInstance(FakeDiInjectRepository::class) }

        // then
        assertThat(result.isSuccess).isTrue

        // and
        assertThat(result.getOrThrow()).isInstanceOf(FakeDiInjectRepository::class.java)
    }

    @Test
    fun `@ArkInject가 있는 생성자가 두개 이상 이면 예외를 발생시킨다`() {
        // given
        class FakeViewModel @ArkInject constructor(fakeDiInjectRepository: FakeDiRepository) {
            @ArkInject
            constructor(fakeDiInjectDataSource: FakeDiDataSource) :
                this(FakeDiProtoTypeRepository(fakeDiInjectDataSource))

            fun get(): String {
                return "FakeDiInjectRepository"
            }
        }

        // when
        val result = runCatching { fakeDiContainer.createInstance(FakeViewModel::class) }

        // then
        assertThat(result.isFailure).isTrue
    }

    @Test
    fun `어노테이션을 통해 의존성을 주입할 수 있다`() {
        // given
        val fakeDiObject = object : DiContainer() {
            @Qualifier("fakeDiProtoTypeRepository")
            fun provideFakeDiRepository(): FakeDiRepository = FakeDiSingletonRepository()
        }

        class FakeViewModel @ArkInject constructor(
            @Qualifier("fakeDiProtoTypeRepository") val fakeDiRepository: FakeDiRepository,
        ) {
            fun get(): String = fakeDiRepository.get()
        }

        // when
        val result = runCatching { fakeDiObject.createInstance(FakeViewModel::class) }
        val viewModel = result.getOrThrow()

        // then
        assertThat(result.isSuccess).isTrue
        assertThat(viewModel.get()).isEqualTo("FakeDiSingletonRepository")
    }

    @Test
    fun `어노테이션을 통해 의존성을 주입할 때 어노테이션의 이름이 다르면 예외를 발생시킨다`() {
        // given
        val fakeDiObject = object : DiContainer() {
            @Qualifier("fakeDiSingltonRepository")
            fun provideFakeDiRepository(): FakeDiRepository = FakeDiSingletonRepository()
        }

        class FakeViewModel @ArkInject constructor(
            @Qualifier("fakeDiProtoTypeRepository") private val fakeDiRepository: FakeDiRepository,
        ) {
            fun get(): String {
                return fakeDiRepository.get()
            }
        }

        // when
        val result = runCatching { fakeDiContainer.createInstance(FakeViewModel::class) }

        // then
        assertThat(result.isFailure).isTrue
    }

    @Test
    fun `같은 타입이면 어노테이션으로 구분한다`() {
        // given
        val fakeDiObject = object : DiContainer() {
            @Qualifier("fakeDiProtoTypeRepository")
            fun provideFakeDiRepository(
                fakeDiDataSource: FakeDiDataSource,
            ): FakeDiRepository =
                FakeDiProtoTypeRepository(fakeDiDataSource)

            fun provideFakeDiDataSource(): FakeDiDataSource =
                FakeDiProtoTypeDataSource()

            @Qualifier("fakeDiSingletonRepository")
            fun provideFakeDiSingletonRepository(): FakeDiRepository =
                FakeDiSingletonRepository()
        }

        class FakeViewModel @ArkInject constructor(
            @Qualifier("fakeDiProtoTypeRepository") val fakeDiRepository: FakeDiRepository,
            @Qualifier("fakeDiSingletonRepository") val fakeDiSingletonRepository: FakeDiRepository,
        ) {
            fun get(): String {
                return fakeDiRepository.get()
            }
        }

        // when
        val result = runCatching { fakeDiObject.createInstance(FakeViewModel::class) }
        val viewModel = result.getOrThrow()

        // then
        assertThat(result.isSuccess).isTrue

        // and
        assertThat(viewModel.fakeDiRepository).isInstanceOf(FakeDiProtoTypeRepository::class.java)
        assertThat(viewModel.fakeDiSingletonRepository).isInstanceOf(FakeDiSingletonRepository::class.java)
    }
}
