package com.buna.di.injector

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.buna.di.annotation.Inject
import com.buna.di.viewModel.viewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

class ConstructorTestActivity : AppCompatActivity() {
    val viewModel: ConstructorTestViewModel by viewModel()
}

class ConstructorTestViewModel(
    val constructorDependency: ConstructorDependency,
) : ViewModel()

class FieldTestActivity : AppCompatActivity() {
    val viewModel: FieldTestViewModel by viewModel()
}

class FieldTestViewModel : ViewModel() {
    @Inject
    lateinit var fieldWithInjectAnnotation: FieldDependency
    lateinit var fieldWithoutInjectAnnotation: FieldDependency

    fun isFieldWithoutInjectAnnotationInitialized() = ::fieldWithoutInjectAnnotation.isInitialized
}

class ConstructorDependency
class FieldDependency

@RunWith(RobolectricTestRunner::class)
class DependencyInjectorTest {

    @Before
    fun setup() {
        DependencyInjector.clear()
    }

    @Test
    fun 뷰모델의_생성자_종속_항목을_자동_주입해준다() {
        // given: 뷰모델 생성에 필요한 종속 항목을 주입한다.
        val constructorDependency = ConstructorDependency()
        DependencyInjector.module(constructorDependency)

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(ConstructorTestActivity::class.java)
            .create()
            .get()

        // then: 액티비티의 뷰모델에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertEquals(constructorDependency, this.constructorDependency)
        }
    }

    @Test
    fun 뷰모델에서_Inject_애노테이션이_붙은_필드에_종속_항목을_자동_주입해준다() {
        // given: 뷰모델 생성과 필드에 필요한 종속 항목을 주입한다.
        val fieldDependency = FieldDependency()
        DependencyInjector.module(fieldDependency)

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(FieldTestActivity::class.java)
            .create()
            .get()

        // then: Inject 애노테이션이 포함된 뷰모델의 필드에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertEquals(fieldDependency, this.fieldWithInjectAnnotation)
        }
    }

    @Test
    fun 뷰모델에서_Inject_애노테이션이_없는_필드에는_종속_항목을_주입하지_않는다() {
        // given: 뷰모델 생성과 필드에 필요한 종속 항목을 주입한다.
        val fieldDependency = FieldDependency()
        DependencyInjector.module(fieldDependency)

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
