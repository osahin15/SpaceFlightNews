package com.onursahin.data.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.onursahin.data.db.entity.AuthorDto
import com.onursahin.data.db.entity.EventDto
import com.onursahin.data.db.entity.LaunchDto
import com.onursahin.data.db.entity.NewsEntity
import com.onursahin.data.db.entity.SocialsDto
import com.onursahin.data.response.AuthorResponse
import com.onursahin.data.response.EventResponse
import com.onursahin.data.response.LaunchResponse
import com.onursahin.data.response.NewsResultsResponse
import com.onursahin.data.response.SocialsResponse
import com.onursahin.domain.util.orFalse
import com.onursahin.domain.util.orZero
import com.onursahin.domain.model.Author
import com.onursahin.domain.model.Event
import com.onursahin.domain.model.Launch
import com.onursahin.domain.model.News
import com.onursahin.domain.model.Socials
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime


fun NewsResultsResponse.toUiModel() = News(
    id = id.orZero(),
    title = title.orEmpty(),
    authors = authors?.map { it.toUiModel() }.orEmpty(),
    url = url.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite.orEmpty(),
    summary = summary.orEmpty(),
    publishedAt = publishedAt.toDateString(),
    updatedAt = updatedAt.toDateString(),
    featured = featured.orFalse(),
    launches = launches?.map { it.toUiModel() }.orEmpty(),
    events = events?.map { it.toUiModel() }.orEmpty()
)

fun NewsResultsResponse.toEntity() = NewsEntity(
    id = id.orZero(),
    title = title.orEmpty(),
    authors = authors?.map { it.toEntity() }.orEmpty(),
    url = url.orEmpty(),
    imageUrl = imageUrl.orEmpty(),
    newsSite = newsSite.orEmpty(),
    summary = summary.orEmpty(),
    publishedAt = publishedAt.toDateString(),
    updatedAt = updatedAt.toDateString(),
    featured = featured.orFalse(),
    launches = launches?.map { it.toEntity() }.orEmpty(),
    events = events?.map { it.toEntity() }.orEmpty()
)

fun NewsEntity.toUiModel() = News(
    id = id.orZero(),
    title = title,
    authors = authors?.map { it.toUiModel() }.orEmpty(),
    url = url,
    imageUrl = imageUrl,
    newsSite = newsSite,
    summary = summary,
    publishedAt = publishedAt,
    updatedAt = updatedAt,
    featured = featured.orFalse(),
    launches = launches.map { it.toUiModel() },
    events = events.map { it.toUiModel() }
)

fun PagingData<NewsEntity>.toPagingDataUiModel(): PagingData<News> {
    return this.map { it.toUiModel() }
}

fun AuthorResponse.toUiModel() = Author(
    name = name.orEmpty(),
    socials = socials?.toUiModel()
)

fun AuthorResponse.toEntity() = AuthorDto(
    name = name.orEmpty(),
    socials = socials?.toEntity()
)

fun AuthorDto.toUiModel() = Author(
    name = name,
    socials = socials?.toUiModel()
)

fun SocialsResponse.toUiModel() = Socials(
    youtube = youtube.orEmpty(),
    instagram = instagram.orEmpty(),
    x = x.orEmpty(),
    linkedin = linkedin.orEmpty(),
    mastodon = mastodon.orEmpty(),
    bluesky = bluesky.orEmpty()
)

fun SocialsResponse.toEntity() = SocialsDto(
    youtube = youtube.orEmpty(),
    instagram = instagram.orEmpty(),
    x = x.orEmpty(),
    linkedin = linkedin.orEmpty(),
    mastodon = mastodon.orEmpty(),
    bluesky = bluesky.orEmpty()
)

fun SocialsDto.toUiModel() = Socials(
    youtube = youtube,
    instagram = instagram,
    x = x,
    linkedin = linkedin,
    mastodon = mastodon,
    bluesky = bluesky
)

fun LaunchResponse.toUiModel() = Launch(
    launchId = launchId.orEmpty(),
    provider = provider.orEmpty()
)

fun LaunchResponse.toEntity() = LaunchDto(
    launch_id = launchId.orEmpty(),
    provider = provider.orEmpty()
)

fun LaunchDto.toUiModel() = Launch(
    launchId = launch_id,
    provider = provider
)

fun EventResponse.toUiModel() = Event(
    eventId = eventId.orZero(),
    provider = provider.orEmpty()
)

fun EventResponse.toEntity() = EventDto(
    event_id = eventId.orZero(),
    provider = provider.orEmpty()
)

fun EventDto.toUiModel() = Event(
    eventId = event_id.orZero(),
    provider = provider
)

fun String?.toDateString(): String {
    return Instant.parse(this.toString())?.let {
        it.toLocalDateTime(TimeZone.currentSystemDefault()).format(
            LocalDateTime.Format {
                year()
                char('-')
                monthNumber()
                char('-')
                dayOfMonth()
                char(' ')
                hour()
                char(':')
                minute()
            })
    }.orEmpty()
}