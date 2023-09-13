package com.now.di

import com.now.annotation.Qualifier

@Qualifier
annotation class Aluminum

class Housing

class Switch

class FakeModule : Module {
    @Aluminum
    fun housing(): Housing {
        return Housing()
    }

    fun switch(): Switch {
        return Switch()
    }
}
