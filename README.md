# AppViewName
Android library, provide View.name property. 

example:
```kotlin
class MainActivity : AppCompatActivity() {
    //property name equal TextView.name
    //or, use BindInt("numValue") specify the TextView.name
    var numValue: Int by BindInt()

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

        Log.d("debug", numValue.toString())
        numValue = 999
        Log.d("debug", numValue.toString())
    }
}
```
