package com.woowa.di.fixture.qualifier

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class QualifierTestViewModel : ViewModel() {
    @Inject
    @FakeQualifier
    lateinit var fake1: TestQualifier

    @Inject
    @Fake2Qualifier
    lateinit var fake2: TestQualifier
}

class FailQualifierTestViewModel : ViewModel() {
    @Inject
    lateinit var fake1: TestQualifier

    @Inject
    @Fake2Qualifier
    lateinit var fake2: TestQualifier
}
