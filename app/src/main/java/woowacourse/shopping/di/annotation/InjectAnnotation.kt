package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectField

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemory

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomDb
