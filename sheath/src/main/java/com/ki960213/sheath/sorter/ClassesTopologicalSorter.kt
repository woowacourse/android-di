package com.ki960213.sheath.sorter

import java.lang.reflect.Constructor
import java.util.LinkedList
import java.util.Queue

object ClassesTopologicalSorter {
    fun sort(classes: List<Class<*>>): List<Class<*>> {
        val inDegree: MutableList<Int> = MutableList(classes.size) { 0 }
        val graph: List<MutableList<Int>> = List(classes.size) { mutableListOf() }
        for (classIndex in classes.indices) {
            val clazz = classes[classIndex]
            val constructor = clazz.primaryConstructor() ?: continue
            val parameterClasses = constructor.parameters.map { it.type as Class<*> }
            parameterClasses.forEach { parameterClass ->
                val findClasses = classes.filter { parameterClass.isAssignableFrom(it) }
                when {
                    findClasses.isEmpty() -> throw IllegalStateException("${parameterClass.name} 클래스가 존재하지 않습니다.")
                    findClasses.size >= 2 -> throw IllegalStateException("${parameterClass.name} 클래스에 주입될 클래스가 모호합니다.")
                }
                val findClass = findClasses[0]
                val findClassIndex = classes.indexOf(findClass)
                graph[findClassIndex].add(classIndex)
                inDegree[classIndex]++
            }
        }
        val result: MutableList<Int> = mutableListOf()
        val queue: Queue<Int> = LinkedList()
        classes.forEachIndexed { index, _ -> if (inDegree[index] == 0) queue.add(index) }
        for (index in (classes.indices)) {
            val currentIndex = queue.poll() ?: throw IllegalStateException("클래스들 간에 의존 사이클이 존재합니다.")
            result.add(currentIndex)
            graph[currentIndex].forEach {
                if (--inDegree[it] == 0) queue.add(it)
            }
        }
        return result.map { classes[it] }
    }

    private fun Class<*>.primaryConstructor(): Constructor<*>? =
        this.declaredConstructors.firstOrNull()
}
