package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BarrierReportDao {
    @Query("SELECT * FROM barrier_reports ORDER BY dateReported DESC")
    fun getAllReports(): Flow<List<BarrierReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: BarrierReportEntity)

    @Update
    suspend fun updateReport(report: BarrierReportEntity)

    @Delete
    suspend fun deleteReport(report: BarrierReportEntity)

    @Query("SELECT COUNT(*) FROM barrier_reports")
    suspend fun getCount(): Int
}
