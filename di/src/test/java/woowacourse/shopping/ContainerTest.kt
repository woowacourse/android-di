package woowacourse.shopping

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ContainerTest {
    private lateinit var container: Container

    @BeforeEach
    fun setUp() {
        container = Container()
    }

    @DisplayName("컨테이너에 존재하는 인스턴스를 반환한다")
    @Test
    fun getTest() {
        // given
        container.bind(TestRepository::class, ::InMemoryTestRepository)

        // when
        val instance1 = container.get<TestRepository>()
        val instance2 = container.get<TestRepository>()

        assertSoftly {
            instance1.shouldNotBeNull()
            instance2.shouldNotBeNull()
            instance1.shouldBeInstanceOf<InMemoryTestRepository>()
            instance2.shouldBeInstanceOf<InMemoryTestRepository>()
            instance1 shouldNotBeSameInstanceAs instance2
        }
    }

    @DisplayName("Qualifier가 일치하는 인스턴스를 반환한다")
    @Test
    fun getInstanceWithQualifier() {
        // given
        container.bind(TestRepository::class, ::InMemoryTestRepository, QualifierA::class)
        container.bind(TestRepository::class, ::RoomTestRepository, QualifierB::class)

        // when
        val inMemoryInstance = container.get(TestRepository::class, QualifierA::class)
        val roomInstance = container.get(TestRepository::class, QualifierB::class)

        assertSoftly {
            inMemoryInstance.shouldBeTypeOf<InMemoryTestRepository>()
            roomInstance.shouldBeTypeOf<RoomTestRepository>()
            inMemoryInstance.getData() shouldBe "InMemory"
            roomInstance.getData() shouldBe "Room"
        }
    }

    @DisplayName("@Inject 생성자가 있는 클래스는 의존성이 주입된다")
    @Test
    fun injectConstructorTest() {
        // given
        container.bind(TestRepository::class, ::InMemoryTestRepository)

        // when
        val service = container.get<TestService>()

        // when
        assertSoftly {
            service.shouldNotBeNull()
            service.getServiceData() shouldBe "InMemory"
        }
    }

    @DisplayName("필드에 @Inject 어노테이션이 있는 클래스는 의존성이 주입된다")
    @Test
    fun fieldInjectTest() {
        // given
        container.bind(TestRepository::class, ::InMemoryTestRepository)

        // when
        val service = container.get<FieldInjectedService>()

        // when
        assertSoftly {
            service.shouldNotBeNull()
            service.getInjectedData() shouldBe "InMemory"
        }
    }

    @DisplayName("@Singleton 어노테이션이 있는 클래스는 싱글톤으로 관리된다")
    @Test
    fun singletonTest() {
        // when
        val instance1 = container.get<AnnotatedSingletonService>()
        val instance2 = container.get<AnnotatedSingletonService>()

        // then
        instance1 shouldBeSameInstanceAs instance2
    }

    @DisplayName("순환 참조가 발생하면 DependencyCycleException이 발생한다")
    @Test
    fun dependencyCycleTest() {
        // when
        // then
        shouldThrow<DependencyCycleException> { container.get<CycleA>() }
    }

    @DisplayName("의존성을 찾을 수 없으면 NoProviderException이 발생한다")
    @Test
    fun noProviderTest() {
        // when
        // then
        shouldThrow<NoProviderException> { container.get<TestService>() }
    }

    @DisplayName("모듈을 통해 컨테이너에 인스턴스를 등록한다")
    @Test
    fun installModuleTest() {
        // given
        container.installModule(TestModule)

        // when
        val roomInstance = container.get(TestRepository::class, QualifierA::class)

        // then
        roomInstance.shouldBeTypeOf<RoomTestRepository>()
    }

    @DisplayName("@Singleton이 붙은 메소드는 생성된 인스턴스가 싱글톤으로 관리된다")
    @Test
    fun installModuleSingletonTest() {
        // given
        container.installModule(TestModule)

        // when
        val instance1 = container.get(TestRepository::class)
        val instance2 = container.get(TestRepository::class)

        // then
        instance1 shouldBeSameInstanceAs instance2
    }
}
