package woowacourse.shopping.util.annotations

import com.example.di.annotations.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryRepo

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomRepo
