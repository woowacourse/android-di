package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.fixture.FakeRepository
import woowacourse.shopping.fixture.FakeRepository1
import woowacourse.shopping.fixture.FakeRepository2

class DependencyInjectorTest {
    private lateinit var container: Container
    private lateinit var injector: DependencyInjector

    private val fakeRepository1 = FakeRepository1()
    private val fakeRepository2 = FakeRepository2()

    @Before
    fun setup() {
        container = Container()
        injector = DependencyInjector(container)
    }

    @Test
    fun `생성자 주입 - 의존성이 모두 등록되어 있으면 의존성 주입이 성공한다`() {
        // given
        class TestClass(
            val repo1: FakeRepository1,
            val repo2: FakeRepository2,
        )
        container.registerProvider(FakeRepository1::class) { fakeRepository1 }
        container.registerProvider(FakeRepository2::class) { fakeRepository2 }

        // when
        val instance = injector.create(TestClass::class, TestClass::class.java.name)

        // then
        assertThat(instance.repo1).isEqualTo(fakeRepository1)
        assertThat(instance.repo2).isEqualTo(fakeRepository2)
    }

    @Test
    fun `생성자 주입 - Qualifier로 올바른 구현체가 주입된다`() {
        // given
        class TestClass(
            @Qualifier("repo1")
            val repo1: FakeRepository,
            @Qualifier("repo2")
            val repo2: FakeRepository,
        )

        container.registerProvider(FakeRepository::class, "repo1") { fakeRepository1 }
        container.registerProvider(FakeRepository::class, "repo2") { fakeRepository2 }

        // when
        val instance = injector.create(TestClass::class, TestClass::class.java.name)

        // then
        assertThat(instance.repo1).isEqualTo(fakeRepository1)
        assertThat(instance.repo2).isEqualTo(fakeRepository2)
    }

    @Test
    fun `생성자 주입 - 의존성이 등록되어 있지 않으면 의존성 주입이 실패한다`() {
        // given
        class TestClass(
            val repo1: FakeRepository1,
        )

        // when & then
        Assert.assertThrows(IllegalArgumentException::class.java) {
            injector.create(TestClass::class, TestClass::class.java.name)
        }
    }

    @Test
    fun `필드 주입 - 의존성이 모두 등록되어 있으면 의존성 주입이 성공한다`() {
        // given
        class TestClass {
            @Inject
            lateinit var repo1: FakeRepository1

            @Inject
            lateinit var repo2: FakeRepository2

            fun isRepo1Initialized(): Boolean = ::repo1.isInitialized

            fun isRepo2Initialized(): Boolean = ::repo2.isInitialized
        }
        container.registerProvider(FakeRepository1::class) { fakeRepository1 }
        container.registerProvider(FakeRepository2::class) { fakeRepository2 }

        // when
        val instance = injector.create(TestClass::class, TestClass::class.java.name)

        // then
        assertThat(instance.isRepo1Initialized()).isTrue
        assertThat(instance.isRepo2Initialized()).isTrue
        assertThat(instance.repo1).isEqualTo(fakeRepository1)
        assertThat(instance.repo2).isEqualTo(fakeRepository2)
    }

    @Test
    fun `필드 주입 - Qualifier로 올바른 구현체가 주입된다`() {
        // given
        class TestClass {
            @Inject
            @Qualifier("repo1")
            lateinit var repo1: FakeRepository

            @Inject
            @Qualifier("repo2")
            lateinit var repo2: FakeRepository

            fun isInMemoryInitialized(): Boolean = ::repo1.isInitialized

            fun isDatabaseInitialized(): Boolean = ::repo2.isInitialized
        }

        container.registerProvider(FakeRepository::class, "repo1") { fakeRepository1 }
        container.registerProvider(FakeRepository::class, "repo2") { fakeRepository2 }

        // when
        val instance = injector.create(TestClass::class, TestClass::class.java.name)

        // then
        assertThat(instance.isInMemoryInitialized()).isTrue
        assertThat(instance.isDatabaseInitialized()).isTrue
        assertThat(instance.repo1).isEqualTo(fakeRepository1)
        assertThat(instance.repo2).isEqualTo(fakeRepository2)
    }

    @Test
    fun `필드 주입 - 의존성이 등록되어 있지 않으면 의존성 주입이 실패한다`() {
        // given
        class TestClass {
            @Inject
            lateinit var repo1: FakeRepository1

            @Inject
            lateinit var repo2: FakeRepository2

            fun isRepo1Initialized(): Boolean = ::repo1.isInitialized

            fun isRepo2Initialized(): Boolean = ::repo2.isInitialized
        }
        container.registerProvider(FakeRepository1::class) { fakeRepository1 }

        // when
        val instance = injector.create(TestClass::class, TestClass::class.java.name)

        // then
        assertThat(instance.isRepo1Initialized()).isTrue
        assertThat(instance.isRepo2Initialized()).isFalse
    }

    @Test
    fun `혼합 주입 - 생성자와 필드 주입을 동시에 사용해도 의존성 주입이 성공한다`() {
        // given
        class TestClass(
            val repo1: FakeRepository1,
        ) {
            @Inject
            lateinit var repo2: FakeRepository2
        }
        container.registerProvider(FakeRepository1::class) { fakeRepository1 }
        container.registerProvider(FakeRepository2::class) { fakeRepository2 }

        // when
        val instance = injector.create(TestClass::class, TestClass::class.java.name)

        // then
        assertThat(instance.repo1).isEqualTo(fakeRepository1)
        assertThat(instance.repo2).isEqualTo(fakeRepository2)
    }
}
