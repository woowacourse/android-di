//package com.app.covi_di.core
//
//import com.app.covi_di.annotation.Inject
//import com.app.covi_di.module.DependencyModule
//import org.junit.Test
//
//class InjectorTest {
//    @Test
//    fun `Inject 어노테이션이 있으면 생성자를 주입한다`() {
//        DIContainer.init(
//            listOf(
//
//            ),
//            listOf(),
//            FakeContext()
//        )
//        val fakeInstance = Injector.inject<>()
//        assert(fakeInstance is FakeInstance)
//    }
//}
//
//class FakeInstance @Inject constructor(private val fakeRepository: FakeRepository) {
//
//}
//
//class FakeRepository()