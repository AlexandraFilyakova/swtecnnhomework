package com.example.swtecnnhomework.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtecnnhomework.R
import com.example.swtecnnhomework.model.Contact
import kotlinx.android.synthetic.main.activity_contact_list.contactList
import kotlinx.android.synthetic.main.activity_contact_list.toolbar

class ContactListActivity : AppCompatActivity(), ContactAdapter.ContactAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        setSupportActionBar(toolbar)
        supportActionBar?.subtitle = "List of Contacts"

        contactList.layoutManager = LinearLayoutManager(this)
        contactList.adapter = ContactAdapter(this, arrayOf(Contact("Alex", "Alex", "123456", "alex@mail.ru"),
                Contact("Alex", "Alex", "123456", "alex@mail.ru"),
                Contact("Alex", "Alex", "123456", "alex@mail.ru")))

        (contactList.adapter as ContactAdapter).setContactAdapterListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addContact) {
            startActivity(ContactDetailsActivity.getDetailsIntent(this, true, null))
        }
        return true
    }

    override fun onInfoButtonClick(contact: Contact) {
        startActivity(ContactDetailsActivity.getDetailsIntent(this, false, contact))
    }

    override fun onItemClick(contact: Contact) {
        //Add CALL
        Toast.makeText(this, "CALL", Toast.LENGTH_LONG).show()
    }

}