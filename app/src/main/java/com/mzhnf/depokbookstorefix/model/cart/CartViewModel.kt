package com.mzhnf.depokbookstorefix.model.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.network.ResultCallback
import com.mzhnf.depokbookstorefix.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _cartSuccess = MutableLiveData<Boolean>()
    val cartSuccess: LiveData<Boolean> = _cartSuccess

    private val _response = MutableLiveData<CartModelDTO>()
    val response: LiveData<CartModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }
    fun addCart(userId: String, bookId: String, totalPrice: String, totalBook: String, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.addToCart(userId, bookId,totalPrice,totalBook, token)
            call.enqueue(object : Callback<CartModelDTO> {
                override fun onResponse(
                    call: Call<CartModelDTO>,
                    response: Response<CartModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val cartModelDTO = response.body()
                        _response.postValue(cartModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        oncartSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onLoginFailed()
                    }
                }

                override fun onFailure(call: Call<CartModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onLoginFailed()
                }
            })
        }
    }
    // Fungsi untuk menandai keberhasilan login
    private fun oncartSuccess() {
        _cartSuccess.postValue(true)
    }

    // Fungsi untuk menandai kegagalan login
    private fun onLoginFailed() {
        _cartSuccess.postValue(false)
    }
}