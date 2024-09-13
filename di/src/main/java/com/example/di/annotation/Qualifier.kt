package com.example.di.annotation

import kotlin.reflect.KClass

annotation class Qualifier(val type: KClass<*>)
