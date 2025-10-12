package com.example.di.test.fixture

import androidx.lifecycle.ViewModel
import com.example.di.DatabaseLogger
import com.example.di.RequireInjection

class FakeCartViewModel : ViewModel() {
    @RequireInjection
    @DatabaseLogger
    private lateinit var fakeCartRepository: FakeCartRepositoryImpl
}
