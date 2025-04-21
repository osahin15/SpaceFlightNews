package com.onursahin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val articleId: Int,
    val prevOffset: Int?,
    val nextOffset: Int?
)