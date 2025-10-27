package woowacourse.shopping.hilt

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HiltInMemory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HiltRoomDB
