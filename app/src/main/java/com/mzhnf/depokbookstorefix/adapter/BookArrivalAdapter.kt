package com.mzhnf.depokbookstorefix.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.dummy.BookGenre
import com.mzhnf.depokbookstorefix.model.home.DataHome
import com.mzhnf.depokbookstorefix.model.home.DataSearch

class BookArrivalAdapter() :
    RecyclerView.Adapter<BookArrivalAdapter.ViewHolder>() {
    private val bookList = ArrayList<DataSearch>()
    private var onItemClick: ((BookGenre) -> Unit)? = null
    private val baseURL = "http://192.168.1.7:8000/storage/assets/"

    fun setData(newListData: List<DataSearch>) {
        if (newListData == null) return
        bookList.clear()
        bookList.addAll((newListData))
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGambar: ImageView = itemView.findViewById(R.id.iv_gambar)
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val tv_author: TextView = itemView.findViewById(R.id.tv_author)
        fun bind(bookGenre: DataSearch) {
            itemView.apply {
                Glide.with(context)
                    .load(baseURL+bookGenre.image)
                    .into(ivGambar)
                tv_title.text = bookGenre.title
                tv_author.text = bookGenre.author
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookGenre = bookList[position]
        holder.bind(bookGenre)
    }
}