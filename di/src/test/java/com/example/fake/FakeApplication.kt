package com.example.fake

import FakeModule
import com.example.di.DIApplication
import com.example.di.DIModule

class FakeApplication : DIApplication() {
    override val module: DIModule = FakeModule()
}
