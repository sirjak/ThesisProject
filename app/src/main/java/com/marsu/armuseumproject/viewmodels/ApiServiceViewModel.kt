package com.marsu.armuseumproject.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marsu.armuseumproject.MyApp
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.service.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * ViewModel class for ApiServiceScreen. Contains the data displayed within the screen.
 */
class ApiServiceViewModel(application: Application) : ViewModel() {
    private val service = APIService.service
    private val initialBatchSize = 15
    private val paginationAmount = 10

    private val result = application.applicationContext.getString(R.string.result)
    private val results = application.applicationContext.getString(R.string.results)
    private val noResults = application.applicationContext.getString(R.string.no_result)

    private val _foundIDs = MutableLiveData<MutableList<Int>>()
    private val _artsList = MutableLiveData(listOf<Artwork>())
    val artsList: LiveData<List<Artwork>>
        get() = _artsList

    /**
     * MutableStateFlow variables
     */
    private val _departmentID = MutableStateFlow(0)
    var departmentID = _departmentID.asStateFlow()

    private val _departmentName = MutableStateFlow("")
    var departmentName = _departmentName.asStateFlow()

    private val _initialBatchLoaded = MutableStateFlow(false)
    val initialBatchLoaded = _initialBatchLoaded.asStateFlow()

    private val _loadingResults = MutableStateFlow(false)
    val loadingResults = _loadingResults.asStateFlow()

    private val _resultAmount = MutableStateFlow(0)
    private val resultAmount = _resultAmount.asStateFlow()

    private val _resultText = MutableStateFlow("")
    val resultText = _resultText.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isArtPopupOpen = MutableStateFlow(false)
    var isArtPopupOpen = _isArtPopupOpen.asStateFlow()

    private val _isDepartmentPopupOpen = MutableStateFlow(false)
    var isDepartmentPopupOpen = _isDepartmentPopupOpen.asStateFlow()

    /**
     * Functions to handle MutableStateFlow variable mutations.
     */
    fun onArtItemClick() {
        _isArtPopupOpen.value = true
    }

    fun onDismissPopup() {
        _isArtPopupOpen.value = false
        _isDepartmentPopupOpen.value = false
    }

    fun onFilterButtonClick() {
        _isDepartmentPopupOpen.value = true
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun resetSelectedDepartment() {
        _departmentID.value = 0
        _departmentName.value = ""
    }

    fun updateDepartmentID(id: Int) {
        _departmentID.value = id
    }

    fun updateDepartmentName(name: String) {
        _departmentName.value = name
    }

    /**
     * Get Art ids and store them for later usage.
     */
    private suspend fun getArtIDs(): MutableList<Int> {
        return if (searchText.value.isNotEmpty()) {

            val response = if (_departmentID.value != 0) {
                service.getArtIDs(
                    q = searchText.value, departmentId = _departmentID.value
                )
            } else {
                service.getArtIDs(q = searchText.value)
            }

            if (response.objectIDs.isNullOrEmpty()) {
                Log.d("getArtIDs", "No objectIDs found")
            } else {
                Log.d("getArtIDs", "Found ${response.objectIDs.size} ids")
            }

            response.objectIDs

        } else {
            mutableListOf()
        }
    }

    /**
     * Fetch art data according to the stored IDs.
     */
    fun getArts(refresh: Boolean = false) {
        CoroutineScope(Dispatchers.Main).launch {
            if (_foundIDs.value == null || refresh) {
                _foundIDs.value = getArtIDs()
                _artsList.value = emptyList()
            }
            _loadingResults.value = true

            if (_artsList.value == null || _artsList.value?.isEmpty() == true) {

                _resultAmount.value = 0
                if (_initialBatchLoaded.value) _initialBatchLoaded.value = false

                for (i in 1..initialBatchSize.coerceAtMost(_foundIDs.value?.size?.minus(1) ?: 0)) {
                    addArtIfImagesAreFound()
                }
                _resultAmount.value = _foundIDs.value?.size ?: 0

            } else {
                for (i in 1..paginationAmount) {
                    if ((_artsList.value?.size ?: 0) >= (_foundIDs.value?.size ?: 0)) break
                    addArtIfImagesAreFound()
                }
            }

            Log.d("artsList size", artsList.value?.size.toString())

            _loadingResults.value = false
            _initialBatchLoaded.value = true

            updateResultText()
        }
    }


    /**
     * Searches the API if the searchInput value is valid. searchInput must have at least a length of 2.
     */
    fun searchArtsWithInput() {
        if (searchText.value.isEmpty()) {
            return
        } else if (searchText.value.length < 2) {
            Toast.makeText(
                MyApp.appContext,
                "${MyApp.appContext.getString(R.string.search_too_short)}.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        _artsList.value = mutableListOf()
        getArts(true)
    }

    /**
     * Updates the resultText which displays the amount of found artworks from the API.
     */
    private fun updateResultText() {

        val r = resultAmount.value

        if (r == 1) {
            _resultText.value = "$r $result"
        } else {
            if (r > 1) {
                _resultText.value = "$r $results"
            } else {
                _resultText.value = noResults
            }
        }
    }

    /**
     * Adds the found art to the list if it contains the required primary images. If it lacks images, the ID is removed from the list.
     * @return the success status if the Artwork was added.
     */
    private suspend fun addArtIfImagesAreFound(): Boolean {
        if ((_artsList.value?.size ?: 0) >= (_foundIDs.value?.size ?: 0)) return false

        val objectID = _foundIDs.value?.get(
            (_artsList.value?.size ?: 0).coerceAtMost(_foundIDs.value?.size?.minus(1) ?: 0)
        ) ?: 0

        try {
            val art = service.getObjectByID(objectID)
            if (isValidArt(art)) {
                _artsList.value = _artsList.value.orEmpty() + art
                return true
            } else {
                _foundIDs.value?.remove(objectID)
                Log.d("Empty art removed", "id: $objectID")
            }
        } catch (e: HttpException) {
            Log.d(
                "HttpException @ addArtIfImagesAreFound, removing id $objectID",
                e.message.toString()
            )
            _foundIDs.value?.remove(objectID)
        } catch (e: Exception) {
            Log.d("Exception @ addArtIfImagesAreFound", e.message.toString())
        }
        return false
    }

    /**
     * Checks if the Artwork object is usable in the application.
     * @return true if contains both primaryImage and primaryImageSmall.
     */
    private fun isValidArt(art: Artwork): Boolean {
        return art.primaryImage.isNotEmpty() && art.primaryImageSmall.isNotEmpty()
    }
}