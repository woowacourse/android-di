package com.kmlibs.supplin.fixtures.android.repository

import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.fixtures.android.datasource.DefaultFakeDataSource1
import com.kmlibs.supplin.fixtures.android.datasource.DefaultFakeDataSource2

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
