package com.onursahin.domain.model

data class News(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val url: String,
    val imageUrl: String,
    val newsSite: String,
    val summary: String,
    val publishedAt: String,
    val updatedAt: String,
    val featured: Boolean,
    val launches: List<Launch>,
    val events: List<Event>
)

data class Author(
    val name: String,
    val socials: Socials?
)

data class Socials(
    val x: String,
    val youtube: String,
    val instagram: String,
    val linkedin: String,
    val mastodon: String,
    val bluesky: String
)

data class Launch(
    val launchId: String,
    val provider: String
)

data class Event(
    val eventId: Int,
    val provider: String
)

