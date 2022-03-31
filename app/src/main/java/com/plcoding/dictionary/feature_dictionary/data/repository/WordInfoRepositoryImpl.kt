package com.plcoding.dictionary.feature_dictionary.data.repository

import com.plcoding.dictionary.core.util.Resource
import com.plcoding.dictionary.feature_dictionary.data.local.WordInfoDao
import com.plcoding.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.plcoding.dictionary.feature_dictionary.domain.model.WordInfo
import com.plcoding.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api: DictionaryApi,
    private val dao: WordInfoDao,
) : WordInfoRepository {
    override fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())

        val cachedWords = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(cachedWords))

        try {
            val apiWords = api.getWordInfo(word)
            dao.deleteWordInfos(apiWords.map { it.word })
            dao.insertWordInfos(apiWords.map { it.toWordEntity() })
        } catch (e: HttpException) {
            emit(Resource.Error("something went wrong", cachedWords))
        } catch (e: IOException) {
            emit(Resource.Error("no internet connection", cachedWords))
        }
        val newWordInfoList = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfoList))
    }
}
