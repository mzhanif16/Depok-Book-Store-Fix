package com.mzhnf.depokbookstorefix.model.cart
import com.google.gson.annotations.SerializedName


data class CartModelDTO(
    @SerializedName("data")
    val `data`: DataCart,
    @SerializedName("message")
    val message: String
)

data class DataCart(
    @SerializedName("book_id")
    val bookId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("total_books")
    val totalBooks: String,
    @SerializedName("total_price")
    val totalPrice: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)