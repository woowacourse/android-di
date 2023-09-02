package woowacourse.shopping.provider

internal object Dummy {
    fun Product(
        name: String = "수달고기",
        price: Int = Int.MAX_VALUE,
        imageUrl: String = "https://i.namu.wiki/i/syhMgeDDOS9VKuWgP6eRxr2d7c1xE21e2ImxCS6SItzjYJIIrgsW8ZuQV94d8Gs8YTooUDRCkzjYlT8wCzh5RaDRiPX6ZwUmKz4ok1gTL8TDnvc2SZ-D6AgZEeIYfbuyfJwt7M9aZnthskOvFnUenA.webp",
    ) = woowacourse.shopping.model.Product(
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}
