package com.example.contentproviderexample

import android.net.Uri

object GiverContract {
    const val TABLE_NAME = "item"
    const val AUTHORITY = "com.example.contentproviderexample"
    const val URI_STRING = "content://$AUTHORITY/$TABLE_NAME"
    val CONTENT_URI: Uri = Uri.parse(URI_STRING)
}