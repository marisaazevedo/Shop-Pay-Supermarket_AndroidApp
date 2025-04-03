package com.example.shop_pay_supermarket_androidapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shop_pay_supermarket_androidapp.ui.theme.ShopPaySupermarket_AndroidAppTheme

@Composable
fun LoginScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder for Logo as Text
            Text(text = "ShopPay Supermarket", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = { /* TODO: Implement Login Navigation */ },
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Log In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = { /* TODO: Implement Register Navigation */ },
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Register")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ShopPaySupermarket_AndroidAppTheme {
        LoginScreen()
    }
}
