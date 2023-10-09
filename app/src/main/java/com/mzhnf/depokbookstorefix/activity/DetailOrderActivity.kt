package com.mzhnf.depokbookstorefix.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.databinding.ActivityDetailOrderBinding
import com.mzhnf.depokbookstorefix.databinding.ActivityMainBinding

class DetailOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val orderId = intent.getIntExtra("orderId", 0).toString()
        val email = intent.getStringExtra("email").toString()
        val username = intent.getStringExtra("username").toString()
        val address = intent.getStringExtra("address").toString()
        val statusBayar = intent.getIntExtra("statusBayar",0)
        val orderDate = intent.getStringExtra("orderDate").toString()
        val totalKuantitas = intent.getStringExtra("totalKuantitas").toString()
        val totalBayar = intent.getStringExtra("totalBayar").toString()
        val deliveryStatus = intent.getIntExtra("deliveryStatus", 0)
        binding.tvIdOrder.text = orderId
        binding.tvEmailDetail.text = email
        binding.tvNamauser.text = username
        binding.tvAlamat.text = address
        if(statusBayar == 1){
            binding.tvStatusBayar.text = "Lunas"
        }else{
            binding.tvStatusBayar.text = "Belum Lunas"
        }
        binding.tvWaktupesan.text = orderDate
        binding.tvTotalkuantitas.text = totalKuantitas
        binding.tvTotalBayar.text = totalBayar
        if(deliveryStatus==1){
            binding.tvStatusTransaksi.text = "DELIVERED"
        }else{
            binding.tvStatusTransaksi.text = "ON PROGRESS"
        }
    }
}