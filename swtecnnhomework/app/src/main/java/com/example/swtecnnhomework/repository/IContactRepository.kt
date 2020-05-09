package com.example.swtecnnhomework.repository

import com.example.swtecnnhomework.model.Contact

interface IContactRepository {

    fun getAllContacts(): Array<Contact>

}