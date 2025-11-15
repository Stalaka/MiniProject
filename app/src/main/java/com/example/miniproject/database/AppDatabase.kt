package com.example.miniproject.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miniproject.models.PurchaseOrder

@Database(entities = [PurchaseOrder::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Connects the Database to the DAO
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton pattern to get the database
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "procurement_database" // Name of the actual file on the phone
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}