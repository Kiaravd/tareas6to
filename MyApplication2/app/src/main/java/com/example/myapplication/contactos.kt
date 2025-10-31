package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ContactosBinding // Asegúrate que este import es correcto

// Data class para guardar la información de cada contacto
data class Contacto(val nombre: String, val telefono: String)

class contactos : AppCompatActivity() {

    private lateinit var binding: ContactosBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private val allContacts = mutableListOf<Contacto>()
    private val READ_CONTACTS_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContactosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Comprobar si tenemos permiso. Si no, lo pedimos.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION_CODE)
        }

        // Añadir listener al campo de búsqueda para filtrar en tiempo real
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactsAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        contactsAdapter = ContactsAdapter(mutableListOf())
        binding.contactsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.contactsRecyclerView.adapter = contactsAdapter
    }

    // Función para cargar los contactos del teléfono
    private fun loadContacts() {
        val contentResolver = contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            allContacts.clear()
            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val phone = it.getString(phoneIndex)
                allContacts.add(Contacto(name, phone))
            }
        }

        contactsAdapter.updateContacts(allContacts)
    }

    // Gestionar la respuesta a la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                Toast.makeText(this, "Permiso denegado. No se pueden mostrar los contactos.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

class ContactsAdapter(private var contacts: MutableList<Contacto>) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private var allContacts: List<Contacto> = contacts.toList()

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contactNameTextView)
        val phoneTextView: TextView = itemView.findViewById(R.id.contactPhoneTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contacto, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.nameTextView.text = contact.nombre
        holder.phoneTextView.text = contact.telefono
    }


    override fun getItemCount() = contacts.size

    fun updateContacts(newContacts: List<Contacto>) {
        contacts.clear()
        contacts.addAll(newContacts)
        allContacts = newContacts.toList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            allContacts
        } else {
            allContacts.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
        contacts.clear()
        contacts.addAll(filteredList)
        notifyDataSetChanged()
    }
}
