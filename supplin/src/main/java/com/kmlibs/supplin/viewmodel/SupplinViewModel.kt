package com.kmlibs.supplin.viewmodel

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.Injector
import kotlin.reflect.KClass

/**
 * These three cases following below are possible.
 * ```
 * class ViewModel1 {
 *      @Supply
 *      private lateinit var foo: Foo
 * }
 * ```
 *
 * ```
 * class ViewModel2 {
 *      @Supply
 *      private lateinit var foo1: Foo1
 *
 *      private lateinit var foo2: Foo2
 * }
 * ```
 * ```
 * class ViewModel3 @Supply constructor(
 *      private val foo: Foo
 * )
 * ```
 *
 * ```
 * class ViewModel4 @Supply constructor(
 *      private val foo1: Foo1
 * ) {
 *      @Supply
 *      private lateinit var foo2: Foo2
 * }
 * ```
 *
 * The cases below are not possible.
 * ```
 * class ViewModel5(
 *      private val foo1: Foo1
 * ) {
 *      @Supply
 *      private lateinit var foo2: Foo2
 * }
 * ```
 * ```
 * class ViewModel6(
 *      @Supply
 *      private val foo1: Foo1,
 *      private val foo2: Foo2
 * )
 * ```
 * ```
 * // use the way of ViewModel3 instead.
 * class ViewModel7(
 *      @Supply
 *      private val foo1: Foo1,
 *      @Supply
 *      private val foo2: Foo2
 * )
 * ```
 */
inline fun <reified VM : ViewModel> ComponentActivity.supplinViewModel(
    vararg modules: KClass<*>,
): Lazy<VM> {
    return viewModels {
        ViewModelFactory(
            viewModelClass = VM::class,
            modules = modules.toList()
        )
    }
}

inline fun <reified VM : ViewModel> Fragment.supplinViewModel(
    vararg modules: KClass<*>,
): Lazy<VM> {
    return viewModels {
        ViewModelFactory(
            viewModelClass = VM::class,
            modules = modules.toList()
        )
    }
}

