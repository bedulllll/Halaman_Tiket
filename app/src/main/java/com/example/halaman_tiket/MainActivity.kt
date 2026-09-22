package com.example.halaman_tiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val ticketPrice by remember { mutableStateOf(50000) }
                    var ticketCount by rememberSaveable { mutableStateOf(1) }
                    var buyerName by rememberSaveable { mutableStateOf("") }

                    var status by remember { mutableStateOf("Silakan pesan tiket") }
                    var orderCount by remember { mutableStateOf(0) } // b

                    TicketScreen(
                        modifier = Modifier,
                        contentPadding = innerPadding,
                        price = ticketPrice,
                        count = ticketCount,
                        name = buyerName,
                        status = status,
                        onNameChange = { buyerName = it },
                        onMinusClick = { if (ticketCount > 1) ticketCount-- },
                        onPlusClick = { ticketCount++ },
                        onOrderClick = { orderCount++ }
                    )

                    LaunchedEffect(orderCount) {
                        if (orderCount > 0) {
                            if (buyerName.isBlank()) {
                                status = "Nama Masih Kosong"
                            } else {
                                status = "Memproses pesanan..."
                                delay(5000) //
                                status = "Tiket telah dipesan!"
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketScreen(
    modifier: Modifier,
    price: Int,
    count: Int,
    name: String,
    status: String,
    onNameChange: (String) -> Unit,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    onOrderClick: () -> Unit,
    contentPadding: PaddingValues
) {
    val blue = Color(0xFF3F5BBF)

    val isProcessing = status == "Memproses pesanan..."
    val isSuccess = status == "Tiket telah dipesan"
    val isError = status == "Nama Masih Kosong"

    var statusBackground = Color(0xFFF1F4FA)
    var statusColor = Color(0xFF424242)
    if (isProcessing) {
        statusBackground = Color(0xFFE8F0FE)
        statusColor = Color(0xFF1A56DB)
    } else if (isSuccess) {
        statusBackground = Color(0xFFE6F4EA)
        statusColor = Color(0xFF2E7D32)
    } else if (isError) {
        statusBackground = Color(0xFFFDECEC)
        statusColor = Color(0xFFC62828)
    }

    Column(modifier) {
        Text(
            text = "Pemesanan Tiket",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .background(blue)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp,
                    top = contentPadding.calculateTopPadding() + 20.dp
                )
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Nama", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text(text = "Masukkan nama Anda") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Text(
                text = "Jumlah Tiket",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Button(
                    onClick = onMinusClick,
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8EEFB),
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "-" ,
                        fontSize = 32.sp
                    )
                }
                Text(
                    text = "$count",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onPlusClick,
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE8EEFB),
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "+",
                        fontSize = 32.sp
                    )
                }
            }

            Text(text = "Harga Tiket: Rp $price", modifier = Modifier.padding(top = 16.dp))
            Text(text = "Total: Rp ${price * count}")

            Button(
                onClick = onOrderClick,
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(containerColor = blue),
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Pesan Tiket")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(10)
                    )
                    .background(statusBackground)
                    .padding(16.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = statusColor
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                } else if (isSuccess) {
                    Text(text = "✅ ")
                } else if (isError) {
                    Text(text = "❗ ")
                }
                Text(
                    text = "Status: $status", color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}