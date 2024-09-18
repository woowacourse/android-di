package com.kmlibs.supplin.fixtures.android.viewmodel

import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.fixtures.FakeRepository1
import com.kmlibs.supplin.fixtures.FakeRepository2
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository3
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository4
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository5
import com.kmlibs.supplin.fixtures.android.repository.FakeRepository

class FakeViewModel1
    @Supply
    constructor(
        @FakeRepository1
        val fakeRepository: FakeRepository,
    ) : ViewModel() {
        val isRepositoryInitialized: Boolean
            get() = ::fakeRepository2.isInitialized

        @Supply
        @FakeRepository2
        lateinit var fakeRepository2: FakeRepository
    }

class FakeViewModel2(
    @Supply
    @FakeRepository1
    val fakeRepository: FakeRepository,
    @Supply
    @FakeRepository2
    private val fakeRepository2: FakeRepository,
) : ViewModel()

class FakeViewModel3
    @Supply
    constructor(
        val fakeRepository3: DefaultFakeRepository3,
        val fakeRepository4: DefaultFakeRepository4,
        val fakeRepository5: DefaultFakeRepository5,
    ) : ViewModel()

class FakeViewModel4
    @Supply
    constructor(
        val fakeRepository3: DefaultFakeRepository3,
        val fakeRepository4: DefaultFakeRepository4,
    ) : ViewModel() {
        val isRepositoryInitialized: Boolean
            get() = ::fakeRepository5.isInitialized

        @Supply
        lateinit var fakeRepository5: DefaultFakeRepository5
    }
