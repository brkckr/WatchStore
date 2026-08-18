package com.brkckr.watchstore.di

import android.content.Context
import androidx.room.Room
import com.brkckr.watchstore.data.WatchRepository
import com.brkckr.watchstore.data.WatchRepositoryImpl
import com.brkckr.watchstore.data.local.AppDatabase
import com.brkckr.watchstore.data.local.CartDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// dependency injection module for app-wide components
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindWatchRepository(
        watchRepositoryImpl: WatchRepositoryImpl
    ): WatchRepository

    companion object {
        // provides the room database instance
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.NAME
            ).build()
        }

        // provides the cart dao from the database
        @Provides
        fun provideCartDao(database: AppDatabase): CartDao {
            return database.cartDao()
        }
    }
}
