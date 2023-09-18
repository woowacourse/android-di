package com.mission.androiddi

import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.component.activity.retain.NonConfigurationActivityInjectorManager
import com.mission.androiddi.component.application.InjectableApplication
import com.woowacourse.bunadi.injector.Injector
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import org.junit.Test

class NonConfigurationActivityInjectorManagerTest {

    @Test
    fun `인젝터를 액티비티 인젝터 매니저에 저장한다`() {
        // given: Injector와 매니저가 주어졌을 때
        val injectors = HashMap<String, Injector>()
        val injector = mockk<Injector>()
        val sut = NonConfigurationActivityInjectorManager(injectors)

        // when: Injector를 매니저에 저장하면
        sut.saveInjector("KEY", injector)

        // then: 매니저에 Injector가 저장된다.
        assert(injectors["KEY"] == injector)
    }

    @Test
    fun `인젝터를 액티비티 인젝터 매니저에서 제거한다`() {
        // given: 인젝터와 매니저가 주어졌을 때
        val injectorKey = "KEY"
        val injector = mockk<Injector>()

        val injectors = HashMap<String, Injector>().apply {
            put(injectorKey, injector)
        }
        val sut = NonConfigurationActivityInjectorManager(injectors)

        // when: Injector를 매니저에서 제거하면
        sut.removeInjector(injectorKey)

        // then: 매니저에서 Injector가 제거된다.
        assert(injectors[injectorKey] == null)
    }

    @Test
    fun `잘못된 키로 인젝터를 제거하면 액티비티 인젝터 매니저에서 인젝터가 제거되지 않는다`() {
        // given: 인젝터와 매니저가 주어졌을 때
        val injectorKey = "KEY"
        val injector = mockk<Injector>()

        val injectors = HashMap<String, Injector>().apply {
            put(injectorKey, injector)
        }
        val sut = NonConfigurationActivityInjectorManager(injectors)

        // when: 잘못된 키로 인젝터를 제거하면
        val invalidKey = "INVALID_KEY"
        sut.removeInjector(invalidKey)

        // then: 매니저에서 인젝터가 제거되지 않는다.
        assert(injectors[injectorKey] == injector)
    }

    @Test
    fun `저장된 인젝터 키가 없으면 액티비티 인젝터 매니저에 새로운 인젝터를 반환한다`() {
        // given: 인젝터와 매니저가 주어졌을 때
        val injectorKey = "KEY"
        val injector = mockk<Injector>()
        val injectors = HashMap<String, Injector>().apply {
            put(injectorKey, injector)
        }
        val sut = NonConfigurationActivityInjectorManager(injectors)
        // given:
        val injectableActivity = mockk<InjectableActivity>()
        every { injectableActivity.application } returns InjectableApplication()

        // when: 저장되지 않은 키로 인젝터를 가져오면
        val invalidKey = "INVALID_KEY"
        val actual = sut.getInjector(injectableActivity, invalidKey)

        // then: 새로운 인젝터를 반환한다.
        assertFalse(injector === actual)
    }

    @Test
    fun `저장된 인젝터 키가 있으면 액티비티 인젝터 매니저에 저장된 인젝터를 반환한다`() {
        // given: 인젝터와 매니저가 주어졌을 때
        val injectorKey = "KEY"
        val injector = mockk<Injector>()
        val injectors = HashMap<String, Injector>().apply {
            put(injectorKey, injector)
        }
        val sut = NonConfigurationActivityInjectorManager(injectors)

        // given: 저장된 키로 인젝터를 가져오면
        val injectableActivity = mockk<InjectableActivity>()
        every { injectableActivity.application } returns InjectableApplication()

        // when: 저장된 키로 인젝터를 가져오면
        val actual = sut.getInjector(injectableActivity, injectorKey)

        // then: 저장된 인젝터를 반환한다.
        assert(injector === actual)
    }
}
