package com.woowa.di.fixture

import com.woowa.di.injection.DIInjection

class DITest(
    @DIInjection
    private val test: Test,
)

class DIFailTest(
    private val test: Test,
)
