package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DIScopeManagerTest {
    class DummyRepository

    annotation class QualifierA

    annotation class QualifierB

    @BeforeEach
    fun setup() {
        DIScopeManager.clearScope(ScopeType.Singleton)
        DIScopeManager.clearScope(ScopeType.Activity)
        DIScopeManager.clearScope(ScopeType.ViewModel)
    }

    @Test
    fun `putInstance 후 getInstance 로 같은 객체를 가져온다`() {
        // given
        val repo = DummyRepository()
        val key = DummyRepository::class to null

        // when
        DIScopeManager.putInstance(ScopeType.Singleton, key, repo)

        // then
        val retrieved = DIScopeManager.getInstance(ScopeType.Singleton, key)
        assertThat(repo).isEqualTo(retrieved)
    }

    @Test
    fun `같은 클래스여도 다른 Qualifier면 별도로 관리된다`() {
        // given
        val repoA = DummyRepository()
        val repoB = DummyRepository()

        val keyA = DummyRepository::class to QualifierA::class
        val keyB = DummyRepository::class to QualifierB::class

        // when
        DIScopeManager.putInstance(ScopeType.Activity, keyA, repoA)
        DIScopeManager.putInstance(ScopeType.Activity, keyB, repoB)

        // then
        assertThat(repoA).isEqualTo(DIScopeManager.getInstance(ScopeType.Activity, keyA))
        assertThat(repoB).isEqualTo(DIScopeManager.getInstance(ScopeType.Activity, keyB))
    }

    @Test
    fun `다른 ScopeType 은 서로 간섭하지 않는다`() {
        // given
        val singletonRepo = DummyRepository()
        val viewModelRepo = DummyRepository()
        val key = DummyRepository::class to null

        // when
        DIScopeManager.putInstance(ScopeType.Singleton, key, singletonRepo)
        DIScopeManager.putInstance(ScopeType.ViewModel, key, viewModelRepo)

        // then
        assertThat(singletonRepo).isEqualTo(DIScopeManager.getInstance(ScopeType.Singleton, key))
        assertThat(viewModelRepo).isEqualTo(DIScopeManager.getInstance(ScopeType.ViewModel, key))
    }

    @Test
    fun `clearScope 호출 시 해당 스코프 인스턴스만 제거된다`() {
        // given
        val singletonRepo = DummyRepository()
        val activityRepo = DummyRepository()
        val key = DummyRepository::class to null

        DIScopeManager.putInstance(ScopeType.Singleton, key, singletonRepo)
        DIScopeManager.putInstance(ScopeType.Activity, key, activityRepo)

        // when
        DIScopeManager.clearScope(ScopeType.Activity)

        // then
        assertThat(DIScopeManager.getInstance(ScopeType.Activity, key)).isNull()
        assertThat(singletonRepo).isEqualTo(DIScopeManager.getInstance(ScopeType.Singleton, key))
    }
}
