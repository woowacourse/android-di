package woowacourse.shopping.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao

    companion object {
        const val DATABASE_NAME = "shopping_database"
    }
}

// TODO:  Application Context
fun createRoomDatabase(context: Context): ShoppingDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        ShoppingDatabase::class.java,
        ShoppingDatabase.DATABASE_NAME,
    ).build()
}

@Database(entities = [CartProductEntity::class], version = 1, exportSchema = false)
abstract class ImMemoryShoppingDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}

// TODO : Activity Context
fun createInMemoryDatabase(context: Context): ImMemoryShoppingDatabase {
    return Room.inMemoryDatabaseBuilder(
        context.applicationContext,
        ImMemoryShoppingDatabase::class.java,
    ).build()
}
