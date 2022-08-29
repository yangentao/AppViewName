@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.entao.app.viewname

import android.app.Activity
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KProperty


private val LifecycleOwner.isDestroyed: Boolean get() = this.lifecycle.currentState == Lifecycle.State.DESTROYED

object BinderConfig {
    var findViewCallback: (thisRef: LifecycleOwner, String) -> View? = { that, vname ->
        when (that) {
            is Activity -> that.findByName(vname)
            is Fragment -> that.findByName(vname)
            else -> null
        }
    }
}

/**
 * Support String/Long/Int/Boolean
 */
class BindValue<T : Any>(private val viewName: String? = null) {
    var onSetCallback: ((View, T) -> Unit)? = null
    var onGetCallback: ((View) -> T)? = null

    private fun findView(thisRef: LifecycleOwner, property: KProperty<*>): View {
        val vName = viewName ?: property.name
        return BinderConfig.findViewCallback(thisRef, vName) ?: error("NO view named $vName")
    }


    operator fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
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
    operator fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): T {
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
