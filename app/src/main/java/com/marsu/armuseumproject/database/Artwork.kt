package com.marsu.armuseumproject.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Structure for our single table within our Room Database
 */
@Parcelize
@Entity(tableName = "art_table")
data class Artwork(
    @PrimaryKey(autoGenerate = false) val objectID: Int,
    var primaryImage: String,
    var primaryImageSmall: String,
    val department: String,
    val title: String,
    val artistDisplayName: String,
    val classification: String
) : Parcelable
