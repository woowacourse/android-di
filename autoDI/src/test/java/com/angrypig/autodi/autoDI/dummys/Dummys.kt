package com.angrypig.autodi.autoDI.dummys

internal interface FakeRepository

internal class FakeRepositoryImpl : FakeRepository

internal interface TestThing {
    val testValue: String
}

internal class Test1(override val testValue: String) : TestThing
internal class Test2(override val testValue: String) : TestThing
internal class Test3(override val testValue: String) : TestThing
internal class Test4(override val testValue: String) : TestThing
internal class Test5(override val testValue: String) : TestThing

