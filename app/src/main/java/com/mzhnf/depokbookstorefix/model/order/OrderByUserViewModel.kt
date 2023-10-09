package com.mzhnf.depokbookstorefix.model.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class OrderByUserViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _getOrderSuccess = MutableLiveData<Boolean>()
    val getOrderSuccess: LiveData<Boolean> = _getOrderSuccess

    private val _response = MutableLiveData<OrderByUserModelDTO>()
    val response: LiveData<OrderByUserModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }

    fun getOrder(id: String,token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.getOrder(id,token)
            call.enqueue(object : Callback<OrderByUserModelDTO> {
                override fun onResponse(
                    call: Call<OrderByUserModelDTO>,
                    response: Response<OrderByUserModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val orderByUserModelDTO = response.body()
                        _response.postValue(orderByUserModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        ongetOrderSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onSearchFailed()
                    }
                }

                override fun onFailure(call: Call<OrderByUserModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onSearchFailed()
                }
            })
        }
    }

    private fun ongetOrderSuccess() {
        _getOrderSuccess.postValue(true)
    }


    private fun onSearchFailed() {
        _getOrderSuccess.postValue(false)
    }
}