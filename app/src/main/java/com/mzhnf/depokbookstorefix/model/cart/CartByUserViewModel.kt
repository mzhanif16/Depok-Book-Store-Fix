package com.mzhnf.depokbookstorefix.model.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzhnf.depokbookstorefix.model.home.SearchModelDTO
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
class CartByUserViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _getCartSuccess = MutableLiveData<Boolean>()
    val getCartSuccess: LiveData<Boolean> = _getCartSuccess

    private val _response = MutableLiveData<CartByUserModelDTO>()
    val response: LiveData<CartByUserModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }

    fun getCart(id: String,token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.getCart(id,token)
            call.enqueue(object : Callback<CartByUserModelDTO> {
                override fun onResponse(
                    call: Call<CartByUserModelDTO>,
                    response: Response<CartByUserModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val cartByUserModelDTO = response.body()
                        _response.postValue(cartByUserModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        ongetCartSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onSearchFailed()
                    }
                }

                override fun onFailure(call: Call<CartByUserModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onSearchFailed()
                }
            })
        }
    }

    private fun ongetCartSuccess() {
        _getCartSuccess.postValue(true)
    }


    private fun onSearchFailed() {
        _getCartSuccess.postValue(false)
    }
}