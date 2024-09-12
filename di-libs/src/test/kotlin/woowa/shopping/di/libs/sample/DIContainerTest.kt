package woowa.shopping.di.libs.sample

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.container
import woowa.shopping.di.libs.container.startDI
import woowa.shopping.di.libs.inject.inject

class DIContainerTest {
    class Service

    interface Repository
    class RepositoryImpl(val service: Service) : Repository

    @AfterEach
    fun tearDown() {
        Containers.clearContainersForTest()
    }

    @Test
    fun `singleton 객체인지 테스트`() {
        //given
        startDI {
            single { Service() }
        }
        //when
        val service1 by inject<Service>()
        val service2 by inject<Service>()
        //then
        service1 shouldBe service2
    }

    @Test
    fun `프로토타입 객체인지 테스트`() {
        //given
        startDI {
            proto { Service() }
        }
        // when
        val service1 by inject<Service>()
        val service2 by inject<Service>()
        //then
        service1 shouldNotBe service2
    }

    @Test
    fun `Respository 싱글톤 구현체`() {
        //given
        startDI {
            single { Service() }
            single<Repository> { RepositoryImpl(get()) }
        }
        // when
        val repository1 by inject<Repository>()
        val repository2 by inject<Repository>()
        //then
        repository1 shouldBe repository2
    }

    @Test
    fun `startDI 이후 컨테이너 추가 시 에러 발생 테스트`() {
        // given
        startDI {
            single { Service() }
        }
        // when & then
        shouldThrow<IllegalStateException> {
            container {
                single { Service() }
                single { RepositoryImpl(get()) }
            }
        }
    }

    @Test
    fun `container 에 추가된 객체는 Lazy하게 생성되기에, 등록 순서가 바뀌어도 상관 없다`() {
        // given
        startDI {
            container {
                single<Repository> { RepositoryImpl(get()) }
                single { Service() }
            }
        }
        // then
        val repository by inject<Repository>()
        repository shouldNotBe null
    }
}
