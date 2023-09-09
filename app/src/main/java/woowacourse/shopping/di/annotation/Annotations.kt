package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.PROPERTY)
annotation class FieldInject

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Qualifier

@Qualifier
annotation class RoomDb

@Qualifier
annotation class InMemory

@Qualifier
annotation class RoomDbCartRepository

@Qualifier
annotation class InMemoryCartRepository
