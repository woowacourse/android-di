package woowacourse.shopping.data.localStorage

import androidx.room.TypeConverter
import java.time.LocalDateTime

object LocalDateTimeConverter {
    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(dateTimeString: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeString)
    }
}
