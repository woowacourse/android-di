package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredMemberProperties

class DependencyContainerTest {
    private val dependencyContainer: DependencyContainer =
        DependencyContainer.getSingletonInstance()

    interface Person {
        val name: String
    }

    class AnnotationContainer {
        @Qualifier("SoptMvp")
        val soptMvp: String = ""
    }

    class Bixx(override val name: String = "bixx") : Person
    class Sangun(override val name: String = "sangun") : Person
    class MatPig(override val name: String = "matPig") : Person

    @BeforeEach
    fun clear() {
        dependencyContainer.clear()
    }

    @Test
    fun `Bixx 인스턴스를 addInstance하면 getInstance로 나온다`() {
        // given
        val bixx = Bixx()

        // when
        dependencyContainer.addInstance(Person::class, emptyList(), bixx)
        val actual = dependencyContainer.getInstance(Person::class)

        // then
        assertThat(actual).isEqualTo(bixx)
    }

    @Test
    fun `저장된 여러 구현체 중 지정한 Qualifier에 해당하는 구현체를 가져온다`() {
        // 빅스, 산군, 멧돼지 중 Qualifier("SoptMvp")로 지정된 구현체를 가져온다
        // given
        val bixx: Person = Bixx()
        val sangun: Person = Sangun()
        val matPig: Person = MatPig()
        val soptMvpQualifier: List<Annotation> = AnnotationContainer::class
            .declaredMemberProperties
            .first()
            .annotations

        // when
        dependencyContainer.run {
            addInstance(Person::class, emptyList(), bixx)
            addInstance(Person::class, emptyList(), sangun)
            addInstance(Person::class, soptMvpQualifier, matPig)
        }
        val actual = dependencyContainer.getInstance(Person::class, "SoptMvp")

        // then
        assertThat(actual).isEqualTo(matPig)
    }
}
