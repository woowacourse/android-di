package woowacourse.shopping.data.mapper

import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.model.CartProduct

// Room이 부여한 ID와 저장 시각을 버리면 화면에서 원래 항목을 삭제하거나 담은 날짜를 표시할 수 없다.
fun CartProductEntity.toDomain(): CartProduct =
    CartProduct(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        createdAt = createdAt,
    )
