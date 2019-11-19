package com.example.puppr

import android.util.Log
import androidx.lifecycle.ViewModel

class ViewDogModel : ViewModel() {

    var dogID: Int = 0
    var dogName: String = "Cornbread"
    var shelterName: String = "Happy Dog Shelter"
    var dogImage: Int = R.mipmap.client_base_dog_foreground

    fun getNewDog() {

        Log.i("YERT", "Getting New Dog Info")
    }
}