package com.petal.handsfree.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat

class AndroidCallDialer(private val context: Context) : CallDialer {

    companion object {
        private const val TAG = "CallHandler"
    }

    override fun hasCallPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun dial(cleanNumber: String): Boolean {
        return try {
            val callIntent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$cleanNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(callIntent)
            Log.i(TAG, "Call initiated successfully to: $cleanNumber")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error initiating call", e)
            false
        }
    }

    override fun findContactByName(contactName: String): String? {
        Log.d(TAG, "Searching for contact: $contactName")

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "READ_CONTACTS permission not granted")
            return null
        }

        try {
            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
                arrayOf("%$contactName%"),
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    if (nameIndex >= 0 && numberIndex >= 0) {
                        val foundName = it.getString(nameIndex)
                        val foundNumber = it.getString(numberIndex)
                        Log.i(TAG, "Found contact: $foundName -> $foundNumber")
                        return PhoneNumberUtils.clean(foundNumber)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error searching contacts", e)
        }

        Log.w(TAG, "Contact not found: $contactName")
        return null
    }
}
