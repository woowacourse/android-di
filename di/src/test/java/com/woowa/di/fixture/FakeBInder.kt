package com.woowa.di.fixture

interface FakeDI

interface Test : FakeDI

class TestInstance : Test

class FakeBInder {
    fun provideTest(): Test = TestInstance()
}
