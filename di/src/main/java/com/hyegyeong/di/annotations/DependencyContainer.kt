//package com.hyegyeong.di.annotations
//
//import com.hyegyeong.di.DependencyModule
//import com.hyegyeong.di.Injector
//import java.lang.IllegalArgumentException
//import kotlin.reflect.KClass
//import kotlin.reflect.KFunction
//import kotlin.reflect.full.declaredFunctions
//import kotlin.reflect.full.hasAnnotation
//import kotlin.reflect.full.valueParameters
//import kotlin.reflect.jvm.jvmErasure
//
//class DependencyContainer(private val dependencyModule: DependencyModule) {
//    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
//
//    fun provideInstance(kClass: KClass<*>, annotations: List<Annotation>): Any {
//        // 싱글톤이면, 일단 instances 에 해당 인스턴스 있는지 확인하고, 있으면 그거 꺼내오고, 없으면 생성해서 넣어주기
//        val containerFunctions: Collection<KFunction<*>> = dependencyModule::class.declaredFunctions
//        val function: KFunction<*> = containerFunctions.first {
//            (it.returnType.jvmErasure == kClass) && it.annotations.containsAll(annotations)
//        }
//        val instances = mutableListOf<Any>()
//        if (function.valueParameters.isNotEmpty()) {
//            function.valueParameters.forEach {
//                instances.add(
//                    provideInstance(
//                        it.type.jvmErasure,
//                        it.annotations.filter { annotation -> annotation.annotationClass.hasAnnotation<Qualifier>() }
//                    )
//                )
//            }
//        }
//        return function.call(Injector.container, * instances.toTypedArray())
//            ?: throw IllegalArgumentException("해당 함수를 찾을 수 없습니다.")
//    }
//
//}