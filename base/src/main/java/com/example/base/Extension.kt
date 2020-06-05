package com.example.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

fun Fragment.toast(text: CharSequence) = requireActivity().toast(text)

fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
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

//fun AppCompatActivity.switchFragmentToStack(@IdRes idRes: Int, fragment: androidx.fragment.app.Fragment, tag: String? = null) {
//    this.supportFragmentManager?.inStackTransaction { add(idRes, fragment, tag) }
//}
//
//fun AppCompatActivity.switchFragment(@IdRes idRes: Int, fragment: androidx.fragment.app.Fragment, tag: String? = null) {
//    this.supportFragmentManager?.inTransaction { add(idRes, fragment, tag) }
//}
//inline fun androidx.fragment.app.FragmentManager.inTransaction(crossinline action: androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction) {
//    this.beginTransaction().setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.right_in, R.anim.right_out)
//        .action().commit()
//}
//
//inline fun androidx.fragment.app.FragmentManager.inStackTransaction(crossinline action: androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction) {
//    this.beginTransaction().setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.right_in, R.anim.right_out)
//        .action().addToBackStack(null).commit()
//}
//
//inline fun AppCompatActivity.Dialog(@LayoutRes layout: Int, title: String,
//                                    content: String = "",
//                                    negativeButton: String = "取消",
//                                    positiveButton: String = "退出",
//                                    crossinline onNegativeButtonClicked: () -> Unit,
//                                    crossinline onPositiveButtonClicked: () -> Unit) {
//    android.app.Dialog(this).apply {
//        setContentView(layout)
//        findViewById<TextView>(R.id.alert_title).text = title
//        with(findViewById<TextView>(R.id.alert_content)) { if (content.isBlank()) gone() else text = content }
//        findViewById<TextView>(R.id.alert_negative).onClick { this.dismiss(); onNegativeButtonClicked.invoke() }
//        findViewById<TextView>(R.id.alert_negative).text = negativeButton
//        findViewById<TextView>(R.id.alert_positive).text = positiveButton
//        findViewById<TextView>(R.id.alert_positive).onClick { this.dismiss(); onPositiveButtonClicked.invoke() }
//    }.show()
//}

fun getDateTime(millionsecond: Long): String {
    val date = Date(millionsecond)
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(date)
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



var extensionProgress: AlertDialog? = null

//fun runProgress(myContext: Context, loading: Boolean) {
//    if (extensionProgress == null) {
//        extensionProgress = AlertDialog.Builder(myContext, R.style.DialogBackgroundNull).setCancelable(true)
//            .setView(R.layout.loading_progress).create()
//    }
//    when (loading) {
//        true -> extensionProgress!!.show()
//        false -> {
//            extensionProgress!!.dismiss()
//            extensionProgress = null
//        }
//    }
//}