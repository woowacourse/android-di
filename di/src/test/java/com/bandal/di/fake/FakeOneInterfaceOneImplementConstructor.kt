package com.bandal.di.fake

import com.bandal.di.BandalInject

class FakeOneInterfaceOneImplementConstructor @BandalInject constructor(
    val fakeImplement: FakeInterface,
)
