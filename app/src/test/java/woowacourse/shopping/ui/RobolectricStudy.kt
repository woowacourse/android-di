package woowacourse.shopping.ui

import androidx.recyclerview.widget.RecyclerView
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.R

@RunWith(RobolectricTestRunner::class)
class RobolectricStudy {

    @Test
    fun `리사이클러뷰 클릭 테스트1`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()
            .visible() // UI가 화면에 표시되었다는 것을 가정할 수 있도록 해줌

        val activity = activityController.get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // when
        val isClicked = recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()!!

        // then
        assertTrue(isClicked)
    }

    @Test
    fun `리사이클러뷰 클릭 테스트2`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()

        val activity = activityController.get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // visible() 이 없는 경우 뷰홀더 자체가 화면에 표시되지 않기 때문에 아이템 뷰를 찾아올 수 없음, 즉 null이 된다
        // when
        val itemView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView

        // then
        assertNull(itemView)
    }

    @Test
    fun `리사이클러뷰 클릭 테스트3`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()

        val activity = activityController.get()

        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // Robolectric 테스트 환경에서 리사이클러뷰가 화면에 표시되고 클릭 이벤트를 발생시킬 수 있는 상태로 만듦
        recyclerView.measure(0, 0)
        recyclerView.layout(0, 0, 100, 10000)

        // when
        val isClicked = recyclerView.findViewHolderForAdapterPosition(0)?.itemView?.performClick()!!

        // then
        assertTrue(isClicked)
    }
}
