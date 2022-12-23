package com.example.grademp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * This file contains all the entities (even if not so many in this particular case) for the databases.
 */

@Entity(tableName = "member_of_finnish_parliament_table")
data class DatabaseMemberOfFinnishParliament constructor(
    @PrimaryKey(autoGenerate = false)
    val personNum: Int,

    @ColumnInfo(name = "first")
    val first: String,

    @ColumnInfo(name = "last")
    val last: String,

    @ColumnInfo(name = "bornYear")
    val bornYear: Int,

    @ColumnInfo(name = "constituency")
    val constituency: String,

    @ColumnInfo(name = "seatNumber")
    val seatNumber: Int,

    @ColumnInfo(name = "party")
    val party: String,

    @ColumnInfo(name = "minister")
    val minister: Boolean,

    @ColumnInfo(name = "pictureUrl")
    val picture: String,

    @ColumnInfo(name = "status")
    var status: Int = 0,

    @ColumnInfo(name = "twitterLink")
    val twitterLink: String
)

@Entity(tableName = "finnish_mp_notes")
data class MPNote constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "person_identifier")
    val personNum: Int,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "opinion")
    var opinion: Boolean,

    @ColumnInfo(name = "day")
    val day: Int,

    @ColumnInfo(name = "month")
    val month: Int,

    @ColumnInfo(name = "year")
    val year: Int
)
