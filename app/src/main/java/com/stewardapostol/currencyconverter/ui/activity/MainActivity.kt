package com.stewardapostol.currencyconverter.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stewardapostol.currencyconverter.BuildConfig
import com.stewardapostol.currencyconverter.data.model.CurrencyCode
import com.stewardapostol.currencyconverter.data.util.Resource
import com.stewardapostol.currencyconverter.ui.theme.CurrencyConverterTheme
import com.stewardapostol.currencyconverter.ui.viewmodel.CurrencyConversionViewModel
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {

    val currencyViewmodel: CurrencyConversionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyConverterTheme {
                CurrencyConverterApp(currencyViewmodel)
            }
        }
    }
}
@Composable
fun CurrencyConverterApp(viewModel: CurrencyConversionViewModel) {
    val amountInput = rememberSaveable { mutableStateOf("") }
    val fromCurrency = rememberSaveable { mutableStateOf("") }
    val toCurrency = rememberSaveable { mutableStateOf("") }

    val conversionResult by viewModel.conversionResult.collectAsState()
    val currencyCodes by viewModel.supportedCurrencies.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchSupportedCurrencies(BuildConfig.API_KEY, true)
    }

    fun performConversion() {
        val amount = amountInput.value.toDoubleOrNull()
        if (amount != null && fromCurrency.value.isNotEmpty() && toCurrency.value.isNotEmpty()) {
            viewModel.fetchPairConversion(
                fromCurrency.value,
                toCurrency.value,
                amount.absoluteValue,
                BuildConfig.API_KEY,
                true
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Currency Converter",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Amount Input
        OutlinedTextField(
            value = amountInput.value,
            onValueChange = { amountInput.value = it },
            label = { Text("Amount") },
            placeholder = { Text("e.g. 100") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Enter a valid number to convert",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 8.dp, top = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Currency Dropdowns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CurrencyDropdown(
                label = "From",
                selectedCurrency = fromCurrency.value,
                currencies = currencyCodes.data,
                onCurrencySelected = { fromCurrency.value = it }
            )

            CurrencyDropdown(
                label = "To",
                selectedCurrency = toCurrency.value,
                currencies = currencyCodes.data,
                onCurrencySelected = { toCurrency.value = it }
            )
        }

        if (fromCurrency.value.isBlank() || toCurrency.value.isBlank()) {
            Text(
                text = "Please select both currencies",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 8.dp, start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Convert Button
        Button(
            onClick = ::performConversion,
            enabled = amountInput.value.isNotBlank() && fromCurrency.value.isNotBlank() && toCurrency.value.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF1976D2), shape = MaterialTheme.shapes.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2), // Background color
                contentColor = Color.White // Text color
            )
        ) {
            Text(
                text = "Convert",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Conversion Result
        when (val result = conversionResult) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }

            is Resource.Success -> {
                val value = result.data?.conversionResult
                if (value != null) {
                    Text(
                        text = "Result: $value",
                        color = Color(0xFF00FF00),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                } else {
                    Text("No result available.", style = MaterialTheme.typography.bodyLarge)
                }
            }

            is Resource.Error -> {
                Text(
                    text = "Error: ${result.error}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> Unit
        }
    }
}

@Composable
fun CurrencyDropdown(
    label: String,
    selectedCurrency: String,
    currencies: List<CurrencyCode>?,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.width(150.dp)
        ) {
            Text(
                text = if (selectedCurrency.isBlank()) "$label..." else "$label: $selectedCurrency",
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                color = Color.White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(300.dp)
        ) {
            currencies?.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(text="${currency.code} - ${currency.name}", color = Color.White) },
                    onClick = {
                        onCurrencySelected(currency.code)
                        expanded = false
                    }
                )
            }
        }
    }
}
