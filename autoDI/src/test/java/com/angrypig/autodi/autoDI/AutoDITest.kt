package com.angrypig.autodi.autoDI

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.repository.CartRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import com.angrypig.autodi.autoDIModule.AutoDIModule

class AutoDITest {

    @Test
    fun `singleton 객체를 Container에 등록한다`() {
        // when
        val testQualifier = "test"
        com.angrypig.autodi.AutoDI {
            singleton<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val containerQualifier = com.angrypig.autodi.autoDIModule.AutoDIModule.singletons.value.first().qualifier
        val containerInstanceType =
            com.angrypig.autodi.autoDIModule.AutoDIModule.singletons.value.first().getInstance().javaClass.typeName
        assertThat(containerQualifier).isEqualTo(containerQualifier)
        assertThat(containerInstanceType).isEqualTo(CartRepositoryImpl().javaClass.typeName)
    }

    @Test
    fun `disposable 객체를 Container에 등록한다`() {
        // when
        val testQualifier = "test"
        com.angrypig.autodi.AutoDI {
            disposable<CartRepository>(testQualifier) { CartRepositoryImpl() }
        }

        // then
        val containerQualifier = com.angrypig.autodi.autoDIModule.AutoDIModule.disposables.value.first().qualifier
        val containerInstanceType =
            com.angrypig.autodi.autoDIModule.AutoDIModule.disposables.value.first().getInstance().javaClass.typeName
        assertThat(containerQualifier).isEqualTo(containerQualifier)
        assertThat(containerInstanceType).isEqualTo(CartRepositoryImpl().javaClass.typeName)
    }

    @Test
    fun `String값 "멧돼지"를 주입받는 Pig 객체를 inject함수를 통해 주입받는다`() {
        // given
        class Pig(val name: String)
        com.angrypig.autodi.AutoDI {
            disposable { Pig(inject("pigName")) }
            disposable("pigName") { "멧돼지" }
        }

        // when
        val injectedInstance = com.angrypig.autodi.AutoDI.inject<Pig>()

        // then
        val pigName = "멧돼지"
        assertThat(injectedInstance.name).isEqualTo(pigName)
    }
}
