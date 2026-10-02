package com.example.ihc_proyecto1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Query("SELECT * FROM reportes ORDER BY creadoEn DESC")
    fun observarTodos(): Flow<List<ReporteEntity>>

    @Query("SELECT * FROM reportes WHERE id = :id LIMIT 1")
    fun observarPorId(id: String): Flow<ReporteEntity?>

    @Query("SELECT COUNT(*) FROM reportes WHERE estado = 'PENDIENTE'")
    fun observarPendientes(): Flow<Int>

    @Query("SELECT * FROM reportes WHERE estado = 'PENDIENTE' ORDER BY creadoEn ASC")
    suspend fun pendientes(): List<ReporteEntity>

    @Query("SELECT pin FROM reportes")
    suspend fun pinesExistentes(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(reporte: ReporteEntity)

    @Query(
        "UPDATE reportes SET estado = 'ONLINE', pin = :pinOnline, " +
            "pinTemporal = 0, sincronizadoEn = :momento WHERE id = :id"
    )
    suspend fun marcarEnLinea(id: String, pinOnline: String, momento: Long)

    @Query("DELETE FROM reportes WHERE id = :id")
    suspend fun eliminar(id: String)
}