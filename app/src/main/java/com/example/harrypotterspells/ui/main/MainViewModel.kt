package com.example.harrypotterspells.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.harrypotterspells.model.DataModel
import com.example.harrypotterspells.model.response.SpellResponse
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class MainViewModel(private  val model: DataModel) : ViewModel() {

    private val _getSpellResponseLiveData = MutableLiveData<List<SpellResponse>>()

    val getSpellResponseLiveData: LiveData<List<SpellResponse>>
        get() = _getSpellResponseLiveData

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> get() = _isError

    fun getSpells() {
        try{
            model.getData()
                .subscribeOn(Schedulers.io()) // [첫번째 스트림 ~ observeOn 호출 전 까지의 스트림]의 쓰레드를 지정
                .subscribe({
                    it.run {
                        Log.d( "MainViewModel","spells : $it")
                        _getSpellResponseLiveData.postValue(it)
                    }
                }, {
                    _isError.postValue(it.message)
                    Log.d("MainViewModel", "response error, message : ${it.message}")
                })
        }catch (e:Exception){
            _isError.postValue(e.message)
        }

    }
}