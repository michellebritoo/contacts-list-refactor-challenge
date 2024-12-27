package com.list.desafio.android.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("img") val image: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("username") val username: String? = ""
) : Parcelable