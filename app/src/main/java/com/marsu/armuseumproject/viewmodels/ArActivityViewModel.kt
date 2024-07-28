package com.marsu.armuseumproject.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
//import com.google.ar.sceneform.Node

/**
 * ViewModel for the AR Activity, keeps track of the current image node used by SceneForm and calculates
 * whether the phone is upright with the gravity sensor data
 */
class ArActivityViewModel(application: Application) : ArSelectionViewModel(application) {
    //val currentImageNode = MutableLiveData<Node>(null)
    val isPhoneUpright = MutableLiveData(false)

    // Calculate if phone is upright
    fun calculateGravityData(y: Float) {
        isPhoneUpright.value = y > 9.4 && y < 10
    }
}