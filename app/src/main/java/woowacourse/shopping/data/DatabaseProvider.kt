package woowacourse.shopping.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: ShoppingDatabase? = null

    fun getDatabase(context: Context): ShoppingDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java, "shopping-database"
            ).build()
        }
        return instance!!
    }
}
