package woowacourse.shopping.di

import javax.inject.Qualifier

/**
 * Qualifier for distinguishing instances that are the same type but have different implementations.
 *
 * Two additional meta annotations are needed : `@Qualifier`, `@Retention`.
 * ```
 * @Qualifier
 * @Retention(AnnotationRetention.RUNTIME)
 * annotation class CustomRepository
 * ```
 */

@Qualifier
annotation class DatabaseRepository

@Qualifier
annotation class InMemoryRepository
