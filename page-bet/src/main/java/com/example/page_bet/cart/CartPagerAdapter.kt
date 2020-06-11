package com.example.page_bet.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleIssueResultItem
import com.example.repository.room.Cart
import kotlinx.android.synthetic.main.fragment_cart_list.view.rvCartList

class CartPagerAdapter(var data: MutableList<MutableList<Cart>>, private val callback: CartPageDialog.SetCallback)
    : RecyclerView.Adapter<CartPagerAdapter.BaseViewHolder>() {

//    private var goAppendListAction: ((cart: Cart)->Unit) ? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_cart_list, parent, false), callback)

    override fun getItemCount(): Int = data.size

    fun addData(newData: MutableList<MutableList<Cart>>) {
        data = newData
    }
//
//    fun goAppendListPage(action: (cart: Cart) -> Unit) {
//        this.goAppendListAction = action
//    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
//        holder.listAdapter.setOnItemChildClickListener { adapter, _, position ->
//            val item: Cart = adapter.data[position] as Cart
//            if (item.isAppend) {
//                goAppendListAction?.invoke(item)
//            } else {
//                return@setOnItemChildClickListener
//            }
//        }
    }


    class BaseViewHolder(val view: View, callback: CartPageDialog.SetCallback) : RecyclerView.ViewHolder(view){

        var listAdapter: CartListAdapter = CartListAdapter(mutableListOf(), callback)

        init {
            itemView.rvCartList.let {
                it.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
                it.adapter = listAdapter
            }
        }

        fun bind(data: MutableList<Cart>){
            listAdapter.setNewData(data)
        }
    }
}