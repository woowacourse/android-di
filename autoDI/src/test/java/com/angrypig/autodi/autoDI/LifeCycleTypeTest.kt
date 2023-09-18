package com.angrypig.autodi.autoDI

import com.angrypig.autodi.autoDI.dummys.FakeRepository
import com.angrypig.autodi.autoDI.dummys.FakeRepositoryImpl
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.full.starProjectedType

class LifeCycleTypeTest {

    @Test
    fun `LifeCycle 하위 객체 생성시 type 변수값을 생성한다`() {
        // Singleton
        val singleton = com.angrypig.autodi.LifeCycleType.Singleton<FakeRepository> {
            FakeRepositoryImpl()
        }
        assertThat(singleton.type).isEqualTo(FakeRepository::class.starProjectedType)
        // Disposable
        val disposable = com.angrypig.autodi.LifeCycleType.Disposable<FakeRepository> {
            FakeRepositoryImpl()
        }
        assertThat(singleton.type).isEqualTo(FakeRepository::class.starProjectedType)
    }
}
