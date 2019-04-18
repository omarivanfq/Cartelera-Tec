package com.itesm.cartelera_tec_mty

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Event")
@Parcelize
data class Event(@ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = false) val id: Int, val photo: String, val name: String, val startDateTime : String, val location: String, val sponsorId: Int, val cancelled: Boolean,
                 val description: String, val campus: String, val category: String, val categoryName: String, val cost: Double, val publicEvent: Boolean,
                 val endDateTime : String, val requirementsToRegister: String, val registrationUrl: String, val registrationDeadline: String, val schedule: String,
                 val facebookUrl:String, val twitterUrl:String, val contactPhone:String, val contactEmail:String, val contactName:String, val published:Boolean,
                 val cancelMessage:String, val languages: String, val prefix:String, val hasRegistration:Boolean, val petFriendly:Boolean, val majors:String,
                 val hasDeadline:Boolean, val registrationMessage:String, val tagNames:String, val maxCapacity:Int, val categoryId:Int, val registeredCount: Int,
                 val latitude:Double, val longitude:Double, val city:String, val state:String, val reviewStatus:String, val reviewComments:String, val applicantId:Int)
            : Parcelable