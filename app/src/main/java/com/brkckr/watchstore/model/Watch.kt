package com.brkckr.watchstore.model

// model representing a watch timepiece
data class Watch(
    val id: String,
    val eyebrow: String,
    val name: String,
    val priceCents: Int,
    val description: String,
    val sourceUrl: String,
    val artworkUrl: String,
    val alternateArtworkUrl: String,
    val movement: String,
    val caseSize: String,
    val waterResistance: String,
    val images: List<String>,
)
