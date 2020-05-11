package com.example.swtecnnhomework.repository

import android.content.Context
import com.example.swtecnnhomework.model.Contact
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.lang.RuntimeException

class ContactRepository(var context: Context) {

    private var contacts: ArrayList<Contact>? = null

    private fun loadContacts() {
        val fileContent: String
        try {
            val assetManager = context.assets
            val stream: InputStream = assetManager.open("contacts_data.json")
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            fileContent = String(buffer)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        contacts = parseJsonToContacts(fileContent)
    }

    private fun parseJsonToContacts(json: String): ArrayList<Contact> {
        val contacts: ArrayList<Contact> = arrayListOf<Contact>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            contacts.add(Contact(id = i.toLong(),
                    firstname = obj.getString(FIRSTNAME),
                    lastname = obj.getString(LASTNAME),
                    phone = obj.getString(PHONE),
                    email = obj.getString(EMAIL))
            )
        }
        return contacts
    }

    fun getAllContacts(): ArrayList<Contact> {
        if (contacts.isNullOrEmpty()) {
            loadContacts()
        }
        return contacts ?: arrayListOf()
    }

    fun getSearchResults(searchText: String): ArrayList<Contact> {
        val searchContacts: ArrayList<Contact> = arrayListOf()
        contacts?.forEach {
            if (it.firstname.contains(searchText, true)
                    || it.lastname.contains(searchText, true)
                    || it.phone.contains(searchText, true)) {
                searchContacts.add(it)
            }
        }
        return searchContacts
    }

    fun updateContact(newContact: Contact): ArrayList<Contact> {
        contacts?.let { contacts ->
            if (contacts.find { contact -> contact.id == newContact.id } != null) {
                contacts.find { contact -> contact.id == newContact.id }?.let {
                    it.apply {
                        this.firstname = newContact.firstname
                        this.lastname = newContact.lastname
                        this.phone = newContact.phone
                        this.email = newContact.email
                    }
                }
            } else {
                contacts.add(Contact(contacts.size.toLong(),
                        newContact.firstname, newContact.lastname, newContact.phone, newContact.email))
            }
        }
        return contacts ?: arrayListOf()
    }

    companion object {
        private const val FIRSTNAME = "firstname"
        private const val LASTNAME = "lastname"
        private const val PHONE = "phone"
        private const val EMAIL = "email"

        fun getInstance(context: Context): ContactRepository {
            return ContactRepository(context)
        }
    }
}