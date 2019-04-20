package Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.itesm.cartelera_tec_mty.Event

@Database(entities = arrayOf(Event::class), version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        private val DATABASE_NAME = "EventDB.db"
        private var dbInstance: EventDatabase? = null

        @Synchronized
        fun getInstance(context: Context): EventDatabase =
                dbInstance ?: buildDatabase(context).also { dbInstance = it}

        private fun buildDatabase(context: Context): EventDatabase = Room.databaseBuilder(context,
                EventDatabase::class.java,
                DATABASE_NAME)
                .build()
    }

}