package woowacourse.shopping.di.room.fixture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TestDao {
    @Query("SELECT * FROM test_table")
    suspend fun getAll(): List<TestEntity>

    @Insert
    suspend fun insert(entity: TestEntity)
}
