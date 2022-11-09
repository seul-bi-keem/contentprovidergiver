package com.example.contentproviderexample

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "item")
data class Item(@PrimaryKey(autoGenerate = true) var itemId: Long = 0L, var key: String = "", var value: String = "") {
    companion object {
        fun fromContentValues(values: ContentValues?): Item {
            val item = Item()
            values?.let {
                if (values.containsKey("key")) item.key = values.getAsString("key")
                if (values.containsKey("value")) item.value = values.getAsString("value")
            }

            return item
        }
    }
}
