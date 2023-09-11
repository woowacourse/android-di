package com.angrypig.autodi.autoDI

import com.angrypig.autodi.AutoDI
import com.angrypig.autodi.Injector
import com.angrypig.autodi.autoDIModule.autoDIModule
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import kotlin.reflect.full.isSubclassOf

private interface FakeRepository

private class FakeRepositoryImpl : FakeRepository

class InjectorTest {
    @After
    fun tearDown() {
        AutoDI.clearModuleContainer()
    }

    @Test
    fun `singleton으로 등록된 객체를 프로퍼티 위임을 통해 주입받는다(qualifier 받지않는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            registerModule(autoDIModule { singleton<FakeRepository>(testQualifier) { FakeRepositoryImpl() } })
        }

        // then
        val fakeRepository: FakeRepository by Injector()
        assertThat(fakeRepository::class.isSubclassOf(FakeRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `singleton으로 등록된 객체를 프로퍼티 위임을 통해 주입받는다(qualifier 받는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            registerModule(autoDIModule { singleton<FakeRepository>(testQualifier) { FakeRepositoryImpl() } })
        }

        // then
        val fakeRepository: FakeRepository by Injector(testQualifier)
        assertThat(fakeRepository::class.isSubclassOf(FakeRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `disposable 객체를 Container에 등록한다(qualifier 받지않는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            registerModule(autoDIModule { disposable<FakeRepository>(testQualifier) { FakeRepositoryImpl() } })
        }

        // then
        val fakeRepository: FakeRepository by Injector()
        assertThat(fakeRepository::class.isSubclassOf(FakeRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `disposable 객체를 Container에 등록한다(qualifier 받는 상태)`() {
        // when
        val testQualifier = "test"
        AutoDI {
            registerModule(autoDIModule { disposable<FakeRepository>(testQualifier) { FakeRepositoryImpl() } })
        }

        // then
        val cartRepository: FakeRepository by Injector(testQualifier)
        assertThat(cartRepository::class.isSubclassOf(FakeRepositoryImpl::class)).isTrue()
    }

    @Test
    fun `String값 "멧돼지"를 주입받는 Pig 객체를 inject함수를 통해 주입받는다`() {
        // given
        class Pig(val name: String)
        AutoDI {
            registerModule(
                autoDIModule {
                    disposable { Pig(inject("pigName")) }
                    disposable("pigName") { "멧돼지" }
                },
            )
        }

        // when
        val injectedInstance: Pig by Injector()

        // then
        val pigName = "멧돼지"
        assertThat(injectedInstance.name).isEqualTo(pigName)
    }
}
