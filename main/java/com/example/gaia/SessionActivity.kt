package com.example.gaia

import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class SessionActivity : AppCompatActivity() {
    private lateinit var username: String
    private lateinit var webView: WebView
    private lateinit var endSessionButton: Button

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.MINUTES)
        .readTimeout(10, TimeUnit.MINUTES)
        .writeTimeout(10, TimeUnit.MINUTES)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        username = intent.getStringExtra("USERNAME") ?: ""

        webView = findViewById(R.id.web_view)
        endSessionButton = findViewById(R.id.end_session_button)

        // Configurar o WebView
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "Página carregada: $url")
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Log.e("WebView", "Erro ao carregar a página: ${error?.description}")
            }
        }

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true // Habilita o armazenamento local
        webView.settings.allowFileAccess = true // Permite o acesso a arquivos
        webView.loadUrl("https://chatgpt.com/share/672120ce-4248-8013-ab3a-eff83ada7235")

        checkOrCreateUserHistory(username)

        endSessionButton.setOnClickListener {
            finish() // Encerra a sessão e fecha a atividade
        }
    }

    private fun checkOrCreateUserHistory(username: String) {
        Log.d("SessionActivity", "Verificando ou criando histórico para o usuário: '$username'")
        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder()
                .url("http://10.0.2.2:5000/check_or_create_history?username=$username")
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@SessionActivity, "Erro ao verificar ou criar histórico: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        // O histórico foi verificado ou criado com sucesso
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@SessionActivity, "Erro ao verificar ou criar histórico: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}
