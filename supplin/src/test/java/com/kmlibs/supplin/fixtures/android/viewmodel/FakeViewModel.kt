package com.kmlibs.supplin.fixtures.android.viewmodel

import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.annotations.Supply
import com.kmlibs.supplin.fixtures.FakeRepository1
import com.kmlibs.supplin.fixtures.FakeRepository2
import com.kmlibs.supplin.fixtures.android.repository.FakeRepository

class FakeViewModel1 @Supply constructor(
    @FakeRepository1
    val fakeRepository: FakeRepository,
) : ViewModel() {
    @Supply
    @FakeRepository2
    private lateinit var fakeRepository2: FakeRepository
}

class FakeViewModel2(
    @Supply
    @FakeRepository1
    val fakeRepository: FakeRepository,
    @Supply
    @FakeRepository2
    private val fakeRepository2: FakeRepository,
) : ViewModel()
