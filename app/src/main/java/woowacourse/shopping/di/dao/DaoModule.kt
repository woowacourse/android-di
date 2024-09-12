package woowacourse.shopping.di.dao

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.woowa.di.injection.Module
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

class DaoModule private constructor(private val context: Context) :
    Module<DaoModule, DaoDI>,
    DefaultLifecycleObserver {
        private lateinit var daoes: List<Pair<String, KFunction<DaoDI>>>
        private lateinit var daoBinder: DaoBinder

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            daoBinder = DaoBinder(context)

            daoes = createRepositories()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            instance = null
            owner.lifecycle.removeObserver(this)
        }

        private fun createRepositories(): List<Pair<String, KFunction<DaoDI>>> {
            return DaoBinder::class.declaredMemberFunctions
                .filter { it.returnType.jvmErasure.isSubclassOf(DaoDI::class) }
                .map { kFunction ->
                    val key =
                        kFunction.returnType.jvmErasure.simpleName
                            ?: error("$kFunction 의 key값을 지정할 수 없습니다.")
                    key to (kFunction as KFunction<DaoDI>)
                }
        }

        override fun getDIInstance(type: KClass<out DaoDI>): DaoDI {
            val kFunction =
                instance?.daoes?.find { it.first == type.simpleName }?.second
                    ?: error("${type.simpleName} 해당 interface에 대한 객체가 없습니다.")
            return kFunction.call(daoBinder)
        }

        override fun getDIInstance(
            type: KClass<out DaoDI>,
            qualifier: KClass<out Annotation>,
        ): DaoDI {
            val kFunction =
                instance?.daoes?.find {
                    it.first == type.simpleName &&
                        it.second.annotations.any { it.annotationClass.isSubclassOf(qualifier) }
                }?.second
                    ?: error("${type.simpleName} 해당 interface에 대한 객체가 없습니다.")
            return kFunction.call(daoBinder)
        }


        companion object {
            private var instance: DaoModule? = null

            fun initLifeCycle(context: Context) {
                require(instance == null) {
                    "Module은 한번만 초기화가 가능합니다."
                }

                val newInstance = DaoModule(context)
                when (context) {
                    is Application -> ProcessLifecycleOwner.get().lifecycle.addObserver(newInstance)
                    is AppCompatActivity -> context.lifecycle.addObserver(newInstance)
                    is Fragment -> context.lifecycle.addObserver(newInstance)
                    else -> error("해당 로직은 Application, AppCompatActivity, Fragment에서만 쓸 수 있습니다.")
                }
                instance = newInstance
            }

            fun getInstance(): DaoModule = instance ?: error("Module에 대한 lifecycle이 지정되지 않았습니다.")

            fun getInstanceOrNull(): DaoModule? = instance
        }
    }
