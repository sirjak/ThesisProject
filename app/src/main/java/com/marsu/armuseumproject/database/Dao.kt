package com.marsu.armuseumproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Calls for inserting and retrieving Artworks to/from Room Database
 */
@Dao
interface ArtworkDAO {
    // Adds artwork to DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArtwork(art: Artwork)

    @Query("SELECT * FROM art_table")
    fun getAllArt(): LiveData<List<Artwork>>

    @Query("SELECT * FROM art_table WHERE objectID = :objectID")
    fun getSpecificArt(objectID: Int): LiveData<List<Artwork>>

    @Query("SELECT * FROM art_table WHERE department =:department")
    fun getArtByDepartment(department: String): LiveData<List<Artwork>>
}