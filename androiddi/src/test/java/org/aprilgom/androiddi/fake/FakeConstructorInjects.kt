package org.aprilgom.androiddi.fake

import javax.inject.Inject

class FakeConstructorInjectClass(
    @Inject val fakeConstructor: FakeConstructorFieldClass,
)

class FakeConstructorFieldClass(val intValue: Int)
