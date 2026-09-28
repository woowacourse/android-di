package woowacourse.shopping.util.annotations

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryRepo

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomRepo
