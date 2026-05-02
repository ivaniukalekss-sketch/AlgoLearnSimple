package com.ivaniuk.algolearnsimple.di

import android.content.Context
import com.ivaniuk.algolearnsimple.data.local.HistoryDao
import com.ivaniuk.algolearnsimple.data.local.HistoryDatabase
import com.ivaniuk.algolearnsimple.data.local.LocalStorage
import com.ivaniuk.algolearnsimple.data.repository.AlgorithmRepositoryImpl
import com.ivaniuk.algolearnsimple.domain.repository.AlgorithmRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext context: Context): LocalStorage {
        return LocalStorage(context)
    }

    @Provides
    @Singleton
    fun provideAlgorithmRepository(
        localStorage: LocalStorage
    ): AlgorithmRepository {
        return AlgorithmRepositoryImpl(localStorage)
    }

    @Provides
    @Singleton
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return HistoryDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: HistoryDatabase): HistoryDao {
        return database.historyDao()
    }
}