package woowacourse.shopping.di

import javax.inject.Qualifier

/**
 * Qualifier for distinguishing instances that are the same type but have different implementations.
 *
 * Two additional meta annotations are needed : `@Qualifier`, `@Retention`.
 * ```
 * @Qualifier
 * @Retention(AnnotationRetention.BINARY)
 * annotation class CustomRepository
 * ```
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemoryRepository
