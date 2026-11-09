package com.lexziewyn.toonteca.ui.comics

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lexziewyn.toonteca.domain.model.Weebcomic
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicDetailScreen(
    comicId: Int,
    viewModel: WeebcomicViewModel,
    onBack: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    val comic = (uiState as? ComicListUiState.Success)?.comics?.find { it.id == comicId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(comic?.titulo ?: "Detalle del Cómic") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    if (comic != null) {
                        IconButton(onClick = { onEditClick(comic.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (comic == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cómic no encontrado")
            }
            return@Scaffold
        }

        var vistosSlider by remember(comic.capitulosVistos) {
            mutableFloatStateOf(comic.capitulosVistos.toFloat())
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Título: ${comic.titulo}", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Demografía: ${comic.genero}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Estado: ${if (comic.terminado) "Terminado ✅" else "En Emisión ⏳"}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(text = "Total de capítulos: ${comic.capitulosTotales}", style = MaterialTheme.typography.bodyLarge)
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Progreso de lectura: ${vistosSlider.roundToInt()} / ${comic.capitulosTotales}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = vistosSlider,
                        onValueChange = { vistosSlider = it },
                        valueRange = 0f..comic.capitulosTotales.toFloat().coerceAtLeast(1f),
                        steps = if (comic.capitulosTotales > 1) comic.capitulosTotales - 1 else 0,
                        onValueChangeFinished = {
                            viewModel.updateCapitulosVistos(comic, vistosSlider.roundToInt())
                        }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Registro") },
                text = { Text("¿Estás seguro de que deseas eliminar '${comic.titulo}'?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteComic(comic.id) {
                            showDeleteDialog = false
                            onBack()
                        }
                    }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
