package dev.entao.app

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.entao.app.viewname.BindValue
import dev.entao.app.viewname.name

class MainActivity : AppCompatActivity() {

    var numValue: Int by BindValue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ll = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        val tv = TextView(this@MainActivity).apply {
            name = "numValue"
            text = "123"
        }
        val wrap = LinearLayout.LayoutParams.WRAP_CONTENT
        ll.addView(tv, LinearLayout.LayoutParams(wrap, wrap))
        setContentView(ll)

        Log.d("xlog", numValue.toString())
        numValue = 999
        Log.d("xlog", numValue.toString())
    }
}