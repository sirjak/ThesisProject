package com.marsu.armuseumproject.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.marsu.armuseumproject.database.ArtDB
import com.marsu.armuseumproject.database.Artwork
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for the AR Selection Fragment, retrieves the artwork from the Room database to display
 * in the RecyclerView
 */
open class ArSelectionViewModel(
    application: Application,
    private val state: SavedStateHandle = SavedStateHandle()
) :
    AndroidViewModel(application) {
    private val database = ArtDB.get(application.applicationContext)

    // Utilities to preselection handling
    val preselectedId: StateFlow<Int?> = state.getStateFlow(key = "BOOP", initialValue = null)
    fun saveId(id: Int?) {
        state.set(key = "BOOP", value = id)
    }

    val imageUri = MutableLiveData<Uri?>(null)
    val imageId = MutableLiveData<Int?>(null)
    var getAllArtwork: LiveData<List<Artwork>> = database.artDao().getAllArt()

    fun getArt(id: Int): LiveData<List<Artwork>> = database.artDao().getSpecificArt(id)
}