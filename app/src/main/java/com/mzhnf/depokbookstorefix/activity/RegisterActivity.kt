package com.mzhnf.depokbookstorefix.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.databinding.ActivityLoginBinding
import com.mzhnf.depokbookstorefix.databinding.ActivityRegisterBinding
import com.mzhnf.depokbookstorefix.model.login.LoginViewModel
import com.mzhnf.depokbookstorefix.model.register.RegisterViewModel
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity(), ResultCallback {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    var progressDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.setResultCallback(this)
        binding.btnRegist.setOnClickListener {
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            val username = binding.tvPassword.text.toString()
            val address = binding.tvPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && address.isNotEmpty()) {
                // Tampilkan ProgressBar saat tombol login diklik
                onShowLoading()
                viewModel.register(email, password,username,address)
            } else {
                // Tampilkan pesan kepada pengguna bahwa email dan password harus diisi
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.registerSuccess.observe(this, Observer { isSuccess->
            val response = viewModel.response
            if(isSuccess){
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"Register Gagal",Toast.LENGTH_SHORT).show()
            }
        })
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