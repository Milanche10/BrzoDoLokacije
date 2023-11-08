package com.example.frontend

data class PostDataItem(
    val date: String,
    val description: String,
    val id: Int,
    val images: List<Image>,
    val location: Location,
    val name: String
)