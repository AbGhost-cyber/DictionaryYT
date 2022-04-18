package com.plcoding.dictionary.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.plcoding.dictionary.feature_dictionary.data.local.Converters
import com.plcoding.dictionary.feature_dictionary.data.local.WordInfoDatabase
import com.plcoding.dictionary.feature_dictionary.data.remote.DictionaryApi
import com.plcoding.dictionary.feature_dictionary.data.repository.WordInfoRepositoryImpl
import com.plcoding.dictionary.feature_dictionary.data.util.GsonParser
import com.plcoding.dictionary.feature_dictionary.domain.repository.WordInfoRepository
import com.plcoding.dictionary.feature_dictionary.domain.usecases.GetWordInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(repository: WordInfoRepository) = GetWordInfo(repository)

    @Provides
    @Singleton
    fun provideGetWordInfoRepo(db: WordInfoDatabase, api: DictionaryApi): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.wordInfoDao)
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryApi = Retrofit.Builder()
        .baseUrl(DictionaryApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(DictionaryApi::class.java)

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app: Application) = Room.databaseBuilder(
        app, WordInfoDatabase::class.java, "word_db"
    ).addTypeConverter(Converters(GsonParser(Gson())))
        .fallbackToDestructiveMigration().build()
}
