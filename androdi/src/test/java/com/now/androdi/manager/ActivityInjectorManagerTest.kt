package com.now.androdi.manager

import com.now.di.Container
import com.now.di.Injector
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class ActivityInjectorManagerTest {
    @Test
    fun `Injector를 ActivityInjectorManager에 저장한다`() {
        // given
        val activityInjectorManager = ActivityInjectorManager()
        val injector = Injector(Container(null))
        activityInjectorManager.saveInjector("krrong", injector)

        // when
        val expected = activityInjectorManager.getInjector("krrong")

        // then
        assertNotNull(expected)
    }

    @Test
    fun `ActivityInjectorManager에 저장된 Injector를 삭제한다`() {
        // given
        val activityInjectorManager = ActivityInjectorManager()
        val injector = Injector(Container(null))
        activityInjectorManager.saveInjector("krrong", injector)

        // when
        activityInjectorManager.removeInjector("krrong")
        val expected = activityInjectorManager.getInjector("krrong")

        // then
        assertNull(expected)
    }
}
