package woowacourse.shopping.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestRule {
    override fun apply(
        base: Statement,
        description: Description,
    ): Statement {
        return object : Statement() {
            override fun evaluate() {
                // 테스트 시작 전에 Main dispatcher를 대체
                Dispatchers.setMain(dispatcher)
                try {
                    // 테스트 실행
                    base.evaluate()
                } finally {
                    // 테스트 후 Main dispatcher를 원래대로 복원
                    Dispatchers.resetMain()
                    dispatcher.cancel() // Dispatcher 종료
                }
            }
        }
    }
}
