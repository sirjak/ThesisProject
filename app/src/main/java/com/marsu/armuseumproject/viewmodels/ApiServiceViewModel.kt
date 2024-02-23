package com.marsu.armuseumproject.viewmodels

import android.content.Context
import android.content.SharedPreferences
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
 * ViewModel class for ApiServiceFragment. Contains the data displayed within the Fragment.
 */
class ApiServiceViewModel(val context: Context) : ViewModel() {

    private val initialBatchSize = 15
    private val service = APIService.service

    private val _departmentText = MutableLiveData("")
    val departmentText: LiveData<String>
        get() = _departmentText

    private val _resultText = MutableLiveData("")
    val resultText: LiveData<String>
        get() = _resultText

    private val _departmentId = MutableLiveData(0)
    val departmentId: LiveData<Int>
        get() = _departmentId

    private val _foundIDs = MutableLiveData<MutableList<Int>>()

    private val _artsList = MutableLiveData(listOf<Artwork>())
    val artsList: LiveData<List<Artwork>>
        get() = _artsList

    private val _loadingResults = MutableLiveData(false)
    val loadingResults: LiveData<Boolean>
        get() = _loadingResults

    private val _initialBatchLoaded = MutableLiveData(false)
    val initialBatchLoaded: LiveData<Boolean>
        get() = _initialBatchLoaded

    private val _resultAmount = MutableLiveData(0)
    private val resultAmount: LiveData<Int>
        get() = _resultAmount

    val paginationAmount = 10

    /**
     * Testing stuff here
     */
    private var _isArtPopupOpen = MutableStateFlow(false)
    var isArtPopupOpen = _isArtPopupOpen.asStateFlow()

    private var _isDepartmentPopupOpen = MutableStateFlow(false)
    var isDepartmentPopupOpen = _isDepartmentPopupOpen.asStateFlow()

    private var _testing = MutableStateFlow("")
    var testing = _testing.asStateFlow()

    private var _dood = MutableStateFlow(0)
    var dood = _dood.asStateFlow()

    private var _body = MutableStateFlow("")
    var body = _body.asStateFlow()

    fun testOne() {

    }
    fun onArtItemClick() {
        _isArtPopupOpen.value = true
    }

    fun onFilterButtonClick() {
        _isDepartmentPopupOpen.value = true
    }

    fun onDismissPopup() {
        _isArtPopupOpen.value = false
        _isDepartmentPopupOpen.value = false
    }

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    /**
     * Get Art ids and store them for later usage.
     */
    private suspend fun getArtIDs(): MutableList<Int> {
        return if (searchText.value.isNotEmpty()) {

            val response = if (_dood.value != 0) {
                Log.d("_HAISTA", _dood.value.toString())
                Log.d("HAISTA", dood.value.toString())
                service.getArtIDs(
                    q = searchText.value.toString(), departmentId = _dood.value ?: 0
                )
            } else {
                service.getArtIDs(q = searchText.value.toString())
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

        /*return if (searchText.value.isNotEmpty()) {

            val response = if (departmentId.value != 0) {
                service.getArtIDs(
                    q = searchText.value.toString(), departmentId = departmentId.value ?: 0
                )
            } else {
                service.getArtIDs(q = searchText.value.toString())
            }

            if (response.objectIDs.isNullOrEmpty()) {
                Log.d("getArtIDs", "No objectIDs found")
            } else {
                Log.d("getArtIDs", "Found ${response.objectIDs.size} ids")
            }

            response.objectIDs

        } else {
            mutableListOf()
        }*/
    }


    /**
     * Updates the departmentId and departmentText LiveData objects.
     */
    fun updateDepartmentID(id: Int) {
        /*val pref: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val newDep = pref.getInt("selectedDepartment", 0)
        if (newDep != departmentId.value) {
            _departmentId.value = newDep
            getArts(true)
        } else {
            _departmentId.value = newDep
        }
        updateDepartmentName()*/
        _dood.value = id
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
                if (_initialBatchLoaded.value != false) _initialBatchLoaded.value = false


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
            Log.i("SEARCH", "Empty searchText")
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
        Log.d("searchText", searchText.value)
    }


    /**
     * Resets the selected department data from SharedPreferences. Furthermore, updates the LiveData objects regarding the department info.
     */
    fun resetSelectedDepartment() {
        /*val pref: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("selectedDepartment", 0)
        editor.putInt("selectedDepartmentRadioButton", 0)
        editor.putString("selectedDepartmentName", "")
        editor.apply()
        //updateDepartmentID()
        getArts(true)*/
        _dood.value = 0
        _body.value = ""
    }


    /**
     * Updates the resultText which displays the amount of found artworks from the API.
     */
    fun updateResultText() {

        val r = resultAmount.value

        if (r == 1) {
            _resultText.value = "$r ${context.getString(R.string.result)}"
        } else if (r != null) {
            if (r > 1) {
                _resultText.value = "$r ${context.getString(R.string.results)}"
            } else {
                _resultText.value = context.getString(R.string.no_result)
            }
        }
    }


    /**
     * Updates the departmentText LiveData object.
     */
    fun updateDepartmentName(name: String) {
        /*val pref: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        _departmentText.value = pref.getString("selectedDepartmentName", "")*/
        _body.value = name
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