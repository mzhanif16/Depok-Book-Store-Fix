package com.mzhnf.depokbookstorefix.model.order
import com.google.gson.annotations.SerializedName


data class OrderByUserModelDTO(
    @SerializedName("data")
    val `data`: List<DataOrderByUser>,
    @SerializedName("message")
    val message: String
)

data class DataOrderByUser(
    @SerializedName("cart")
    val cart: List<Cart>,
    @SerializedName("cart_id")
    val cartId: Any,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("delivery_status")
    val deliveryStatus: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_date")
    val orderDate: String,
    @SerializedName("payment_status")
    val paymentStatus: Int,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("total_price")
    val totalPrice: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: User,
    @SerializedName("user_id")
    val userId: Int
)

data class Cart(
    @SerializedName("book_id")
    val bookId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("pivot")
    val pivot: Pivot,
    @SerializedName("total_books")
    val totalBooks: String,
    @SerializedName("total_price")
    val totalPrice: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)

data class User(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("profile_pict")
    val profilePict: Any,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)

data class Pivot(
    @SerializedName("cart_id")
    val cartId: Int,
    @SerializedName("kuantitas")
    val kuantitas: String,
    @SerializedName("transaksi_id")
    val transaksiId: Int
)