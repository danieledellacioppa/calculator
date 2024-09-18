package com.forteur.calculator

import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    // Variabili per tracciare la posizione della finestra
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Imposta la finestra come flottante
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        // Applica il flag per permettere alla finestra di muoversi liberamente
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setContent {
            DraggableCalculatorApp() // Mostra la calcolatrice e abilita il trascinamento
        }

        // Rendi la finestra trascinabile
        val layoutParams = window.attributes

        window.decorView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Memorizza le coordinate iniziali della finestra
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Aggiorna la posizione della finestra (sia in verticale che in orizzontale)
                    layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()

                    // Aggiorna le proprietà della finestra in tempo reale
                    window.attributes = layoutParams
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // Mantiene la posizione corrente della finestra
                    layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()

                    // Imposta la finestra con la posizione finale
                    window.attributes = layoutParams
                    true
                }
                else -> false
            }
        }
    }
}

@Composable
fun DraggableCalculatorApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Trascina per muovere la finestra", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Mostra la calcolatrice
        CalculatorApp()
    }
}

@Composable
fun CalculatorApp() {
    var result by remember { mutableStateOf("0") }
    var input by remember { mutableStateOf("") }

    // Definisci i bottoni della calcolatrice
    val buttons = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", "=", "+")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = input.ifEmpty { result },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    Button(
                        onClick = {
                            input = onButtonClick(label, input, result) { newResult -> result = newResult }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        Text(text = label)
                    }
                }
            }
        }
    }
}

fun onButtonClick(label: String, input: String, result: String, updateResult: (String) -> Unit): String {
    return when (label) {
        "=" -> {
            // Esegui il calcolo e aggiorna il risultato
            try {
                val calcResult = evaluateExpression(input)
                updateResult(calcResult)
                ""
            } catch (e: Exception) {
                "Errore"
            }
        }
        else -> {
            // Aggiungi il valore al display
            input + label
        }
    }
}

// Funzione semplice per valutare un'espressione matematica
fun evaluateExpression(expression: String): String {
    return try {
        val result = expression.toDoubleOrNull() ?: expression.split(" ").last().toDouble()
        result.toString()
    } catch (e: Exception) {
        "Errore"
    }
}
