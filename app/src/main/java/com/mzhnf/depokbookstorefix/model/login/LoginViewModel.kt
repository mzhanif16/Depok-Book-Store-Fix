package com.mzhnf.depokbookstorefix.model.login

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzhnf.depokbookstorefix.repository.Repository
import com.mzhnf.depokbookstorefix.network.ResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _response = MutableLiveData<LoginModelDTO>()
    val response: LiveData<LoginModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.login(email, password)
            call.enqueue(object : Callback<LoginModelDTO> {
                override fun onResponse(
                    call: Call<LoginModelDTO>,
                    response: Response<LoginModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val loginModelDTO = response.body()
                        _response.postValue(loginModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        onLoginSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onLoginFailed()
                    }
                }

                override fun onFailure(call: Call<LoginModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onLoginFailed()
                }
            })
        }
    }
    // Fungsi untuk menandai keberhasilan login
    private fun onLoginSuccess() {
        _loginSuccess.postValue(true)
    }

    // Fungsi untuk menandai kegagalan login
    private fun onLoginFailed() {
        _loginSuccess.postValue(false)
    }
}

