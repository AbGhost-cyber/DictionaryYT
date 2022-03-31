package com.plcoding.dictionary.feature_dictionary.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.plcoding.dictionary.feature_dictionary.data.util.JsonParser
import com.plcoding.dictionary.feature_dictionary.domain.model.Meaning
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning> {
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json = json,
            type = object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meaningsList: List<Meaning>): String {
        return jsonParser.toJson(
            obj = meaningsList,
            type = object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: "[]"
    }
}

//fun main() = runBlocking {
//    println("first thread starts ${Thread.currentThread().name}")
//    val jobDeferred = async {
//        delay(1000)
//        "String"
//    }
//    val value = jobDeferred.await()
//    println("value is $value")
//    val s = listOf<String>()
//    val (a, b, c) = s
//    println("$a $b $c")
//}
