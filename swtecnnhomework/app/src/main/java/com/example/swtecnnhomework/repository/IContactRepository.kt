package com.example.swtecnnhomework.repository

import com.example.swtecnnhomework.model.Contact

interface IContactRepository {

    suspend fun getAllContacts(): ArrayList<Contact>

    fun addContact(contact: Contact)

}