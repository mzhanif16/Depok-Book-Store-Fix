package com.mzhnf.depokbookstorefix.model.home
import com.google.gson.annotations.SerializedName


data class HomeModelDTO(
    @SerializedName("data")
    val `data`: List<DataHome>,
    @SerializedName("message")
    val message: String
)

data class DataHome(
    @SerializedName("author")
    val author: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("publication_date")
    val publicationDate: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("status")
    val status: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String
)