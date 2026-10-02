package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.VerdeBodega
import com.tecsup.mibodega.ui.componentes.BotonPrimario

@Composable
fun ConfirmacionScreen(
    onVolverAlInicio: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.CheckCircle, contentDescription = "Confirmación", tint = VerdeBodega, modifier = Modifier.size(96.dp))
        Text(text = "¡Tu pedido ha sido confirmado!", style =  MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Text(text ="Tu pedido estará en camino pronto", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        BotonPrimario(texto = "Volver al inicio", onClick = onVolverAlInicio)
    }
}

