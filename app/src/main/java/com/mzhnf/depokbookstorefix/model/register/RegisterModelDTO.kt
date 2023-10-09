package com.mzhnf.depokbookstorefix.model.register
import com.google.gson.annotations.SerializedName


data class RegisterModelDTO(
    @SerializedName("data")
    val `data`: DataRegister,
    @SerializedName("message")
    val message: String
)

data class DataRegister(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String
)