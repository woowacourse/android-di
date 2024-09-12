package woowacourse.shopping.di.annotation

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CLASS,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
annotation class InMemoryDatabase

@Qualifier
annotation class RoomDatabase
