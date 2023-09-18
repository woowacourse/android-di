package woowacourse.shopping.ui.main

import android.content.Context
import android.widget.Toast

class ChildShareResource {
    private var printCount: Int = 0

    fun print(host: String, context: Context) {
        Toast.makeText(context, "$host : ${++printCount}번 호출되었어요!", Toast.LENGTH_SHORT).show()
    }
}
