package com.example.gaia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.IOException

class SessionsFragment : Fragment() {
    private lateinit var startSessionButton: Button
    private lateinit var sessionHistoryListView: ListView
    private val baseUrl = "http://10.0.2.2:5000/"
    private val client = OkHttpClient()
    private val sessionHistory = mutableListOf<String>()
    private lateinit var username: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        username = arguments?.getString("USERNAME") ?: ""
        Log.d("SessionsFragment", "Username recebido: '$username'")

        val view = inflater.inflate(R.layout.fragment_start_session, container, false)

        startSessionButton = view.findViewById(R.id.start_session_button)
        sessionHistoryListView = view.findViewById(R.id.session_history_list_view)

        startSessionButton.setOnClickListener {
            startNewSession()
        }

        return view
    }

    private fun startNewSession() {
        // Lógica para iniciar uma nova sessão com a Gaia
        Toast.makeText(context, "Iniciando nova sessão com Gaia...", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), SessionActivity::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
    }



    private fun getUsername(): String {
        // Implemente uma lógica para obter o nome de usuário, por exemplo, a partir de SharedPreferences ou Intent
        return "usuario_teste"
    }
}
