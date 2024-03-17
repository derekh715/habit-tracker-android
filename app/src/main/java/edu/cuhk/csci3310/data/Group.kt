package edu.cuhk.csci3310.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "group",
)
data class Group(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long? = null,
    // hex colour in ARGB form
    val colour: Int,
    val name: String,
    val description: String?,
)
