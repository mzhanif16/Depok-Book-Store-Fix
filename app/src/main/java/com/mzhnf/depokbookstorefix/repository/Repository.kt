package com.mzhnf.depokbookstorefix.repository

import com.mzhnf.depokbookstorefix.model.cart.CartByUserModelDTO
import com.mzhnf.depokbookstorefix.model.cart.CartModelDTO
import com.mzhnf.depokbookstorefix.model.home.HomeModelDTO
import com.mzhnf.depokbookstorefix.model.home.SearchModelDTO
import com.mzhnf.depokbookstorefix.model.login.LoginModelDTO
import com.mzhnf.depokbookstorefix.model.order.OrderByUserModelDTO
import com.mzhnf.depokbookstorefix.model.register.RegisterModelDTO
import com.mzhnf.depokbookstorefix.network.ApiService
import retrofit2.Call
import javax.inject.Inject


class Repository @Inject constructor(private val apiService: ApiService) {

    fun login(email: String, password: String): Call<LoginModelDTO> {
        return apiService.login(email, password)
    }
    fun home(): Call<HomeModelDTO> {
        return apiService.home()
    }
    fun addToCart(userId: String, bookId: String, totalPrice: String, totalBook: String, token: String): Call<CartModelDTO> {
        return apiService.addCart(userId, bookId, totalPrice, totalBook, token)
    }
    fun register(email: String, password: String, username: String, address: String): Call<RegisterModelDTO> {
        return apiService.register(email, password, username, address)
    }
    fun search(title: String): Call<SearchModelDTO> {
        return apiService.search(title)
    }
    fun getCart(id: String, token: String): Call<CartByUserModelDTO> {
        return apiService.getCart(id, token)
    }
    fun getOrder(id: String, token: String): Call<OrderByUserModelDTO> {
        return apiService.getOrder(id, token)
    }
}