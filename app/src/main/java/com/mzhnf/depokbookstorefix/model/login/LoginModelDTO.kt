package com.mzhnf.depokbookstorefix.model.login
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class LoginModelDTO(
    @Expose
    @SerializedName("data")
    val data: Data,
    @Expose
    @SerializedName("message")
    val message: String,
    @Expose
    @SerializedName("token")
    val token: String
)

data class Data(
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
    val profilePict: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String,
    @Expose
    @SerializedName("username")
    val username: String
)