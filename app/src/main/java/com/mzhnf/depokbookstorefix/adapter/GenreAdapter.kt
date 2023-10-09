package com.mzhnf.depokbookstorefix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.dummy.Genre
import com.mzhnf.depokbookstorefix.model.home.DataHome

class GenreAdapter(private val genreList: List<Genre>, private var selectedItemPosition: Int = 0) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    private var onItemClick: ((Genre) -> Unit)? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.genreCardView)
        val genreTextView: TextView = itemView.findViewById(R.id.genreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genreList[position]
        holder.genreTextView.text = genre.name

        if (position == selectedItemPosition) {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(R.color.primary))
            holder.genreTextView.setTextColor(holder.itemView.context.getColor(R.color.white))
        } else {
            holder.cardView.setCardBackgroundColor(holder.itemView.context.getColor(android.R.color.white))
            holder.genreTextView.setTextColor(holder.itemView.context.getColor(R.color.black))
        }
        holder.itemView.setOnClickListener {
            setSelectedItemPosition(position)
            notifyDataSetChanged()
            onItemClick?.invoke(genre)
        }
    }

    override fun getItemCount(): Int {
        return genreList.size
    }
    fun setSelectedItemPosition(position: Int) {
        val previousSelectedItem = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previousSelectedItem) // Perbarui tampilan item sebelumnya
        notifyItemChanged(selectedItemPosition) // Perbarui tampilan item yang dipilih sekarang
    }

    fun setOnItemClickListener(listener: (Genre) -> Unit) {
        onItemClick = listener
    }
}
