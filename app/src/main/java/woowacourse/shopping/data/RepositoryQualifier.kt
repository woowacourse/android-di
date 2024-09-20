package woowacourse.shopping.data

enum class RepositoryQualifier(val qualifier: String) {
    IN_MEMORY("InMemory"),
    ROOM_DB("RoomDB");

    companion object {
        const val IN_MEMORY = "InMemory"
        const val ROOM_DB = "RoomDB"
    }
}
