@file:Suppress("unused")

package dev.entao.app.viewname

import android.app.Activity
import android.view.View
import android.widget.Checkable
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

class BindString(private val viewName: String? = null) {
    var onSetCallback: (TextView, String) -> Unit = { tv, s -> tv.text = s }
    var onGetCallback: (TextView) -> String = { it.text.toString() }

    operator fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: String) {
        if (thisRef.isDestroyed) return
        val tv: TextView = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? TextView ?: return
        onSetCallback(tv, value)
    }

    operator fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): String {
        val tv: TextView = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? TextView ?: return ""
        return onGetCallback(tv)
    }
}

class BindBool(private val viewName: String? = null) {
    var onSetCallback: (Checkable, Boolean) -> Unit = { v, s -> v.isChecked = s }
    var onGetCallback: (Checkable) -> Boolean = { it.isChecked }

    operator fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: Boolean) {
        if (thisRef.isDestroyed) return
        val v: Checkable = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? Checkable ?: return
        onSetCallback(v, value)
    }

    operator fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): Boolean {
        val v: Checkable = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? Checkable ?: return false
        return onGetCallback(v)
    }
}

class BindInt(private val viewName: String? = null, private val defaultValue: Int = 0) {
    var onSetCallback: (TextView, Int) -> Unit = { tv, s -> tv.text = s.toString() }
    var onGetCallback: (TextView) -> Int = { it.text.toString().trim().toIntOrNull() ?: defaultValue }

    operator fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: Int) {
        if (thisRef.isDestroyed) return
        val tv: TextView = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? TextView ?: return
        onSetCallback(tv, value)
    }

    operator fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): Int {
        val tv: TextView = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? TextView ?: return defaultValue
        return onGetCallback(tv)
    }
}

class BindLong(private val viewName: String? = null, private val defaultValue: Long = 0) {
    var onSetCallback: (TextView, Long) -> Unit = { tv, s -> tv.text = s.toString() }
    var onGetCallback: (TextView) -> Long = { it.text.toString().trim().toLongOrNull() ?: defaultValue }

    operator fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: Long) {
        if (thisRef.isDestroyed) return
        val tv: TextView = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? TextView ?: return
        onSetCallback(tv, value)
    }

    operator fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): Long {
        val tv: TextView = BinderConfig.findViewCallback(thisRef, viewName ?: property.name) as? TextView ?: return defaultValue
        return onGetCallback(tv)
    }
}