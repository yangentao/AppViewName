@file:Suppress("MemberVisibilityCanBePrivate")

package dev.entao.app.viewname

import android.app.Activity
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

const val RootViewPropertyName = "rootContentView"

object BinderConfig {
    var findRootViewCallback: ((Any) -> View?)? = null
}

private fun findRootViewByPropertyName(container: Any): View? {
    for (p in container::class.memberProperties) {
        if (p.name != RootViewPropertyName) continue
        if (!(p.returnType.classifier as KClass<*>).isSubclassOf(View::class)) continue
        return p.getter.call(container) as? View
    }
    return null
}

fun findRootViewOfContainer(obj: Any): View? {
    BinderConfig.findRootViewCallback?.invoke(obj)?.also { return it }
    findRootViewByPropertyName(obj)?.also { return it }
    return when (obj) {
        is Activity -> obj.findViewById(android.R.id.content)
        is Fragment -> obj.view
        else -> null
    }
}


/**
 *  val pickView: ImageView by NamedView("pick")
 *  if viewName is null, use property.name instead.
 */
class NamedView(private val viewName: String? = null) {
    private var realView: View? = null

    private fun findView(thisRef: Any, property: KProperty<*>): View {
        if (realView == null) {
            val vName = viewName ?: property.name
            realView = findRootViewOfContainer(thisRef)?.findViewByName(vName)
        }
        return realView ?: error("NO view named $property")
    }


    @Suppress("UNCHECKED_CAST")
    operator fun <T : View> getValue(thisRef: Any, property: KProperty<*>): T {
        return findView(thisRef, property) as T
    }
}


/**
 * val userName:String by BindValue("userNameEdit")
 * Support String/Long/Int/Boolean
 */
class BindValue<T : Any>(private val viewName: String? = null) {
    var onSetCallback: ((View, T) -> Unit)? = null
    var onGetCallback: ((View) -> T)? = null

    private var realView: View? = null

    private fun findView(thisRef: Any, property: KProperty<*>): View {
        if (realView == null) {
            val vName = viewName ?: property.name
            realView = findRootViewOfContainer(thisRef)?.findViewByName(vName)
        }
        return realView ?: error("NO view named $property")
    }


    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        val v: View = findView(thisRef, property)
        val c = onSetCallback
        if (c != null) return c(v, value)
        when (property.returnType.classifier) {
            String::class -> (v as TextView).text = value.toString()
            Int::class -> (v as TextView).text = value.toString()
            Long::class -> (v as TextView).text = value.toString()
            Boolean::class -> (v as CompoundButton).isChecked = value as Boolean
            else -> error("BindValue: Unsupport property: ${property}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any, property: KProperty<*>): T {
        val v: View = findView(thisRef, property)
        val c = onGetCallback
        if (c != null) return c(v)
        return when (property.returnType.classifier) {
            String::class -> (v as TextView).text.toString() as T
            Int::class -> ((v as TextView).text.toString().toIntOrNull() ?: 0) as T
            Long::class -> ((v as TextView).text.toString().toLongOrNull() ?: 0L) as T
            Boolean::class -> (v as CompoundButton).isChecked as T
            else -> error("BindValue: Unsupport property: $property")
        }
    }
}
