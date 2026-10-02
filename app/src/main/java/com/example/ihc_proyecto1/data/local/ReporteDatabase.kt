package com.example.ihc_proyecto1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReporteEntity::class], version = 2, exportSchema = false)
abstract class ReporteDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDao

    companion object {
        @Volatile
        private var instancia: ReporteDatabase? = null

        fun obtener(context: Context): ReporteDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReporteDatabase::class.java,
                    "objetos_perdidos.db"
                )
                    // La app todavía está en desarrollo: al cambiar el esquema
                    // se reinicia la base en vez de migrar.
                    .fallbackToDestructiveMigration()
                    .build().also { instancia = it }
            }
    }
}