package woowacourse

import woowacourse.fakeClasses.AConstructorDependency
import woowacourse.fakeClasses.AFieldDependency
import woowacourse.fakeClasses.ConstructorDependency
import woowacourse.fakeClasses.ConstructorTestActivity
import woowacourse.fakeClasses.FieldDependency
import woowacourse.fakeClasses.FieldTestActivity
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DependencyInjectorTest {

    @Before
    fun setup() {
        com.woowacourse.bunadi.injector.DependencyInjector.clear()
    }

    @Test
    fun 뷰모델의_생성자_종속_항목을_자동_주입해준다() {
        // given: 뷰모델 생성에 필요한 종속 항목을 주입한다.
        com.woowacourse.bunadi.dsl.modules {
            com.woowacourse.bunadi.dsl.types {
                type(ConstructorDependency::class to AConstructorDependency::class)
            }
        }

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(ConstructorTestActivity::class.java)
            .create()
            .get()

        // then: 액티비티의 뷰모델에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertTrue(constructorDependency is AConstructorDependency)
        }
    }

    @Test
    fun 뷰모델에서_Inject_애노테이션이_붙은_필드에_종속_항목을_자동_주입해준다() {
        // given: 뷰모델 생성과 필드에 필요한 종속 항목을 주입한다.
        com.woowacourse.bunadi.dsl.modules {
            com.woowacourse.bunadi.dsl.types {
                type(FieldDependency::class to AFieldDependency::class)
            }
        }

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(FieldTestActivity::class.java)
            .create()
            .get()

        // then: Inject 애노테이션이 포함된 뷰모델의 필드에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertTrue(fieldWithInjectAnnotation is AFieldDependency)
        }
    }

    @Test
    fun 뷰모델에서_Inject_애노테이션이_없는_필드에는_종속_항목을_주입하지_않는다() {
        // given: 뷰모델 생성과 필드에 필요한 종속 항목을 주입한다.
        com.woowacourse.bunadi.dsl.modules {
            com.woowacourse.bunadi.dsl.types {
                type(FieldDependency::class to AFieldDependency::class)
            }
        }

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(FieldTestActivity::class.java)
            .create()
            .get()

        // then: Inject 애노테이션이 포함된 뷰모델의 필드에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertFalse(isFieldWithoutInjectAnnotationInitialized())
        }
    }
}
