package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/** Campo de texto con borde fino. `icono` es opcional y se dibuja con el color primario del perfil. */
@Composable
fun CafeTextField(
    valor: String,
    onValor: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    teclado: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false,
    error: Boolean = false,
    singleLine: Boolean = true,
    minLineas: Int = 1,
    icono: ImageVector? = null
) {
    val colores = MaterialTheme.colorScheme
    val iconoInicial: (@Composable () -> Unit)? =
        if (icono != null) { { Icon(icono, contentDescription = null) } } else null

    OutlinedTextField(
        value = valor,
        onValueChange = onValor,
        label = { Text(etiqueta) },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        minLines = if (singleLine) 1 else minLineas,
        isError = error,
        shape = RoundedCornerShape(12.dp),
        leadingIcon = iconoInicial,
        visualTransformation = if (esClave) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else teclado),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colores.primary,
            unfocusedBorderColor = colores.outlineVariant,
            focusedLabelColor = colores.primary,
            focusedLeadingIconColor = colores.primary,
            unfocusedLeadingIconColor = colores.primary,
            errorLeadingIconColor = colores.error
        )
    )
}