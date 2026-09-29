package woowacourse.shopping.di

import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.annotation.Qualifier

class DIContainerQualifierTest {
    @Before
    fun setUp() {
        DIContainer.clear()
    }

    @Test
    fun `Qualifier를 지정하면 해당 구현체를 생성한다`() {
        // given
        DIContainer.bind(
            type = TestRepository::class,
            implementation = RoomTestRepository::class,
            qualifier = TestRoom::class,
        )
        DIContainer.bind(
            type = TestRepository::class,
            implementation = InMemoryRepository::class,
            qualifier = TestInMemory::class,
        )

        // when
        val repository =
            DIContainer.createInstance(
                TestRepository::class,
                TestRoom::class,
            )

        // then
        assertTrue(repository is RoomTestRepository)
    }

    @Test
    fun `다른 Qualifier를 지정하면 다른 구현체를 생성한다`() {
        // given
        DIContainer.bind(
            type = TestRepository::class,
            implementation = RoomTestRepository::class,
            qualifier = TestRoom::class,
        )
        DIContainer.bind(
            type = TestRepository::class,
            implementation = InMemoryRepository::class,
            qualifier = TestInMemory::class,
        )

        // when
        val repository =
            DIContainer.createInstance(
                TestRepository::class,
                TestInMemory::class,
            )

        // then
        assertTrue(repository is InMemoryRepository)
    }

    @Test
    fun `동일 타입 구현체가 여러 개이고 Qualifier가 없으면 예외가 발생한다`() {
        // given
        DIContainer.bind(
            type = TestRepository::class,
            implementation = RoomTestRepository::class,
            qualifier = TestRoom::class,
        )
        DIContainer.bind(
            type = TestRepository::class,
            implementation = InMemoryRepository::class,
            qualifier = TestInMemory::class,
        )

        // when
        val exception =
            assertThrows(
                IllegalArgumentException::class.java,
            ) {
                DIContainer.createInstance(TestRepository::class)
            }

        // then
        assertTrue(
            exception.message!!.contains("Qualifier가 필요해요"),
        )
    }

    @Test
    fun `등록되지 않은 Qualifier를 요청하면 예외가 발생한다`() {
        // given
        DIContainer.bind(
            type = TestRepository::class,
            implementation = RoomTestRepository::class,
            qualifier = TestRoom::class,
        )

        // when
        val exception =
            assertThrows(
                IllegalArgumentException::class.java,
            ) {
                DIContainer.createInstance(
                    TestRepository::class,
                    TestInMemory::class,
                )
            }

        // then
        assertTrue(
            exception.message!!.contains("등록되지 않은 Qualifier예요"),
        )
    }

    interface TestRepository

    class RoomTestRepository : TestRepository

    class InMemoryRepository : TestRepository

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestRoom

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TestInMemory
}
