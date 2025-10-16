package woowacourse.shopping.data.annotation

import com.example.di.Qualifier

@Qualifier
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryCartProductDao

@Qualifier
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalDatabaseCartProductDao
