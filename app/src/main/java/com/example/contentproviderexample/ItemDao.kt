package com.example.contentproviderexample

import android.database.Cursor
import androidx.room.*

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item): Long

    @Update
    fun updateItem(item: Item): Int

    @Query("DELETE FROM item WHERE itemId = :id")
    fun deleteItem(id: Long): Int

    @Query("DELETE FROM item")
    fun deleteAll(): Int

    @Query("SELECT * FROM item WHERE itemId = :id")
    fun getItem(id: Long) : Cursor

    @Query("SELECT * FROM item")
    fun getAllItem(): Cursor
}