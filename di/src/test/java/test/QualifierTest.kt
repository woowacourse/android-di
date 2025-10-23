package test

import com.example.di.DatabaseLogger
import com.example.di.RequireInjection
import org.assertj.core.api.SoftAssertions
import org.junit.Before
import org.junit.Test
import test.fixture.FakeCartViewModel
import kotlin.reflect.full.findAnnotation

class QualifierTest {
    private lateinit var fakeCartViewModel: FakeCartViewModel

    @Before
    fun setUp() {
        fakeCartViewModel = FakeCartViewModel()
    }

    @Test
    fun `해당 클래스에 어노테이션이 존재한다`() {
        // given
        val softly = SoftAssertions()

        // when
        val property = fakeCartViewModel::class.members.first()

        // then
        val hasRequireInjection = property.findAnnotation<RequireInjection>() != null
        val hasDatabaseLogger = property.findAnnotation<DatabaseLogger>() != null
        softly.assertThat(hasRequireInjection).isEqualTo(true)
        softly.assertThat(hasDatabaseLogger).isEqualTo(true)
        softly.assertAll()
    }
}
