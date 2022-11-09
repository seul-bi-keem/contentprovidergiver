package com.example.contentproviderexample

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

class GiverContentProvider : ContentProvider() {

    lateinit var db: ItemDatabase

    override fun onCreate(): Boolean {
        context?.let {
            db = ItemDatabase.getInstance(it)
        }

        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        Log.v(">>>", "query() - $uri")

        context?.let {
            if (selection != null && selection == "id") {
                if (selectionArgs != null && selectionArgs.isNotEmpty()) {
                    try {
                        val itemId = selectionArgs[0].toLong()
                        val cursor = db.itemDao().getItem(itemId)
                        cursor.setNotificationUri(it.contentResolver, uri)
                        return cursor
                    } catch (e: NumberFormatException) {

                    }
                }
            } else {
                val cursor = db.itemDao().getAllItem()
                cursor.setNotificationUri(it.contentResolver, uri)
                return cursor
            }
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String? {
        return "${GiverContract.AUTHORITY}.${GiverContract.TABLE_NAME}"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        context?.let {
            val id = db.itemDao().insertItem(Item.fromContentValues(values))
            if (id != -1L) {
                it.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        context?.let {
            val count = db.itemDao().deleteItem(ContentUris.parseId(uri))
            it.contentResolver.notifyChange(uri, null)
            return count
        }

        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        context?.let {
            val count = db.itemDao().updateItem(Item.fromContentValues(values))
            it.contentResolver.notifyChange(uri, null)
            return count
        }

        throw IllegalArgumentException("Failed to update row into $uri")
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        db.itemDao().deleteAll()
        context?.let {
                ItemDatabase.getInstance(it).itemDao().deleteAll()
                val prefs = it.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
                val contentResolverHelper = ContentResolverHelper(it)
                val key = prefs.all
                for (entry in key.entries) {
                    var content: String
                    if (entry.value != null) {
                        content = if (entry.value!! is String) {
                            entry.value as String
                        } else {
                            entry.value.toString()
                        }
                        contentResolverHelper.insertItem(entry.key, content)
                    }
                }
            }


        return null
    }
}