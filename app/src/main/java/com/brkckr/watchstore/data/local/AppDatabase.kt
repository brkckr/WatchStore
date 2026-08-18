package com.brkckr.watchstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// room database for cart persistence
@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        const val NAME = "watch_store_db"
    }
}
