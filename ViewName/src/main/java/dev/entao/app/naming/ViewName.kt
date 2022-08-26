@file:Suppress("unused")

package dev.entao.app.naming

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment


var View.name: String?
    get() {
        return this.getTag(R.id.viewName) as? String
    }
    set(value) {
        this.setTag(R.id.viewName, value)
    }


@Suppress("UNCHECKED_CAST")
fun <T : View> View.findViewByName(viewName: String): T? {
    if (this.name == viewName) return this as? T
    if (this is ViewGroup) {
        for (child in this.children) {
            val v: T? = child.findViewByName(viewName)
            if (v != null) return v
        }
    }
    return null
}

fun View.sibling(viewName: String): View {
    val pv: ViewGroup = this.parent as? ViewGroup ?: throw IllegalStateException("NO parent view")
    for (child in pv.children) {
        if (child.name == viewName) return child
    }
    throw IllegalArgumentException("NO sibling view named $viewName ")
}

@Suppress("UNCHECKED_CAST")
fun <T : View> Activity.findByName(viewName: String): T? {
    val contentView: View = this.findViewById(android.R.id.content) ?: return null
    return contentView.findViewByName(viewName)
}

@Suppress("UNCHECKED_CAST")
fun <T : View> Fragment.findByName(viewName: String): T? {
    val contentView: View = this.view ?: return null
    return contentView.findViewByName(viewName)
}
