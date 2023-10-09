package com.mzhnf.depokbookstorefix.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mcdev.quantitizerlibrary.QuantitizerListener
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.databinding.ItemCartBinding
import com.mzhnf.depokbookstorefix.model.cart.DataCartByUser
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private val listData: ArrayList<DataCartByUser>,
    private val itemAdapterCallback: ItemAdapterCallback,
    private val onTotalKuantitasUpdated: (Int) -> Unit,
    private val onTotalBayarUpdated: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val selectedValues = HashMap<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val data = listData[position]
        //validasi data apakah outlet id yang berada di outlet sama dengan outlet id yang ada di layanan
            holder.bind(data, itemAdapterCallback)
            holder.updateTotalBayar()
            holder.updateTotalKuantitas(data, itemAdapterCallback)

    }

    fun setData(newListData: List<DataCartByUser>) {
        if (newListData == null) return
        listData.clear()
        listData.addAll((newListData))
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    //menghitung total bayar berdasarkan nilai kuantitas yang dipilih untuk setiap layanan.
    fun calculateTotalBayar() {
        var totalBayar = 0
        for ((position, value) in selectedValues) {
            val data = listData[position]
            val harga = data.totalPrice.toInt()
            val totalHargaItem = harga * value
            totalBayar += totalHargaItem
        }
        onTotalBayarUpdated(totalBayar)
    }

    //menghitung total kuantitas berdasarkan nilai kuantitas yang dipilih untuk setiap layanan.
    fun calculateTotalKuantitas() {
        var totalKuantitas = 0
        for (value in selectedValues.values) {
            totalKuantitas += value
        }
        onTotalKuantitasUpdated(totalKuantitas)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCartBinding.bind(itemView)
        init {
            binding.hqCart.setMinusIconColor(R.color.primary)
            binding.hqCart.setPlusIconColor(R.color.primary)
            binding.hqCart.setPlusIconBackgroundColor(R.color.white)
            binding.hqCart.setMinusIconBackgroundColor(R.color.white)
            binding.hqCart.setQuantitizerListener(object : QuantitizerListener {
                override fun onDecrease() {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val data = listData[position]
                        val currentValue = selectedValues[position] ?: 0
                        if (currentValue > 0) {
                            selectedValues[position] = currentValue
                            data.kuantitas = currentValue
                            updateTotalBayar()
                            updateTotalKuantitas(data, itemAdapterCallback)
                        }
                    }
                }

                override fun onIncrease() {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val data = listData[position]
                        val currentValue = selectedValues[position] ?: 0
                        selectedValues[position] = currentValue
                        data.kuantitas = currentValue
                        updateTotalBayar()
                        updateTotalKuantitas(data, itemAdapterCallback)
                    }
                }

                override fun onValueChanged(value: Int) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val data = listData[position]
                        selectedValues[position] = value
                        data.kuantitas = value
                        updateTotalBayar()
                        updateTotalKuantitas(data, itemAdapterCallback)
                    }
                }
            })
        }

        fun bind(data: DataCartByUser, itemAdapterCallback: ItemAdapterCallback) {
            itemView.apply {
                binding.tvTitleCart.text = data.book.title
                val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(data.book.price.toInt())
                binding.tvPriceCart.text = formattedPrice
                binding.tvAuthorCart.text = data.book.author
                Log.d("test", data.book.author)
                Glide.with(this)
                    .load("http://192.168.1.7:8000/storage/assets/"+data.book.image)
                    .into(binding.ivGambar)
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val value = selectedValues[position] ?: 0
                    binding.hqCart.value = value
                }
            }
        }

        fun updateTotalBayar() {
            calculateTotalBayar()
        }

        fun updateTotalKuantitas(data: DataCartByUser, itemAdapterCallback: ItemAdapterCallback) {
            calculateTotalKuantitas()
            itemAdapterCallback.onClick(data)
        }
    }

    interface ItemAdapterCallback {
        fun onClick(data: DataCartByUser)
    }
}