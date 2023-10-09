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
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private var resultCallback: ResultCallback? = null

    // Properti LiveData untuk menandai keberhasilan login
    private val _searchSuccess = MutableLiveData<Boolean>()
    val searchSuccess: LiveData<Boolean> = _searchSuccess

    private val _response = MutableLiveData<SearchModelDTO>()
    val response: LiveData<SearchModelDTO> = _response

    fun setResultCallback(callback: ResultCallback) {
        resultCallback = callback
    }

    fun search(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val call = repository.search(title)
            call.enqueue(object : Callback<SearchModelDTO> {
                override fun onResponse(
                    call: Call<SearchModelDTO>,
                    response: Response<SearchModelDTO>
                ) {
                    if (response.isSuccessful) {
                        // Tangani hasil login yang berhasil di sini
                        val homeModelDTO = response.body()
                        _response.postValue(homeModelDTO!!) // Kirim hasil respons ke LiveData
                        resultCallback?.onDismissLoading()
                        onSearchSuccess()
                    } else {
                        resultCallback?.onDismissLoading()
                        onSearchFailed()
                    }
                }

                override fun onFailure(call: Call<SearchModelDTO>, t: Throwable) {
                    resultCallback?.onDismissLoading()
                    onSearchFailed()
                }
            })
        }
    }

    private fun onSearchSuccess() {
        _searchSuccess.postValue(true)
    }


    private fun onSearchFailed() {
        _searchSuccess.postValue(false)
    }
}