package com.example.di.annotation

/**
 * Qualifier 메타 어노테이션.
 * 예:
 *   @Qualifier annotation class InMemory
 *   @Qualifier annotation class Prod
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier
