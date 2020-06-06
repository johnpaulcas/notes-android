package com.johnpaulcas.notes.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by johnpaulcas on 06/06/2020.
 */
@Entity(tableName = "note_table")
class Note(
    @ColumnInfo(name = "title")
    val  title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "priority")
    val priority: Int): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}