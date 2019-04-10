package Database

import android.arch.persistence.room.*
import com.itesm.cartelera_tec_mty.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM Event")
    fun loadAllEvents(): MutableList<Event>
/*
    @Query("SELECT * FROM Event WHERE _id = :arg0")
    fun loadEvent(id:Int):Event
*/
    @Query("SELECT COUNT(*) from Event WHERE _id = :arg0")
    fun eventsCount(id:Int):Int

    @Insert
    fun insertAllEvents(eventList: List<Event>)

    @Insert
    fun insertEvent(event:Event)

    @Query("DELETE FROM Event")
    fun deleteAllEvents()

    @Query("DELETE FROM Event WHERE _id = :arg0")
    fun deleteEvent(id:Int)

    @Query("SELECT _id FROM Event")
    fun loadIds():List<Int>

}