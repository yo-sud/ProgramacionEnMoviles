package com.ochoa.lab05.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ochoa.lab05.data.sampleStudents
import com.ochoa.lab05.ui.components.AppCard
import com.ochoa.lab05.ui.components.InitialsAvatar
import com.ochoa.lab05.ui.theme.AppColors

private val LilacBox = Color(0xFFE6DAFF)   // cuadrado lila detrás de los iconos
private val CardGray = Color(0xFFEDEDED)   // tarjeta gris

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(navController: NavController, studentId: Int) {
    val student = sampleStudents.firstOrNull { it.id == studentId }
    if (student == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expediente Académico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.Background)
            )
        },
        containerColor = AppColors.Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Degradado morado → gris con esquinas inferiores redondeadas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(AppColors.Primary, Color(0xFF9E9E9E))
                            )
                        )
                )
                InitialsAvatar(
                    initials = "${student.firstName.first()}${student.lastName.first()}",
                    size = 112.dp,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    borderColor = AppColors.Primary,
                    borderWidth = 4.dp,
                    fontSize = 36.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${student.firstName} ${student.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = student.career,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tarjeta gris
                AppCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = CardGray
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        BoxedInfoRow(Icons.Filled.Badge, "ID", "2026-" + student.id.toString().padStart(4, '0'))
                        BoxedInfoRow(Icons.Filled.Email, "Correo", student.email)
                        BoxedInfoRow(Icons.Filled.Apartment, "Facultad", student.faculty)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = AppColors.CardBorder
                        )
                        // Biografía: solo título y texto, sin icono
                        Text(
                            text = "Biografía",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = student.bio,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

// Fila con el icono encerrado en un cuadrado lila
@Composable
private fun BoxedInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LilacBox, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = AppColors.Primary)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}