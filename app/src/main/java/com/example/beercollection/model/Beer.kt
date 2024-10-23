package com.example.beercollection.model

data class Beer(
    val id: Int,
    val user: String,
    val brewery: String,
    val name: String,
    val style: String,
    val abv: Double,
    val volume: Double,
    val pictureUrl: String?,
    val howMany: Int
) {
    constructor(
        user: String,
        brewery: String,
        name: String,
        style: String,
        abv: Double,
        volume: Double,
        pictureUrl: String,
        howMany: Int
    ) : this(-1, user, brewery, name, style, abv, volume, pictureUrl, howMany)

    override fun toString(): String {
        return "$id Owner: $user, About Beer: $brewery, $name, $style, $abv, $volume, $howMany PictureUrl: $pictureUrl"
    }
}
