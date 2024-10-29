    package com.example.gaia

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.RadioButton
    import android.widget.RadioGroup
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import okhttp3.*
    import com.google.gson.Gson
    import com.google.gson.JsonObject
    import java.io.IOException


    class TestActivity : AppCompatActivity() {
        private var currentQuestionIndex = 0
        private lateinit var PostgresHelper: PostgresHelper
        private lateinit var databaseHelper: DatabaseHelper

        private val baseUrl = "http://10.0.2.2:5000/"
        private val client = OkHttpClient()



        private lateinit var questionTextView: TextView
        private lateinit var radioGroup: RadioGroup
        private lateinit var nextButton: Button

        private val questions = arrayOf(
            "1. Sentiu-se triste ou deprimido(a) nos últimos 7 dias?",
            "2. Sentiu-se nervoso(a) ou ansioso(a) nos últimos 7 dias?",
            "3. Teve dificuldades para dormir nos últimos 7 dias?",
            "4. Sentiu-se sem esperança nos últimos 7 dias?",
            "5. Perdeu o interesse em fazer coisas que costumava gostar nos últimos 7 dias?",
            "6. Sentiu-se irritado(a) com frequência?",
            "7. Teve dificuldade em se concentrar?",
            "8. Sentiu-se sobrecarregado(a) pela pressão?",
            "9. Sentiu-se sem controle sobre suas ações?",
            "10. Teve pensamentos de suicídio?",
            "11. Sentiu-se isolado(a) ou solitário(a)?",
            "12. Experimentou mudanças de humor frequentes?",
            "13. Sentiu-se apático(a) ou sem energia?",
            "14. Teve dificuldades em lidar com as responsabilidades do dia a dia?",
            "15. Sentiu-se ansioso(a) em situações sociais?",
            "16. Sentiu-se preocupado(a) com sua saúde física?",
            "17. Teve ataques de pânico?",
            "18. Teve dificuldades em manter relacionamentos?",
            "19. Sentiu-se incapaz de lidar com a vida cotidiana?",
            "20. Sentiu-se incapaz de relaxar?",
            "21. Teve medos irracionais?",
            "22. Sentiu-se sem motivação?",
            "23. Teve dificuldade em se lembrar de coisas?",
            "24. Sentiu-se excessivamente preocupado(a) com o que os outros pensam de você?",
            "25. Teve dificuldades em se expressar?",
            "26. Sentiu-se excessivamente emocional?",
            "27. Experimentou mudanças no apetite?",
            "28. Sentiu-se confuso(a) com frequência?",
            "29. Sentiu-se culpado(a) sem motivo aparente?",
            "30. Teve dificuldades em tomar decisões?",
            "31. Teve pensamentos repetitivos ou intrusivos?",
            "32. Sentiu-se desconfortável em situações sociais?",
            "33. Teve dificuldades em controlar sua raiva?",
            "34. Sentiu-se constantemente cansado(a)?",
            "35. Teve dificuldades em dormir à noite?",
            "36. Sentiu-se insatisfeito(a) com sua vida?",
            "37. Sentiu-se incapaz de desfrutar das atividades que costumava gostar?",
            "38. Teve sentimentos de inadequação?",
            "39. Teve dificuldades em seguir instruções?",
            "40. Sentiu-se ansioso(a) em relação ao futuro?",
            "41. Teve problemas para se concentrar em tarefas simples?",
            "42. Sentiu-se frustrado(a) com frequência?",
            "43. Teve medos de perder o controle?",
            "44. Sentiu-se pressionado(a) por outras pessoas?",
            "45. Teve dificuldades em estabelecer limites pessoais?",
            "46. Sentiu-se incapaz de lidar com críticas?",
            "47. Teve dificuldades em lidar com mudanças?",
            "48. Sentiu-se incapaz de confiar nas pessoas?",
            "49. Teve pensamentos negativos sobre si mesmo(a)?",
            "50. Sentiu-se irritado(a) sem motivo aparente?",
            "51. Sentiu-se sobrecarregado(a) emocionalmente?",
            "52. Teve dificuldades em relaxar ou descansar?",
            "53. Sentiu-se desconectado(a) de suas emoções?",
            "54. Teve dificuldades em se sentir satisfeito(a) com a vida?",
            "55. Sentiu-se incapaz de encontrar alegria nas pequenas coisas?",
            "56. Teve dificuldade em aceitar elogios?",
            "57. Sentiu-se frequentemente ansioso(a) ou preocupado(a)?",
            "58. Teve dificuldades em iniciar ou completar tarefas?",
            "59. Sentiu-se perdido(a) em sua vida?",
            "60. Teve dificuldades em manter a calma sob pressão?",
            "61. Sentiu-se excessivamente crítico(a) consigo mesmo(a)?",
            "62. Teve dificuldades em lidar com a frustração?",
            "63. Sentiu-se frequentemente triste ou melancólico(a)?",
            "64. Teve dificuldades em entender as emoções dos outros?",
            "65. Sentiu-se inseguro(a) em situações sociais?",
            "66. Teve dificuldades em estabelecer e manter relacionamentos?",
            "67. Sentiu-se distante dos outros?",
            "68. Teve pensamentos de que não era bom o suficiente?",
            "69. Sentiu-se incapaz de se comprometer com outras pessoas?",
            "70. Teve dificuldade em se sentir seguro(a) em situações novas?",
            "71. Sentiu-se frequentemente cansado(a) ou exausto(a)?",
            "72. Teve dificuldades em se organizar?",
            "73. Sentiu-se excessivamente estressado(a)?",
            "74. Teve problemas de memória?",
            "75. Sentiu-se incapaz de lidar com conflitos?",
            "76. Teve dificuldades em lidar com as expectativas dos outros?",
            "77. Sentiu-se frequentemente sobrecarregado(a) por suas responsabilidades?",
            "78. Teve sentimentos de desespero?",
            "79. Sentiu-se frequentemente ansioso(a) sem motivo aparente?",
            "80. Teve dificuldades em fazer escolhas?",
            "81. Sentiu-se frequentemente desconfortável em seu próprio corpo?",
            "82. Teve problemas de autoimagem?",
            "83. Sentiu-se incapaz de relaxar ou descansar adequadamente?",
            "84. Teve dificuldades em lidar com críticas construtivas?",
            "85. Sentiu-se frequentemente culpado(a) por coisas que não fez?",
            "86. Teve dificuldades em lidar com suas emoções?",
            "87. Sentiu-se frequentemente excluído(a) ou rejeitado(a)?",
            "88. Teve dificuldades em confiar em si mesmo(a)?",
            "89. Sentiu-se incapaz de expressar suas necessidades?",
            "90. Sentiu-se frequentemente em conflito com os outros?")

        private val userResponses = IntArray(questions.size)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_test)

            databaseHelper = DatabaseHelper(this)
            PostgresHelper = PostgresHelper()

            questionTextView = findViewById(R.id.questionTextView)
            radioGroup = findViewById(R.id.radioGroup)
            nextButton = findViewById(R.id.nextButton)

            // Verificar se é o primeiro login do usuário
            val username = intent.getStringExtra("USERNAME") ?: ""

            checkExistingDiagnosis(username)
            showQuestion()



            nextButton.setOnClickListener {
                val selectedId = radioGroup.checkedRadioButtonId
                if (selectedId != -1) {
                    val radioButton = findViewById<RadioButton>(selectedId)
                    userResponses[currentQuestionIndex] = radioButton.text.toString().toInt()
                    currentQuestionIndex++

                    if (currentQuestionIndex < questions.size) {
                        showQuestion()
                    } else {
                        calculateDiagnosis()
                    }
                } else {
                    Toast.makeText(this, "Por favor, selecione uma resposta.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun checkExistingDiagnosis(username: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val request = Request.Builder()
                    .url("${baseUrl}check_diagnosis?username=$username")
                    .get()
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Erro ao verificar diagnóstico: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body?.string()
                            val jsonObject = Gson().fromJson(jsonResponse, JsonObject::class.java)

                            if (jsonObject.get("has_diagnosis").asBoolean) {
                                // Redirecionar para MainActivity apenas se o diagnóstico já existir
                                val intent = Intent(this@TestActivity, MainActivity::class.java)
                                intent.putExtra("USERNAME", username)
                                startActivity(intent)
                                finish() // Fecha a TestActivity
                            }
                        } else {
                            println("Erro ao verificar diagnóstico: ${response.message}")
                        }
                    }
                })
            }
        }

        private fun showQuestion() {
            questionTextView.text = questions[currentQuestionIndex]
            radioGroup.clearCheck()
        }

        private fun calculateDiagnosis() {
            val diagnosis = analyzeResponses(userResponses)

            // Armazenar a hipótese de diagnóstico no banco de dados
            val username = intent.getStringExtra("USERNAME") ?: ""
            println("Username capturado: $username")  // Adicione esta linha


            PostgresHelper.storeDiagnosis(username, diagnosis)
            databaseHelper.setFirstLoginCompleted(username)


            Toast.makeText(this, "Teste concluído! Hipótese de diagnóstico: $diagnosis", Toast.LENGTH_LONG).show()
            println("Hipótese de diagnostico $diagnosis")
            // Retornar à MainActivity ou outra Activity
            startActivity(Intent(this, MainActivity::class.java))
            intent.putExtra("USERNAME", username)
            startActivity(intent)
            finish() // Encerra a atividade atual
        }

        private fun analyzeResponses(responses: IntArray): String {
            val scores = mutableMapOf<String, Int>(
                "Depressão" to 0,
                "Ansiedade" to 0,
                "TDAH" to 0,
                "TEA" to 0,
                "Bipolaridade" to 0,
                "TOC" to 0,
                "TEPT" to 0,
                "Transtorno de Personalidade" to 0
            )

            for (i in responses.indices) {
                when (i) {
                    // Depressão
                    0, 1, 3, 4, 62 -> scores["Depressão"] = scores["Depressão"]!! + responses[i]

                    // Ansiedade
                    2, 5, 17, 40, 10 -> scores["Ansiedade"] = scores["Ansiedade"]!! + responses[i]

                    // TDAH
                    7, 8, 32, 59, 42 -> scores["TDAH"] = scores["TDAH"]!! + responses[i]

                    // TEA
                    33, 56, 50, 52, 28 -> scores["TEA"] = scores["TEA"]!! + responses[i]

                    // Bipolaridade
                    12, 13, 61, 60, 62 -> scores["Bipolaridade"] = scores["Bipolaridade"]!! + responses[i]

                    // TOC
                    31, 56, 64, 43, 49 -> scores["TOC"] = scores["TOC"]!! + responses[i]

                    // TEPT
                    10, 61, 78, 79, 80 -> scores["TEPT"] = scores["TEPT"]!! + responses[i]

                    // Transtorno de Personalidade
                    46, 47, 65, 66, 67 -> scores["Transtorno de Personalidade"] = scores["Transtorno de Personalidade"]!! + responses[i]
                }
            }

            val diagnosis = mutableListOf<String>()

            scores.forEach { (disorder, score) ->
                when {
                    score >= 10 -> diagnosis.add("$disorder (Alto risco)")
                    score in 5..9 -> diagnosis.add("$disorder (Médio risco)")
                    score < 5 -> diagnosis.add("$disorder (Baixo risco)")
                }
            }

            return if (diagnosis.isNotEmpty()) {
                diagnosis.joinToString(", ")
            } else {
                "Nenhum transtorno aparente"
            }
        }
    }