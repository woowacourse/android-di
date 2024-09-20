package com.woowa.di.fixture.qualifier

interface TestQualifier

@FakeQualifier
class FakeImpl : TestQualifier

@Fake2Qualifier
class Fake2Impl : TestQualifier
