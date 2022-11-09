package com.example.contentproviderexample

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log

class ContentResolverHelper(private var context: Context) {
    private var contentResolver: ContentResolver = context.contentResolver

    /**
     * select all items
     */
    fun getAllItems(): List<Item> {

        val list = ArrayList<Item>()

        val cursor = contentResolver.query(GiverContract.CONTENT_URI, null, null, null, null)

        if (cursor != null && cursor.count > 0) {

            while (cursor.moveToNext()) {

                val itemIdIndex = cursor.getColumnIndex("itemId")
                val titleIndex = cursor.getColumnIndex("key")
                val contentIndex = cursor.getColumnIndex("value")

                val id = cursor.getLong(itemIdIndex)
                val title = cursor.getString(titleIndex)
                val content = cursor.getString(contentIndex)

                val item = Item(id, title, content)
                list.add(item)

                Log.v(">>>", "@# id[$id] title[$title] content[$content]")
            }
        }

        return list
    }

    /**
     * select single item
     */
    fun getItem(id: Long): Item? {

        val cursor = contentResolver.query(GiverContract.CONTENT_URI, null, "id", arrayOf("$id"), null)

        if (cursor != null && cursor.count > 0) {

            while (cursor.moveToNext()) {

                val itemIdIndex = cursor.getColumnIndex("itemId")
                val titleIndex = cursor.getColumnIndex("key")
                val contentIndex = cursor.getColumnIndex("value")

                val id = cursor.getLong(itemIdIndex)
                val title = cursor.getString(titleIndex)
                val content = cursor.getString(contentIndex)

                return Item(id, title, content)
            }
        }

        return null
    }

    /**
     * Insert
     */
    fun insertItem(title: String, content: String) {

        val contentValues = generateItem(title, content)
        contentResolver.insert(GiverContract.CONTENT_URI, contentValues)
    }

    /**
     * Remove
     */
    fun removeItem(id: Long) {

        val uriString = "${GiverContract}.URL/$id"

        contentResolver.delete(Uri.parse(uriString), "id", arrayOf("$id"))
    }

    /**
     * Item 생성 (ContentValues)
     */
    private fun generateItem(title: String, content: String): ContentValues {

        val values = ContentValues()
        values.put("key", title)
        values.put("value", content)

        return values
    }

}