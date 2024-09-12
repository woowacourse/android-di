package woowa.shopping.di.libs.sample

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.startDI
import woowa.shopping.di.libs.factory.Lifecycle
import woowa.shopping.di.libs.inject.inject
import woowa.shopping.di.libs.qualify.qualifier

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
            container {
                single { Service() }
            }
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
            container {
                proto { Service() }
            }
        }
        // when
        val service1 by inject<Service>(lifecycle = Lifecycle.PROTOTYPE)
        val service2 by inject<Service>(lifecycle = Lifecycle.PROTOTYPE)
        //then
        service1 shouldNotBe service2
    }

    @Test
    fun `Respository 싱글톤 구현체`() {
        //given
        startDI {
            container {
                single { Service() }
                single<Repository> { RepositoryImpl(get()) }
            }
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
            container {
                single { Service() }
            }
        }
        // when & then
        shouldThrow<IllegalStateException> {
            startDI {
                container {
                    single { Service() }
                }
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

    @Test
    fun `중복되는 타입과 Qualifier로 객체를 가져올 때 에러 발생`() {
        // given
        shouldThrow<IllegalStateException> {
            startDI {
                container {
                    single(qualifier("오둥")) { Service() }
                }
                container {
                    single(qualifier("오둥")) { Service() }
                }
            }
        }
    }

    @Test
    fun `Qualifier로 원하는 객체 가져오기`() {
        // given
        startDI {
            container {
                single { Service() }
                single<Repository>(qualifier("안녕")) { RepositoryImpl(get()) }
            }

            container {
                single<Repository>(qualifier("오둥아")) { RepositoryImpl(get()) }
            }
        }
        // when
        val repository1 by inject<Repository>(qualifier("안녕"))
        val repository2 by inject<Repository>(qualifier("오둥아"))
        // then
        repository1 shouldNotBe repository2
    }

    @Test
    fun `Qualifier로 원하는 객체 가져오기2`() {
        // given
        startDI {
            container {
                single(qualifier("둥가둥가")) { Service() }
                single(qualifier("오둥이")) { RepositoryImpl(get(qualifier("둥가둥가"))) }
            }
        }
        // when
        val repository1 by inject<RepositoryImpl>(qualifier("오둥이"))
        val service by inject<Service>(qualifier("둥가둥가"))
        // then
        repository1.service shouldBe service
    }
}
