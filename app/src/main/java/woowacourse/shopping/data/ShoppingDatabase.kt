package woowacourse.shopping.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}
