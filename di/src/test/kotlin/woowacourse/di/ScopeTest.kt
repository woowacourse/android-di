package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class ScopeTest {
    private val screen = ScopeType("screen")
    private val model = ScopeType("model")

    @Test
    fun `같은 스코프에서는 재사용하고 다른 스코프에서는 따로 생성한다`() {
        val root = injector { scoped<Resource>(screen) }
        val first = root.openScope("first", screen)
        val second = root.openScope("second", screen)

        assertThat(root.openScope("first", screen)).isSameAs(first)
        assertThat(first.get<Resource>()).isSameAs(first.get<Resource>())
        assertThat(second.get<Resource>()).isNotSameAs(first.get<Resource>())
        root.close()
    }

    @Test
    fun `자식은 부모와 앱 의존성을 공유하고 자신의 의존성만 따로 가진다`() {
        val root =
            injector {
                singleton<AppResource> { AppResource() }
                scoped<Resource>(screen)
                scoped<ModelResource>(model)
            }
        val destination = root.openScope("cart", screen)
        val first = destination.openScope("model1", model)
        val second = destination.openScope("model2", model)

        assertThat(first.get<Resource>()).isSameAs(destination.get<Resource>())
        assertThat(first.get<AppResource>()).isSameAs(root.get<AppResource>())
        assertThat(first.get<ModelResource>()).isNotSameAs(second.get<ModelResource>())
        first.close()
        assertThat(destination.isClosed).isFalse()
        assertThat(second.get<AppResource>()).isSameAs(root.get<AppResource>())
        root.close()
    }

    @Test
    fun `종료하면 캐시와 자식 참조를 제거하고 정리 콜백을 한 번 실행한다`() {
        var closed = 0
        val root = injector { scoped<Resource>(screen, onClose = { closed++ }) }
        val scope = root.openScope("cart", screen)
        scope.get<Resource>()
        assertThat(scope.instanceCount).isEqualTo(1)

        scope.close()
        scope.close()

        assertThat(scope.instanceCount).isZero()
        assertThat(root.childCount).isZero()
        assertThat(closed).isEqualTo(1)
        assertThatThrownBy { scope.get<Resource>() }.hasMessageContaining("이미 닫힌 스코프")
        assertThatThrownBy { scope.openScope("child", model) }.hasMessageContaining("이미 닫힌 스코프")
        root.close()
    }

    @Test
    fun `같은 화면 키로 재진입해도 종료된 인스턴스를 되살리지 않는다`() {
        val root = injector { scoped<Resource>(screen) }
        val oldScope = root.openScope("cart", screen)
        val old = oldScope.get<Resource>()
        oldScope.close()

        val newScope = root.openScope("cart", screen)
        assertThat(newScope).isNotSameAs(oldScope)
        assertThat(newScope.get<Resource>()).isNotSameAs(old)
        oldScope.close()
        assertThat(root.openScope("cart", screen)).isSameAs(newScope)
        root.close()
    }

    @Test
    fun `반복 진입과 이탈 이후 인스턴스와 스코프가 누적되지 않는다`() {
        var created = 0
        var closed = 0
        val root =
            injector {
                scoped<Resource>(screen, onClose = { closed++ }) {
                    created++
                    Resource()
                }
            }
        repeat(100) {
            val scope = root.openScope("cart-$it", screen)
            scope.get<Resource>()
            scope.close()
            assertThat(scope.instanceCount).isZero()
            assertThat(root.childCount).isZero()
        }
        assertThat(created).isEqualTo(100)
        assertThat(closed).isEqualTo(created)
        root.close()
    }

    @Test
    fun `앱 종료는 모든 자식과 앱 인스턴스를 함께 해제한다`() {
        var closed = 0
        val root =
            injector {
                singleton<AppResource>(onClose = { closed++ }) { AppResource() }
                scoped<Resource>(screen, onClose = { closed++ })
                scoped<ModelResource>(model, onClose = { closed++ })
            }
        val destination = root.openScope("cart", screen)
        val viewModel = destination.openScope("viewModel", model)
        viewModel.get<AppResource>()
        viewModel.get<Resource>()
        viewModel.get<ModelResource>()

        root.close()

        assertThat(closed).isEqualTo(3)
        for (scope in listOf(root, destination, viewModel)) {
            assertThat(scope.isClosed).isTrue()
            assertThat(scope.instanceCount).isZero()
            assertThat(scope.childCount).isZero()
        }
    }

    @Test
    fun `앱 객체가 수명이 짧은 화면 객체를 붙잡지 못하게 한다`() {
        val root =
            injector {
                scoped<Resource>(screen)
                singleton<Holder> { Holder(get()) }
            }
        val destination = root.openScope("cart", screen)

        assertThatThrownBy { destination.get<Holder>() }.hasMessageContaining("활성 스코프가 필요합니다: screen")
        assertThat(root.instanceCount).isZero()
        root.close()
    }

    @Test
    fun `외부에서 새 스코프 종류를 등록하고 생성과 소멸을 관리한다`() {
        val session = ScopeType("checkout-session")
        var closed = false
        val root = injector { scoped<Resource>(session, onClose = { closed = true }) }
        assertThatThrownBy { root.get<Resource>() }.hasMessageContaining("checkout-session")
        val scope = root.openScope("payment", session)
        scope.get<Resource>()
        scope.close()
        assertThat(closed).isTrue()
        root.close()
    }

    @Test
    fun `정리 콜백이 실패해도 나머지 참조와 리소스를 정리한다`() {
        var released = false
        val root =
            injector {
                scoped<Resource>(screen, onClose = { released = true })
                scoped<ModelResource>(screen, onClose = { error("정리 실패") })
            }
        val scope = root.openScope("cart", screen)
        scope.get<Resource>()
        scope.get<ModelResource>()

        assertThatThrownBy { scope.close() }.hasMessage("정리 실패")
        assertThat(released).isTrue()
        assertThat(scope.instanceCount).isZero()
        assertThat(root.childCount).isZero()
        scope.close()
        root.close()
    }

    @Test
    fun `같은 스코프 ID에 다른 타입을 연결할 수 없다`() {
        val root = Injector()
        root.openScope("same", screen)
        assertThatThrownBy { root.openScope("same", model) }.hasMessageContaining("다른 타입")
        root.close()
    }

    class Resource

    class AppResource

    class ModelResource

    class Holder(
        val resource: Resource,
    )
}
