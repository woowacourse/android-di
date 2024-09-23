package com.kmlibs.supplin.annotations

import com.kmlibs.supplin.ComponentContainer
import com.kmlibs.supplin.model.Scope
import kotlin.reflect.KClass

/**
 * For supplying dependencies to classes.
 * This annotation can be used for field injection or constructor injection.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY)
annotation class Supply

/**
 * For providing application context as a parameter of module functions.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ApplicationContext

/**
 * For providing Activity context as a parameter of module functions.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ActivityContext

/**
 * For distinguishing modules that can provide instances needed for the program.
 */
@Target(AnnotationTarget.CLASS)
annotation class Module

/**
 * For distinguishing module functions whether to provide an concrete instance or define an abstract direction.
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Concrete

@Target(AnnotationTarget.FUNCTION)
annotation class Abstract

/**
 * For distinguishing lifecycle of modules.
 */
@Target(AnnotationTarget.CLASS)
annotation class Within(val scope: KClass<out Scope>)
