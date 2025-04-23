package com.onursahin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.onursahin.domain.model.News

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    @PrimaryKey val newsId: Int,
    val title: String,
    val summary: String,
    val url: String,
    val imageUrl: String,
    val newsSite: String,
    val publishedAt: String,
    val updatedAt: String,
    val featured: Boolean,
    val authors: List<AuthorDto>,
    val launches: List<LaunchDto>,
    val events: List<EventDto>,
    val addedAt: Long = System.currentTimeMillis()
)