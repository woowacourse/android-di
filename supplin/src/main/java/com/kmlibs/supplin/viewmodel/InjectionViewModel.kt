package com.kmlibs.supplin.viewmodel

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.kmlibs.supplin.Injector

/**
 * These three cases following below are possible.
 * ```
 * class ViewModel1 {
 *      @Supply
 *      private val foo: Foo
 * }
 * ```
 *
 * ```
 * class ViewModel2 @Supply constructor(
 *      private val foo: Foo
 * )
 * ```
 *
 * ```
 * class ViewModel3 @Supply constructor(
 *      private val foo1: Foo1
 * ) {
 *      @Supply
 *      private val foo2: Foo2
 * }
 * ```
 *
 * The case below is not possible.
 * ```
 * class ViewModel4(
 *      private val foo1: Foo1
 * ) {
 *      @Supply
 *      private val foo2: Foo2
 * }
 * ```
 */
inline fun <reified VM : ViewModel> ComponentActivity.injectionViewModel(): Lazy<VM> {
    return viewModels {
        ViewModelFactory(
            viewModelClass = VM::class,
            instanceContainer = Injector.instanceContainer,
        )
    }
}
