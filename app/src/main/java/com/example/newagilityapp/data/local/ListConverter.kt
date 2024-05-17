package com.example.newagilityapp.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
@ProvidedTypeConverter
class ListConverter {
    @TypeConverter
    fun fromColorList(colorList: List<Int>):String {
        return colorList.joinToString(",") { it.toString() }
    }
    @TypeConverter
    fun toColorList(colorListString: String):List<Int>{
        return colorListString.split(",").map { it.toInt() }
    }

}