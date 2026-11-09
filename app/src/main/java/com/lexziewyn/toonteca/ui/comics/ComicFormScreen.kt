package com.lexziewyn.toonteca.ui.comics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lexziewyn.toonteca.domain.model.Weebcomic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicFormScreen(
    comicId: Int?,
    viewModel: WeebcomicViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val existingComic = (uiState as? ComicListUiState.Success)?.comics?.find { it.id == comicId }

    val demografias = listOf("Kodomo", "Shonen", "Shoujo", "Seinen", "Josei", "Seijin", "Ecchi", "BL", "GL")

    var titulo by remember { mutableStateOf(existingComic?.titulo ?: "") }
    var capitulosTotales by remember { mutableStateOf(existingComic?.capitulosTotales?.toString() ?: "") }
    var seleccionadoGenero by remember { mutableStateOf(existingComic?.genero ?: demografias.first()) }
    var terminado by remember { mutableStateOf(existingComic?.terminado ?: false) }
    var expandedDropdown by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (comicId == null) "Nuevo Cómic" else "Editar Cómic") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del Weebcomic") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = capitulosTotales,
                onValueChange = { if (it.all { char -> char.isDigit() }) capitulosTotales = it },
                label = { Text("Cantidad de capítulos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = seleccionadoGenero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Demografía") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    demografias.forEach { demo ->
                        DropdownMenuItem(
                            text = { Text(demo) },
                            onClick = {
                                seleccionadoGenero = demo
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (terminado) "Historia Terminada" else "Historia No Terminada",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = terminado,
                    onCheckedChange = { terminado = it }
                )
            }

            if (errorMsg != null) {
                Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val caps = capitulosTotales.toIntOrNull()
                    if (titulo.isBlank() || caps == null || caps <= 0) {
                        errorMsg = "Completa el título y una cantidad válida de capítulos"
                        return@Button
                    }
                    val comic = Weebcomic(
                        id = existingComic?.id ?: 0,
                        titulo = titulo.trim(),
                        genero = seleccionadoGenero,
                        capitulosTotales = caps,
                        capitulosVistos = existingComic?.capitulosVistos?.coerceAtMost(caps) ?: 0,
                        terminado = terminado
                    )
                    viewModel.saveComic(comic) {
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cómic")
            }
        }
    }
}
