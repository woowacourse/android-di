package com.example.seogi.fixture

import android.content.Context
import com.example.seogi.di.Module
import com.example.seogi.di.annotation.ActivityScoped
import com.example.seogi.di.annotation.Qualifier
import com.example.seogi.di.annotation.SingleTone
import com.example.seogi.di.annotation.ViewModelScoped

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
    @SingleTone
    fun provideChildFoo1(): ParentFoo = ChildFoo1()

    @Child2
    @ViewModelScoped
    fun provideChildFoo2(bar: Bar): ParentFoo = ChildFoo2(bar)

    @ActivityScoped
    fun provideDateFormatter(context: Context): FakeDateFormatter = FakeDateFormatter(context)
}
