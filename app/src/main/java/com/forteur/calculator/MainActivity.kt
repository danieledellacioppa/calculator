package com.forteur.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp() // Qui stai definendo la UI interamente in Compose
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display della calcolatrice
        BasicTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 32.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bottoni della calcolatrice
        CalculatorButtons { value ->
            input += value
        }
    }
}

@Composable
fun CalculatorButtons(onButtonClick: (String) -> Unit) {
    val buttons = listOf(
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", ".", "=", "+"
    )

    Column {
        for (i in 0..3) {
            Row {
                for (j in 0..3) {
                    val buttonValue = buttons[i * 4 + j]
                    Button(
                        onClick = { onButtonClick(buttonValue) },
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    ) {
                        Text(text = buttonValue, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}
