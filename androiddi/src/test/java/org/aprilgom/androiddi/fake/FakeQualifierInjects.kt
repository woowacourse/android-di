package org.aprilgom.androiddi.fake

import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
annotation class FakeQualifier1

@Qualifier
annotation class FakeQualifier2

interface FakeQualifierInterface {
    fun getValue(): Int
}

class FakeQualifierClass1(val intValue: Int) : FakeQualifierInterface {
    override fun getValue(): Int {
        return intValue
    }
}

class FakeQualifierClass2(val intValue: Int) : FakeQualifierInterface {
    override fun getValue(): Int {
        return intValue
    }
}

class FakeQualifierInjectClass {
    @Inject
    @FakeQualifier1
    lateinit var fakeQualifier1: FakeQualifierInterface

    @Inject
    @FakeQualifier2
    lateinit var fakeQualifier2: FakeQualifierInterface
}
