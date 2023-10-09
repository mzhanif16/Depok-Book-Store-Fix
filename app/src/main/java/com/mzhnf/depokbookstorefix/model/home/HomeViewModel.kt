package com.mzhnf.depokbookstorefix.model.home

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
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _homeSuccess = MutableLiveData<Boolean>()
    val homeSuccess: LiveData<Boolean> = _homeSuccess

    private val _response = MutableLiveData<HomeModelDTO>()
    val response: LiveData<HomeModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }

    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.home()
            call.enqueue(object : Callback<HomeModelDTO> {
                override fun onResponse(
                    call: Call<HomeModelDTO>,
                    response: Response<HomeModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val homeModelDTO = response.body()
                        _response.postValue(homeModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        onHomeSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onHomeFailed()
                    }
                }

                override fun onFailure(call: Call<HomeModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onHomeFailed()
                }
            })
        }
    }

    private fun onHomeSuccess() {
        _homeSuccess.postValue(true)
    }


    private fun onHomeFailed() {
        _homeSuccess.postValue(false)
    }
}