package woowacourse.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DIScopeManagerTest {
    class DummyRepository

    annotation class QualifierA

    annotation class QualifierB

    private val activityScopeKey = "Activity1"
    private val anotherActivityScopeKey = "Activity2"
    private val viewModelScopeKey = "ViewModel"

    @BeforeEach
    fun setup() {
        DIScopeManager.clearScope(activityScopeKey)
        DIScopeManager.clearScope(anotherActivityScopeKey)
        DIScopeManager.clearScope(viewModelScopeKey)
    }

    @Test
    fun `putInstance 후 getInstance 로 같은 객체를 가져온다`() {
        // given
        val repo = DummyRepository()
        val dependencyKey = DummyRepository::class to null

        // when
        DIScopeManager.putInstance(ScopeType.Singleton, "", dependencyKey, repo)

        // then
        val retrieved = DIScopeManager.getInstance(ScopeType.Singleton, "", dependencyKey)
        assertThat(repo).isEqualTo(retrieved)
    }

    @Test
    fun `같은 클래스여도 다른 Qualifier면 별도로 관리된다`() {
        // given
        val repoA = DummyRepository()
        val repoB = DummyRepository()

        val dependencyKeyA = DummyRepository::class to QualifierA::class
        val dependencyKeyB = DummyRepository::class to QualifierB::class

        // when
        DIScopeManager.putInstance(ScopeType.Activity, activityScopeKey, dependencyKeyA, repoA)
        DIScopeManager.putInstance(ScopeType.Activity, activityScopeKey, dependencyKeyB, repoB)

        // then
        assertThat(repoA).isEqualTo(
            DIScopeManager.getInstance(
                ScopeType.Activity,
                activityScopeKey,
                dependencyKeyA,
            ),
        )
        assertThat(repoB).isEqualTo(
            DIScopeManager.getInstance(
                ScopeType.Activity,
                activityScopeKey,
                dependencyKeyB,
            ),
        )
    }

    @Test
    fun `다른 ScopeType 은 서로 간섭하지 않는다`() {
        // given
        val singletonRepo = DummyRepository()
        val viewModelRepo = DummyRepository()
        val dependencyKey = DummyRepository::class to null

        // when
        DIScopeManager.putInstance(ScopeType.Singleton, "", dependencyKey, singletonRepo)
        DIScopeManager.putInstance(
            ScopeType.ViewModel,
            viewModelScopeKey,
            dependencyKey,
            viewModelRepo,
        )

        // then
        assertThat(singletonRepo).isEqualTo(
            DIScopeManager.getInstance(
                ScopeType.Singleton,
                "",
                dependencyKey,
            ),
        )
        assertThat(viewModelRepo).isEqualTo(
            DIScopeManager.getInstance(
                ScopeType.ViewModel,
                viewModelScopeKey,
                dependencyKey,
            ),
        )
    }

    @Test
    fun `clearScope 호출 시 해당 스코프 인스턴스만 제거된다`() {
        // given
        val singletonRepo = DummyRepository()
        val activityRepo = DummyRepository()
        val dependencyKey = DummyRepository::class to null

        DIScopeManager.putInstance(ScopeType.Singleton, "", dependencyKey, singletonRepo)
        DIScopeManager.putInstance(
            ScopeType.Activity,
            activityScopeKey,
            dependencyKey,
            activityRepo,
        )

        // when
        DIScopeManager.clearScope(activityScopeKey)

        // then
        assertThat(
            DIScopeManager.getInstance(
                ScopeType.Activity,
                activityScopeKey,
                dependencyKey,
            ),
        ).isNull()
        assertThat(singletonRepo).isEqualTo(
            DIScopeManager.getInstance(
                ScopeType.Singleton,
                "",
                dependencyKey,
            ),
        )
    }

    @Test
    fun `다른 scopeKey를 가진 Activity 스코프는 서로 간섭하지 않는다`() {
        // given
        val activity1Repo = DummyRepository()
        val activity2Repo = DummyRepository()
        val dependencyKey = DummyRepository::class to null

        // when
        DIScopeManager.putInstance(
            ScopeType.Activity,
            activityScopeKey,
            dependencyKey,
            activity1Repo,
        )
        DIScopeManager.putInstance(
            ScopeType.Activity,
            anotherActivityScopeKey,
            dependencyKey,
            activity2Repo,
        )

        // then
        val retrieved1 =
            DIScopeManager.getInstance(ScopeType.Activity, activityScopeKey, dependencyKey)
        val retrieved2 =
            DIScopeManager.getInstance(ScopeType.Activity, anotherActivityScopeKey, dependencyKey)
        assertThat(activity1Repo).isEqualTo(retrieved1)
        assertThat(activity2Repo).isEqualTo(retrieved2)
        assertThat(retrieved1).isNotEqualTo(retrieved2)
    }
}
