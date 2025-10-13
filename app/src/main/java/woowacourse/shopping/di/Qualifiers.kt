package woowacourse.shopping.di

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Target(AnnotationTarget.PROPERTY)
@Qualifier
annotation class InjectRoomCartRepository

@Target(AnnotationTarget.PROPERTY)
@Qualifier
annotation class InjectInMemoryCartRepository
