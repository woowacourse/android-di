package org.aprilgom.androiddi.fake

import javax.inject.Inject

class FakeRecursive1(val intValue: Int = 1)

class FakeRecursive2(
    @Inject val recursive: FakeRecursive1,
)

class FakeRecursive3(
    @Inject val recursive: FakeRecursive2,
)

class FakeRecursive4(
    @Inject val recursive: FakeRecursive3,
)
