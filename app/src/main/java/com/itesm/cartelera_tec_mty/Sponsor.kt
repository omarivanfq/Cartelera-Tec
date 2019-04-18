package com.itesm.cartelera_tec_mty

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Sponsor")
@Parcelize
data class Sponsor(@ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = false) val id: Int): Parcelable