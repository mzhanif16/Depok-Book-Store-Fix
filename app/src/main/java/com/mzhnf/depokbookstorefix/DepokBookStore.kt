package com.mzhnf.depokbookstorefix

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.mzhnf.depokbookstorefix.network.ApiService
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class DepokBookStore : Application() {
    @Inject
    lateinit var apiService: ApiService

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        // Cek apakah token sudah tersimpan, jika ya, konfigurasikan ApiService
        val token = getToken()
        if (token != null) {
            buildRetrofitClient(token)
        }
    }

    fun getPreferences(): SharedPreferences {
        return sharedPreferences
    }

    fun isLoggedIn(): Boolean {
        val token = getToken()
        return !token.isNullOrBlank()
    }

    // Menyimpan token ke dalam SharedPreferences dan mengkonfigurasi ApiService
    fun setToken(token: String) {
        sharedPreferences.edit().putString("PREFERENCES_TOKEN", token).apply()
        buildRetrofitClient(token)
    }

    // Mengambil token dari SharedPreferences
    fun getToken(): String? {
        return sharedPreferences.getString("PREFERENCES_TOKEN", null)
    }

    // Menghapus token dari SharedPreferences dan menghapus konfigurasi ApiService
    fun removeToken() {
        sharedPreferences.edit().remove("PREFERENCES_TOKEN").apply()
    }

    fun setUser(user: String) {
        sharedPreferences.edit().putString("PREFERENCES_USER", user).apply()
    }

    // Mengambil data pengguna dari SharedPreferences
    fun getUser(): String? {
        return sharedPreferences.getString("PREFERENCES_USER", null)
    }

    // Mengkonfigurasi Retrofit dengan token
    private fun buildRetrofitClient(token: String) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }
}