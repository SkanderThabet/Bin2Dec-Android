package com.skander.bin2dec.ui.main.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.maxTextLength
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skander.bin2dec.domain.model.BinaryNumber
import com.skander.bin2dec.ui.main.viewmodel.MainUiState
import com.skander.bin2dec.ui.main.viewmodel.MainViewModel


@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Binary to Decimal Converter",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            BinaryInputField(
                onValueChanged = { viewModel.onBinaryInput(it) }, uiState
            )
            Spacer(modifier = Modifier.height(24.dp))
            when (val state = uiState) {
                is MainUiState.Success -> {
                    Text(
                        text = "Decimal value: ${state.decimal}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                is MainUiState.Error -> {
                    Unit
                }
                MainUiState.Initial -> Unit
            }
        }
    }
}

@Composable
fun BinaryInputField(
    onValueChanged: (String) -> Unit,
    uiState: MainUiState
) {
    val state = rememberTextFieldState()
    val isError = uiState is MainUiState.Error

    LaunchedEffect(Unit) {
        snapshotFlow { state.text }.collect {
            onValueChanged(it.toString())
        }
    }

    TextField(
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text(if (isError) "Binary Input*" else "Binary Input") },
        supportingText = {
            Row {
                if (uiState is MainUiState.Error) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                Spacer(Modifier.weight(1f))
                Text("${state.text.length}/${BinaryNumber.MAX_LENGTH}")
            }
        },
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.semantics {
            maxTextLength = BinaryNumber.MAX_LENGTH
        }
    )
}
