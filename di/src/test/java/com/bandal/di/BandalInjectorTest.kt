package com.bandal.di

import com.bandal.di.fake.FakeImplement
import com.bandal.di.fake.FakeImplementWithQualifierDatabase
import com.bandal.di.fake.FakeImplementWithQualifierInMemory
import com.bandal.di.fake.FakeInterface
import com.bandal.di.fake.FakeOneInterfaceOneImplementConstructor
import com.bandal.di.fake.FakeOneInterfaceTwoImplementConstructor
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Test

class BandalInjectorTest {

    @After
    fun tearDown() {
        BandalInjectorAppContainer.clear()
    }

    @Test
    fun `주생성자 안에 하나의 인터페이스와 구현체 하나가 있을 때 주입할 수 있다`() {
        // given
        val instanceForInject: FakeInterface = FakeImplement()
        BandalInjectorAppContainer.addInstance(FakeInterface::class, instanceForInject)

        // when
        val injectedClass = BandalInjector.inject(FakeOneInterfaceOneImplementConstructor::class)

        // then
        assertEquals(injectedClass.fakeImplement, instanceForInject)
    }

    @Test(expected = DIError.NotFoundQualifierOrInstance::class)
    fun `주입하려는 인스턴스를 찾지 못할 때 예외처리한다`() {
        // given: 주입하려는 인터페이스에 대한 인스턴스를 미리 컨테이너에 추가하지 않으면

        // when: 주입을 시켜주려할 때
        val injectedClass = BandalInjector.inject(FakeOneInterfaceOneImplementConstructor::class)

        // then: 예외처리 된다.
    }

    @Test
    fun `같은 인터페이스를 구현하는 두 구현체를 주입시킬 수 있다`() {
        // given
        val fakeImplementWithQualifierInMemory: FakeInterface = FakeImplementWithQualifierInMemory()
        val fakeImplementWithQualifierDatabase: FakeInterface = FakeImplementWithQualifierDatabase()
        BandalInjectorAppContainer.addInstance(
            FakeInterface::class,
            fakeImplementWithQualifierInMemory,
        )
        BandalInjectorAppContainer.addInstance(
            FakeInterface::class,
            fakeImplementWithQualifierDatabase,
        )

        // when
        val injectedClass = BandalInjector.inject(FakeOneInterfaceTwoImplementConstructor::class)

        // then
        assertEquals(injectedClass.inMemory, fakeImplementWithQualifierInMemory)
        assertEquals(injectedClass.database, fakeImplementWithQualifierDatabase)
    }
}
