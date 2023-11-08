package com.example.frontend

data class PostDataItemLocation(

        val date: String? = null,
        val description: String? = null,
        val id: Int,
        val images: List<Image>? = null,
        val location: Location? = null,
        val name: String? = null
)