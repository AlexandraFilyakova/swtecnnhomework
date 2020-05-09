package com.example.swtecnnhomework.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.swtecnnhomework.R
import com.example.swtecnnhomework.model.Contact
import kotlinx.android.synthetic.main.contact_item_layout.view.infoButton
import kotlinx.android.synthetic.main.contact_item_layout.view.nameTextView
import kotlinx.android.synthetic.main.contact_item_layout.view.phoneTextView

class ContactAdapter(private val context: Context, private val contacts: Array<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var listener: ContactAdapterListener? = null
    fun setContactAdapterListener(listener: ContactAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContactViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.contact_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.setContact(contacts[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(contacts[position])
        }
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var currentContact: Contact? = null

        init {
            itemView.infoButton.setOnClickListener {
                currentContact?.let {
                    listener?.onInfoButtonClick(it)
                }
            }
        }

        fun setContact(contact: Contact) {
            currentContact = contact
            itemView.nameTextView.text = contact.firstname + " " + contact.surname
            itemView.phoneTextView.text = contact.phone
        }
    }

    interface ContactAdapterListener {
        fun onInfoButtonClick(contact: Contact)
        fun onItemClick(contact: Contact)
    }
}