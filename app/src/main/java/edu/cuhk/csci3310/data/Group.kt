package edu.cuhk.csci3310.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long? = null,
    // hex colour
    val colour: String,
    val name: String,
    val description: String?,
)
