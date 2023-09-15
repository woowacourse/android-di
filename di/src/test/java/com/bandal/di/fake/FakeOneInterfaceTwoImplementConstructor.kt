package com.bandal.di.fake

import com.bandal.di.BandalInject
import com.bandal.di.Database
import com.bandal.di.InMemory

class FakeOneInterfaceTwoImplementConstructor @BandalInject constructor(
    @InMemory val inMemory: FakeInterface,
    @Database val database: FakeInterface,
)
