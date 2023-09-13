package com.now.di

import com.now.annotation.Inject
import com.now.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

object Injector {
    // 인자로 받은 모듈에 있는 메서드를 인스턴스화 하여 Container에 저장한다
    fun addModule(module: Module) {
        val kFunctions = module::class.declaredFunctions
        kFunctions.forEach { kFunction ->
            createOrAdd(module, kFunction)
        }
    }

    private fun createOrAdd(receiver: Module, kFunction: KFunction<*>) {
        val functionReturnType = kFunction.returnType
        val qualifier = kFunction.annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }

        val dependencyType = DependencyType(functionReturnType.jvmErasure, qualifier)

        // 컨테이너에 이미 있다면 종료
        if (Container.getInstance(dependencyType) != null) return

        // Container에 없는 경우
        // 함수에 파라미터가 없는 경우 인스턴스를 생성하고 Container에 추가 후 종료
        if (kFunction.valueParameters.isEmpty()) {
            val kclass = kFunction.call(receiver)
            kclass?.let { Container.addInstance(functionReturnType.jvmErasure, it, qualifier) }
                ?: throw IllegalArgumentException("문제 생김")
            return
        }

        // 함수에 파라미터가 있는 경우 컨테이너에서 가져오거나 생성해야 함
        val requiredParams = kFunction.valueParameters.map { kParameter ->
            getParameterInstance(receiver, kFunction, kParameter)
        }

        kFunction.call(receiver, *requiredParams.toTypedArray())?.let {
            Container.addInstance(functionReturnType.jvmErasure, it, qualifier)
        }
    }

    private fun getParameterInstance(
        receiver: Module,
        kFunction: KFunction<*>,
        kParameter: KParameter,
    ): Any {
        val klass = kParameter.type.jvmErasure
        val annotation = kParameter.findAnnotation<Qualifier>()
        val dependencyType = DependencyType(klass, annotation)

        // 파라미터가 컨테이너에 없는 경우 재귀적 호출을 통해 컨테트이너에 추가로 저장
        if (Container.getInstance(dependencyType) == null) {
            createOrAdd(receiver, kFunction)
        }

        // 재귀적 호출을 통해 컨테이너에 저장했기 때문에 무조건 있음
        return Container.getInstance(dependencyType)!!
    }

    // klass의 인스턴스를 생성하여 반환한다
    fun <T : Any> inject(klass: KClass<*>): T {
        //  주생성자를 가져온다
        val primaryConstructor =
            klass.primaryConstructor ?: throw NullPointerException("주 생성자가 없습니다.")

        // 인자들 중 Inject 어노테이션 붙은 인자들만 가져온다
        val parameters = primaryConstructor.parameters.filter { it.hasAnnotation<Inject>() }

        // 주생성자의 인자들을 인스턴스화 시킨다
        // Container에 있는 경우 바로 가져오고 없다면 인스턴스를 생성한다
        val insertedParameters = parameters.associateWith { kParameter ->
            val type = kParameter.type.jvmErasure
            val annotation = kParameter.annotations.firstOrNull { _annotation ->
                _annotation.annotationClass.hasAnnotation<Qualifier>()
            }
            Container.getInstance(DependencyType(type, annotation))
        }

        return primaryConstructor.callBy(insertedParameters) as T
    }
}
