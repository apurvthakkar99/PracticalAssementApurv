package com.example.scotiapracticaltest.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val name: String,
    @SerializedName("avatar_url") val avatarUrl: String?
)