package com.onursahin.data.response

import com.google.gson.annotations.SerializedName

data class PagedResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)

data class NewsResults(
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<Author>?,
    @SerializedName("url") val url: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("news_site") val newsSite: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("featured") val featured: Boolean?,
    @SerializedName("launches") val launches: List<Launch>?,
    @SerializedName("events") val events: List<Event>?
)

data class Author(
    @SerializedName("name") val name: String?,
    @SerializedName("socials") val socials: Socials?
)

data class Socials(
    @SerializedName("x") val x: String?,
    @SerializedName("youtube") val youtube: String?,
    @SerializedName("instagram") val instagram: String?,
    @SerializedName("linkedin") val linkedin: String?,
    @SerializedName("mastodon") val mastodon: String?,
    @SerializedName("bluesky") val bluesky: String?
)

data class Launch(
    @SerializedName("launch_id") val launchId: String?,
    @SerializedName("provider") val provider: String?
)

data class Event(
    @SerializedName("event_id") val eventId: Int?,
    @SerializedName("provider") val provider: String?
)
