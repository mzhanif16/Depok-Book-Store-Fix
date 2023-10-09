package com.mzhnf.depokbookstorefix.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.databinding.ActivityLoginBinding
import com.mzhnf.depokbookstorefix.model.login.LoginViewModel
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), ResultCallback {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    var progressDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setResultCallback(this)

        binding.btnLogin.setOnClickListener {
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Tampilkan ProgressBar saat tombol login diklik
                onShowLoading()
                viewModel.login(email, password)
            } else {
                // Tampilkan pesan kepada pengguna bahwa email dan password harus diisi
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loginSuccess.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                val response = viewModel.response.value
                if (response!=null){
                    val token = response.token
                    val gson = Gson()
                    val json = gson.toJson(response.data)
                    val depokBookStore = application as DepokBookStore
                    depokBookStore.setToken(token)
                    depokBookStore.setUser(json)
                    Log.d("user", json)
                    Log.d("token", token)
                }
                onLoginSuccess()
            } else {
                // Panggil fungsi onLoginFailed jika login gagal
                onLoginFailed()
            }
        })
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
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
    private fun onLoginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginFailed() {
        Toast.makeText(this, "Login Gagal Username atau Password Salah", Toast.LENGTH_SHORT).show()
    }

    override fun onShowLoading() {
        progressDialog?.show()
    }

    override fun onDismissLoading() {
        progressDialog?.dismiss()
    }
}