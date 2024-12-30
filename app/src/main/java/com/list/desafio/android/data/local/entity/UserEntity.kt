package com.list.desafio.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (
    @PrimaryKey var id: Int,
    @ColumnInfo("image") var image: String?,
    @ColumnInfo("name")var name: String?,
    @ColumnInfo("username")var username: String?
)
