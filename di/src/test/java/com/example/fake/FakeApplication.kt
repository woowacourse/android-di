package com.example.fake

import FakeApplicationModule
import com.example.di.DIApplication
import com.example.di.DIModule

class FakeApplication : DIApplication() {
    override val module: DIModule = FakeApplicationModule()
}
