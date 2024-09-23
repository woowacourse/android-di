package com.example.seogi.fixture

import com.example.seogi.di.DiActivity
import com.example.seogi.di.annotation.FieldInject

class FakeActivity : DiActivity() {
    lateinit var viewModel: FakeViewModel

    @FieldInject
    lateinit var dateFormatter: FakeDateFormatter
}
