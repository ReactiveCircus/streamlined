package io.github.reactivecircus.streamlined.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SourceDTO(
    val id: String,
    val name: String
)
