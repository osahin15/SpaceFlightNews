package com.onursahin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "news_article")
data class NewsEntity(
    @PrimaryKey val id: Int,
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
    val events: List<EventDto>
)

@Serializable
data class AuthorDto(val name: String, val socials: SocialsDto?)

@Serializable
data class SocialsDto(
    val x: String,
    val youtube: String,
    val instagram: String,
    val linkedin: String,
    val mastodon: String,
    val bluesky: String
)

@Serializable
data class LaunchDto(val launch_id: String, val provider: String)

@Serializable
data class EventDto(val event_id: Int, val provider: String)


