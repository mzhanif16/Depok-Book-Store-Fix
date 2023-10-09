package com.mzhnf.depokbookstorefix.network



import com.mzhnf.depokbookstorefix.model.cart.CartByUserModelDTO
import com.mzhnf.depokbookstorefix.model.cart.CartModelDTO
import com.mzhnf.depokbookstorefix.model.home.HomeModelDTO
import com.mzhnf.depokbookstorefix.model.home.SearchModelDTO
import com.mzhnf.depokbookstorefix.model.login.LoginModelDTO
import com.mzhnf.depokbookstorefix.model.order.OrderByUserModelDTO
import com.mzhnf.depokbookstorefix.model.register.RegisterModelDTO
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginModelDTO>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") username: String,
        @Field("address") address: String
    ): Call<RegisterModelDTO>

    @FormUrlEncoded
    @POST("addToCart")
    fun addCart(
        @Field("user_id") email: String,
        @Field("book_id") bookId: String,
        @Field("total_price") totalPrice: String,
        @Field("total_books") totalBooks: String,
        @Header("Authorization") token: String,
    ): Call<CartModelDTO>

    @GET("book")
    fun home(): Call<HomeModelDTO>

    @GET("book/search/title/{title}")
    fun search(@Path("title")title: String): Call<SearchModelDTO>

    @GET("cart/user/{id}")
    fun getCart(@Path("id")id: String, @Header("Authorization") token: String): Call<CartByUserModelDTO>

    @GET("transaksi/user/{id}")
    fun getOrder(@Path("id")id: String, @Header("Authorization") token: String): Call<OrderByUserModelDTO>
}