package com.example.squeezemybrain.ui_.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object Utility {

    fun convertListToString(list : List<String>) : String{
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        val json: String = gson.toJson(list, type)
        return json
    }

    fun convertStringToList(str : String) : List<String>{
        val gson = Gson()
        val type: Type = object : TypeToken<List<String?>?>() {}.type
        val json: List<String> = gson.fromJson(str, type)
        return json
    }
}