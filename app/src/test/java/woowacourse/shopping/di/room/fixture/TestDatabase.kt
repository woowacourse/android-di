package woowacourse.shopping.di.room.fixture

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TestEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TestDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao
}
