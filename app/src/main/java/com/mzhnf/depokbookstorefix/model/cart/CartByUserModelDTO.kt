package com.mzhnf.depokbookstorefix.model.cart
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CartByUserModelDTO(
    @Expose
    @SerializedName("data")
    val `data`: List<DataCartByUser>,
    @Expose
    @SerializedName("message")
    val message: String
)

data class DataCartByUser(
    @Expose
    @SerializedName("book")
    val book: Book,
    @Expose
    @SerializedName("book_id")
    val bookId: Int,
    @Expose
    @SerializedName("created_at")
    val createdAt: String,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("total_books")
    val totalBooks: String,
    @Expose
    @SerializedName("total_price")
    val totalPrice: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String,
    @Expose
    @SerializedName("user")
    val user: User,
    @Expose
    @SerializedName("user_id")
    val userId: Int,
    @Expose

    var kuantitas: Int? =0
)

data class Book(
    @Expose
    @SerializedName("author")
    val author: String,
    @Expose
    @SerializedName("created_at")
    val createdAt: String,
    @Expose
    @SerializedName("genre")
    val genre: String,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("image")
    val image: String,
    @Expose
    @SerializedName("price")
    val price: String,
    @Expose
    @SerializedName("publication_date")
    val publicationDate: String,
    @Expose
    @SerializedName("rating")
    val rating: Int,
    @Expose
    @SerializedName("status")
    val status: String,
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String
)

data class User(
    @Expose
    @SerializedName("address")
    val address: String,
    @Expose
    @SerializedName("created_at")
    val createdAt: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("profile_pict")
    val profilePict: Any,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String,
    @Expose
    @SerializedName("username")
    val username: String
)