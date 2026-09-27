package woowacourse.shopping.model

// 담은 시각은 지역 시간 문자열이 아닌 epoch 밀리초로 보관해 저장 데이터와 화면 표시 형식을 분리한다.
class CartProduct(
    val id: Long,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val createdAt: Long,
)
