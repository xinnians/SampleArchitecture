package com.example.base

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.hadilq.liveevent.LiveEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

inline fun unless(codition: Boolean, crossinline block: () -> Unit) {
    if (codition) {
        block.invoke()
    }
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToBoolean: Boolean = false): View =
    LayoutInflater.from(this.context).inflate(layout, this, attachToBoolean)

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

inline fun View.onClick(crossinline clickAction: () -> Unit) = this.setOnClickListener {
    clickAction.invoke()
}

fun Fragment.drawable(resId: Int) = requireContext().drawable(resId)

fun Context.drawable(resId: Int) = ContextCompat.getDrawable(this, resId)

/**
 * Covert dp to px
 * @param dp
 * @param context
 * @return pixel
 */
fun dpToPx(dp: Float, context: Context): Float {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt().toFloat()
}

inline fun AppCompatActivity.Dialog(@LayoutRes layout: Int, title: String,
                                    content: String = "",
                                    negativeButton: String = "取消",
                                    positiveButton: String = "退出",
                                    crossinline onNegativeButtonClicked: () -> Unit,
                                    crossinline onPositiveButtonClicked: () -> Unit) {
    android.app.Dialog(this).apply {
        setContentView(layout)
        findViewById<TextView>(R.id.alert_title).text = title
        with(findViewById<TextView>(R.id.alert_content)) { if (content.isBlank()) gone() else text = content }
        findViewById<TextView>(R.id.alert_negative).onClick { this.dismiss(); onNegativeButtonClicked.invoke() }
        findViewById<TextView>(R.id.alert_negative).text = negativeButton
        findViewById<TextView>(R.id.alert_positive).text = positiveButton
        findViewById<TextView>(R.id.alert_positive).onClick { this.dismiss(); onPositiveButtonClicked.invoke() }
    }.show()
}

fun getDateTime(millionsecond: Long): String {
    val date = Date(millionsecond)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(date)
}

fun getMonth(date: Date): String {
    return DateFormat.format("MM", date).toString()
}

fun getDay(date: Date): String {
    return DateFormat.format("dd", date).toString()
}

fun convertDate(time: String): Long {
    return SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault()).parse(time).time
}

fun getMonAndDay(date: String): String {
    val millionSecond = SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.getDefault()).parse(date).time
    val zoneTimeMillionSecond = millionSecond + Calendar.getInstance().timeZone.rawOffset
    val dateTime = Date(zoneTimeMillionSecond)
    val dateFormatter = SimpleDateFormat("M月d日", Locale.getDefault())
    return dateFormatter.format(dateTime)
}

inline fun <T> createBaseAdpater(itemlist: List<T>,
                                 context: Context, @LayoutRes layout: Int,
                                 crossinline bindView: (item: T, view: View) -> Unit) = object : BaseAdapter() {

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View? {
        var rootView = convertview
        if (rootView == null) {
            rootView = LayoutInflater.from(context).inflate(layout, parent, false)
        }
        bindView(itemlist[position], rootView!!)


        return rootView
    }

    override fun getItem(position: Int) = itemlist.get(position)

    override fun getItemId(p0: Int): Long = 0

    override fun getCount(): Int = itemlist.size

}

fun showSoftKeyboard(view: View, mContext: Context) {
    if (view.requestFocus()) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun closeSoftKeyboard(mEditText: EditText, mContext: Context) {
    val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
}

fun String.isNumeric(): Boolean{
    this.forEach {
        if(!Character.isDigit(it)){
            return false
        }
    }
    return true
}

fun Int.isOdd(): Boolean {
    return this % 2 != 0
}

fun <T> MutableLiveData<T>.toSingleEvent(): MutableLiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}