package com.kmlibs.supplin.fixtures.android.repository

import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.fixtures.FakeDataSource1
import com.kmlibs.supplin.fixtures.FakeDataSource2
import com.kmlibs.supplin.fixtures.FakeRepository6
import com.kmlibs.supplin.fixtures.FakeRepository7
import com.kmlibs.supplin.fixtures.FakeRepository8
import com.kmlibs.supplin.fixtures.android.datasource.DefaultFakeDataSource1
import com.kmlibs.supplin.fixtures.android.datasource.DefaultFakeDataSource2
import com.kmlibs.supplin.fixtures.android.datasource.FakeDataSource
import com.kmlibs.supplin.fixtures.android.datasource.OtherDataSource

interface FakeRepository

class DefaultFakeRepository1 : FakeRepository

class DefaultFakeRepository2 : FakeRepository

class DefaultFakeRepository3
    @Supply
    constructor() {
        @Supply
        private lateinit var fakeDataSource1: DefaultFakeDataSource1

        @Supply
        private lateinit var fakeDataSource2: DefaultFakeDataSource2
    }

class DefaultFakeRepository4
    @Supply
    constructor(
        private val fakeDataSource1: DefaultFakeDataSource1,
        private val fakeDataSource2: DefaultFakeDataSource2,
    )

class DefaultFakeRepository5
    @Supply
    constructor(
        private val fakeDataSource1: DefaultFakeDataSource1,
    ) {
        @Supply
        private lateinit var fakeDataSource2: DefaultFakeDataSource2
    }

@FakeRepository6
class DefaultFakeRepository6
    @Supply
    constructor(
        @FakeDataSource1
        private val fakeDataSource1: FakeDataSource,
        @FakeDataSource2
        private val fakeDataSource2: FakeDataSource,
    ) : FakeRepository

@FakeRepository7
class DefaultFakeRepository7
    @Supply
    constructor(
        @FakeDataSource1
        private val fakeDataSource1: FakeDataSource,
    ) : FakeRepository {
        @Supply
        @FakeDataSource2
        private lateinit var fakeDataSource2: FakeDataSource
    }

@FakeRepository8
class DefaultFakeRepository8
    @Supply
    constructor(
        private val otherDataSource: OtherDataSource,
        @FakeDataSource2
        private val fakeDataSource2: FakeDataSource,
    ) : FakeRepository
