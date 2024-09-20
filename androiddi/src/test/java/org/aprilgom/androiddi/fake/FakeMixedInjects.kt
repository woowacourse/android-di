package org.aprilgom.androiddi.fake

import javax.inject.Inject

class FakeMixedInjectClass(
    @Inject
    val fakeConstructorFieldClass: FakeConstructorFieldClass,
) {
    @Inject
    lateinit var fakeField: FakeField
}
