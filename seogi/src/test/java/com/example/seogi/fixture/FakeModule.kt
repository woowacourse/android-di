package com.example.seogi.fixture

import com.example.seogi.di.Module
import com.example.seogi.di.annotation.Qualifier

interface ParentFoo

class ChildFoo1 : ParentFoo

class ChildFoo2(bar: Bar) : ParentFoo

class Bar

@Qualifier
annotation class Child1

@Qualifier
annotation class Child2

object FakeModule : Module {
    fun provideBar(): Bar = Bar()

    @Child1
    fun provideChildFoo1(): ParentFoo = ChildFoo1()

    @Child2
    fun provideChildFoo2(bar: Bar): ParentFoo = ChildFoo2(bar)
}
