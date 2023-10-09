package com.mzhnf.depokbookstorefix.model.register

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
class RegisterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    private val _response = MutableLiveData<RegisterModelDTO>()
    val response: LiveData<RegisterModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }
    fun register(email: String, password: String, username: String, address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.register(email, password,username,address)
            call.enqueue(object : Callback<RegisterModelDTO> {
                override fun onResponse(
                    call: Call<RegisterModelDTO>,
                    response: Response<RegisterModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val registerModelDTO = response.body()
                        _response.postValue(registerModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        onLoginSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onLoginFailed()
                    }
                }

                override fun onFailure(call: Call<RegisterModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onLoginFailed()
                }
            })
        }
    }
    // Fungsi untuk menandai keberhasilan login
    private fun onLoginSuccess() {
        _registerSuccess.postValue(true)
    }

    // Fungsi untuk menandai kegagalan login
    private fun onLoginFailed() {
        _registerSuccess.postValue(false)
    }
}

