package com.ochoa.lab05.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ochoa.lab05.data.Session
import com.ochoa.lab05.data.currentUser
import com.ochoa.lab05.navigation.logout
import com.ochoa.lab05.ui.components.AppCard
import com.ochoa.lab05.ui.components.InfoRow
import com.ochoa.lab05.ui.components.InitialsAvatar
import com.ochoa.lab05.ui.theme.AppColors

private val HeaderLilac = Color(0xFFE1D5FF)      // encabezado lila
private val CoverPurple = Color(0xFF7E57C2)      // inicio del degradado (morado)
private val CoverMaroon = Color(0xFF7B1E3A)      // fin del degradado (guinda/marrón)
private val LogoutOrangePink = Color(0xFFFFD3C4) // fondo naranja/rosa
private val LogoutRed = Color(0xFFD32F2F)        // texto rojo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = HeaderLilac)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Portada: degradado horizontal morado → guinda, con el avatar centrado en ella
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(
                            Brush.horizontalGradient(listOf(CoverPurple, CoverMaroon))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    InitialsAvatar(
                        initials = currentUser.initials,
                        size = 110.dp,
                        borderColor = Color.White,
                        borderWidth = 4.dp,
                        fontSize = 36.sp
                    )
                }

                // Nombre completo debajo del marco circular
                Text(
                    text = currentUser.fullName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("Información Personal")
                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.White,
                        bordered = false
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            InfoRow(Icons.Filled.Person, "Nombre completo", currentUser.fullName)
                            InfoRow(Icons.Filled.Email, "Correo institucional", Session.email)
                            InfoRow(Icons.Filled.Phone, "Teléfono", currentUser.phone)
                        }
                    }

                    SectionTitle("Académico")
                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.White,
                        bordered = false
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            InfoRow(Icons.Filled.School, "Carrera", currentUser.career)
                            InfoRow(Icons.Filled.DateRange, "Ciclo actual", currentUser.cycle)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Botón naranja/rosa con texto rojo
            Button(
                onClick = { navController.logout() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LogoutOrangePink,
                    contentColor = LogoutRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp)
                    .height(52.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}