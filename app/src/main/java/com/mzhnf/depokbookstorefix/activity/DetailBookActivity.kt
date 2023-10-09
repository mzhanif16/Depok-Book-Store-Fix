package com.mzhnf.depokbookstorefix.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer
import com.mcdev.quantitizerlibrary.QuantitizerListener
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.databinding.ActivityDetailBookBinding
import com.mzhnf.depokbookstorefix.model.cart.CartViewModel
import com.mzhnf.depokbookstorefix.model.login.Data
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class DetailBookActivity : AppCompatActivity(), ResultCallback {
    private lateinit var binding: ActivityDetailBookBinding
    private var totalKuantitas = 0
    private var hargaPerItem = 0
    private var totalBayar = 0
    private lateinit var cartViewModel: CartViewModel
    var progressDialog: Dialog? = null
    private var bookId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        cartViewModel.setResultCallback(this)
        bookId = intent.getIntExtra("bookId", 0).toString()
        Log.d("id", bookId)
        val title = intent.getStringExtra("title").toString()
        val author = intent.getStringExtra("author").toString()
        val publicationDate = intent.getStringExtra("publicationDate").toString()
        val image = intent.getStringExtra("image").toString()
        val genre = intent.getStringExtra("genre").toString()
        val price = intent.getStringExtra("price").toString()
        val rating = intent.getFloatExtra("rating", 0.0f)
        val format = DecimalFormat("#0.0")
        val formattedRating = format.format(rating.toDouble())
        binding.tvRate.text = formattedRating
        val ratingBar = binding.rbBook
        ratingBar.setIsIndicator(true)
        binding.rbBook.rating = rating
        binding.tvTitle.text = title
        binding.tvAuthor.text = author
        binding.tvDate.text = publicationDate
        binding.tvGenre.text = genre
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(price.toInt())
        binding.tvPrice.text = formattedPrice
        Log.d("total", totalKuantitas.toString())
        Log.d("total", totalBayar.toString())
        Glide.with(this)
            .load("http://192.168.1.7:8000/storage/assets/" + image)
            .into(binding.ivGambar)

        val hq1: HorizontalQuantitizer = findViewById(R.id.hq1)
        hargaPerItem = price.toInt()
        hq1.setMinusIconColor(R.color.primary)
        hq1.setPlusIconColor(R.color.primary)
        hq1.setPlusIconBackgroundColor(R.color.white)
        hq1.setMinusIconBackgroundColor(R.color.white)
        hq1.setQuantitizerListener(object : QuantitizerListener {
            override fun onDecrease() {
                if (totalKuantitas > 0) {
                    totalKuantitas
                    totalBayar == hargaPerItem
                    updateTotalViews()
                }
            }

            override fun onIncrease() {
                totalKuantitas
                totalBayar == hargaPerItem
                updateTotalViews()
            }

            override fun onValueChanged(value: Int) {
                totalKuantitas = value
                totalBayar = totalKuantitas * hargaPerItem
                binding.tvTotalkuantitas.text = totalKuantitas.toString()
                binding.tvTotalbayar.text = DecimalFormat("#,##0.00").format(totalBayar)
            }

        })
        binding.btnAddCart.setOnClickListener {
            if (totalKuantitas == 0 && totalBayar == 0) {
                Toast.makeText(this, "Silahkan tambahkan buku dulu", Toast.LENGTH_SHORT).show()
            } else {
                val bookId = bookId
                val totalKuantitas = totalKuantitas.toString()
                val totalBayar = totalBayar.toString()
                val depokBookStore = application as DepokBookStore
                val user = depokBookStore.getUser()
                val token = depokBookStore.getToken()
                val tokenHeader = "Bearer $token"
                val userResponse = Gson().fromJson(user, Data::class.java)
                onShowLoading()
                cartViewModel.addCart(
                    userResponse.id.toString(),
                    bookId,
                    totalBayar,
                    totalKuantitas,
                    tokenHeader
                )

            }
        }
        cartViewModel.cartSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                val response = cartViewModel.response.value
                if (response != null) {
                    Toast.makeText(this, "Berhasil Menambahkan Cart", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Gagal Menambahkan Cart", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTotalViews() {
        binding.tvTotalkuantitas.text = totalKuantitas.toString()
        binding.tvTotalbayar.text = DecimalFormat("#,##0.00").format(totalBayar)
    }

    private fun initView() {
        progressDialog = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onShowLoading() {
        progressDialog?.show()
    }

    override fun onDismissLoading() {
        progressDialog?.dismiss()
    }
}