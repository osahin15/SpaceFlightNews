package com.onursahin.data.db

import androidx.room.TypeConverter
import com.onursahin.data.db.entity.AuthorDto
import com.onursahin.data.db.entity.EventDto
import com.onursahin.data.db.entity.LaunchDto
import com.onursahin.data.db.entity.SocialsDto
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

object Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromAuthors(value: List<AuthorDto>): String =
        json.encodeToString(ListSerializer(AuthorDto.serializer()), value)

    @TypeConverter
    fun toAuthors(value: String): List<AuthorDto> =
        json.decodeFromString(ListSerializer(AuthorDto.serializer()), value)

    @TypeConverter
    fun fromSocials(value: SocialsDto?): String? =
        value?.let { json.encodeToString(SocialsDto.serializer(), it) }

    @TypeConverter
    fun toSocials(value: String?): SocialsDto? =
        value?.let { json.decodeFromString(SocialsDto.serializer(), it) }

    @TypeConverter
    fun fromLaunches(value: List<LaunchDto>): String =
        json.encodeToString(ListSerializer(LaunchDto.serializer()), value)

    @TypeConverter
    fun toLaunches(value: String): List<LaunchDto> =
        json.decodeFromString(ListSerializer(LaunchDto.serializer()), value)

    @TypeConverter
    fun fromEvents(value: List<EventDto>): String =
        json.encodeToString(ListSerializer(EventDto.serializer()), value)

    @TypeConverter
    fun toEvents(value: String): List<EventDto> =
        json.decodeFromString(ListSerializer(EventDto.serializer()), value)

    @TypeConverter
    fun fromFeatured(value: Boolean): Int = if (value) 1 else 0

    @TypeConverter
    fun toFeatured(value: Int): Boolean = value == 1
}