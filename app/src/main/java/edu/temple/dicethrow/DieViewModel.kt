package edu.temple.dicethrow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class DieViewModel: ViewModel (){
    // Starts empty so the fragment knows it hasn't rolled yet
    private val currentRoll = MutableLiveData<Int>()

    fun setRoll(roll: Int){
        currentRoll.value=roll
    }
    fun getRoll(): LiveData<Int> {
        return currentRoll
    }
}
