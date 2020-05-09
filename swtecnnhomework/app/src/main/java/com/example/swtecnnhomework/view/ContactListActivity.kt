package com.example.swtecnnhomework.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swtecnnhomework.R
import com.example.swtecnnhomework.model.Contact
import kotlinx.android.synthetic.main.activity_contact_list.*


class ContactListActivity : AppCompatActivity(), ContactAdapter.ContactAdapterListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        setSupportActionBar(toolbar)
        supportActionBar?.subtitle = "List of Contacts"

        requestCallPhonePermission()

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + contact.phone)
            startActivity(intent)
        } else {
            requestCallPhonePermission()
        }
    }

    private fun requestCallPhonePermission() {
        val permissions = arrayOf(Manifest.permission.CALL_PHONE)
        ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_PHONE_CALL_PERM)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val RC_HANDLE_PHONE_CALL_PERM = 56
    }

}