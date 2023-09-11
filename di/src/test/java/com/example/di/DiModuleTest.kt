package com.example.di

//import android.content.Context
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.ViewModel
//import androidx.test.core.app.ApplicationProvider
//import com.example.di.annotation.FieldInject
//import com.example.di.application.DiApplication
//import com.example.di.module.ApplicationModule
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNotNull
//import org.junit.Assert.assertThrows
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
//import woowacourse.shopping.data.CartRepository
//import woowacourse.shopping.data.ProductRepository
//import woowacourse.util.FakeApplicationModule
//import woowacourse.util.getFakeActivityModule
//import woowacourse.util.getFakeApplicationModule
//import kotlin.reflect.full.isSubclassOf
//
//class FakeViewModel(
//    private val cartRepository: CartRepository,
//) : ViewModel() {
//    @FieldInject
//    lateinit var productRepository: ProductRepository
//}
//
//class FakePrivateFieldInjectViewModel(
//    private val cartRepository: CartRepository,
//) : ViewModel() {
//    @FieldInject
//    private lateinit var productRepository: ProductRepository
//}
//
//@RunWith(RobolectricTestRunner::class)
//@Config(application = FakeApplication::class)
//internal class DiModuleTest {
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var application: Context
//
//    @Before
//    fun setup() {
//        application = ApplicationProvider.getApplicationContext<DiApplication>()
//    }
//
//    @Test
//    fun `DefaultActivityModule로 MainViewModel을 만들 수 있다`() {
//        // given
//        val activityModule =
//            getFakeActivityModule(application, getFakeApplicationModule(application))
//
//        // when
//        val instance = activityModule.provideInstance(FakeViewModel::class.java)
//
//        // then
//        assertEquals(true, instance::class.isSubclassOf(FakeViewModel::class))
//        assertNotNull(instance.productRepository)
//    }
//
//    @Test
//    fun `가시성이 공개되지 않은 필드는 주입받을 수 없다`() {
//        // given
//        val activityModule =
//            getFakeActivityModule(application, getFakeApplicationModule(application))
//
//        // then
//        assertThrows(IllegalStateException::class.java) {
//            activityModule.provideInstance(FakePrivateFieldInjectViewModel::class.java)
//        }
//    }
//}
