package com.mzhnf.depokbookstorefix.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            val depokBookStore = application as DepokBookStore

            // Cek apakah pengguna sudah login (token ada)
            if (depokBookStore.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            }
        }, 3000)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}