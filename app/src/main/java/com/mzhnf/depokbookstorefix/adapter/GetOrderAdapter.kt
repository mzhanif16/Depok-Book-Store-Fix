package com.mzhnf.depokbookstorefix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.databinding.ItemBookBinding
import com.mzhnf.depokbookstorefix.databinding.ItemOrderBinding
import com.mzhnf.depokbookstorefix.dummy.BookGenre
import com.mzhnf.depokbookstorefix.model.cart.DataCartByUser
import com.mzhnf.depokbookstorefix.model.home.DataHome
import com.mzhnf.depokbookstorefix.model.home.DataSearch
import com.mzhnf.depokbookstorefix.model.order.DataOrderByUser

class GetOrderAdapter(private val listener: BookGenreClickListener) :
    RecyclerView.Adapter<GetOrderAdapter.ViewHolder>() {
    private val bookList = ArrayList<DataOrderByUser>()
    private val baseURL = "http://192.168.1.7:8000/storage/assets/"

    fun setData(newListData: List<DataOrderByUser>) {
        if (newListData == null) return
        bookList.clear()
        bookList.addAll((newListData))
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemOrderBinding.bind(itemView)
        fun bind(bookGenre: DataOrderByUser) {
            itemView.apply {
                binding.tvOrderId.text = bookGenre.id.toString()
                binding.tvEmail.text = bookGenre.user.email
                binding.tvOrderDate.text = bookGenre.orderDate
                binding.tvTotalPrice.text = bookGenre.totalPrice
                binding.cvOrder.setOnClickListener {
                    listener.bookGenreClickListener(bookGenre)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookGenre = bookList[position]
        holder.bind(bookGenre)
    }
    interface BookGenreClickListener {
        fun bookGenreClickListener(item: DataOrderByUser)
    }
}