package com.example.shop_pay_supermarket_androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shop_pay_supermarket_androidapp.ui.LoginScreen
import com.example.shop_pay_supermarket_androidapp.ui.theme.ShopPaySupermarket_AndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopPaySupermarket_AndroidAppTheme {
                LoginScreen()
            }
        }
    }
}
