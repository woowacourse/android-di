package com.kmlibs.supplin.fixtures.android.datasource

import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.fixtures.FakeDataSource1
import com.kmlibs.supplin.fixtures.FakeDataSource2

interface FakeDataSource

interface OtherDataSource

@FakeDataSource1
class DefaultFakeDataSource1
    @Supply
    constructor() : FakeDataSource, OtherDataSource

@FakeDataSource2
class DefaultFakeDataSource2
    @Supply
    constructor() : FakeDataSource
