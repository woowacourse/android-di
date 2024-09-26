package org.aprilgom.androiddi.fake

import javax.inject.Inject

class FakeFieldInjectClass {
    @Inject
    lateinit var fakeField: FakeField
}

data class FakeField(val intValue: Int)
