package com.udacity.asteroidradar.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

// Room Entity Asteroid
@Entity
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun List<AsteroidEntity>.asAsteroidEntity():List<Asteroid>
{
    return map {
        Asteroid(
            id=it.id,
            codename =it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude =it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous )
    }
}

// DOA Asteroid
@Dao
interface AsteroidDAO {
    @Query("SELECT * FROM asteroidentity obj WHERE obj.closeApproachDate = :today")
    fun getTodayAsteroid(today :String):LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroidentity obj WHERE obj.closeApproachDate BETWEEN :startDate  AND :endDate  order by closeApproachDate desc")
    fun getWeekAsteroid(startDate:String, endDate:String):LiveData<List<AsteroidEntity>>

    @Query("select * from asteroidentity order by closeApproachDate desc")
    fun getAsteroid():LiveData<List<AsteroidEntity>>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertAllAsteroid(vararg database: AsteroidEntity)

    @Query("DELETE FROM asteroidentity WHERE closeApproachDate<:today")
    fun delete(today:String)
}

//////////////////////////////  IMAGE  //////////////////////////////

// Room Entity Image
@Entity
data class ImageEntity (@PrimaryKey
                        val media_type: String,
                        val title: String,
                        val url: String)

fun ImageEntity.asImageEntity(): PictureOfDay {
    return PictureOfDay(url=url, media_type=media_type, title=title)
}

// DOA Image
@Dao
interface ImageDAO{
    @Query("select * from ImageEntity")
    fun getImage():LiveData<ImageEntity>

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    fun insertImage(ImageEntity: ImageEntity)
}

//////////////////////////////  Database entities  //////////////////////////////

@Database (entities = [AsteroidEntity::class, ImageEntity::class], version = 1, exportSchema = false)
abstract class AsteroidData : RoomDatabase() {
    abstract val asteroidDAO:AsteroidDAO
    abstract val imageDao:ImageDAO
    companion object{
        private lateinit var INSTANCE: AsteroidData
        fun getDatabase(context: Context): AsteroidData {
            synchronized(AsteroidData::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidData::class.java, "asteroids")
                        .build()
                }
            }
            return INSTANCE
        }
    }

}


