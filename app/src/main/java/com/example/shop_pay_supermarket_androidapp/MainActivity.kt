package com.example.shop_pay_supermarket_androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.shop_pay_supermarket_androidapp.ui.LoginScreen
import com.example.shop_pay_supermarket_androidapp.ui.MainScreen
import com.example.shop_pay_supermarket_androidapp.ui.ProductListScreen
import com.example.shop_pay_supermarket_androidapp.ui.RegisterScreen
import com.example.shop_pay_supermarket_androidapp.ui.Product
import com.example.shop_pay_supermarket_androidapp.ui.theme.ShopPaySupermarket_AndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopPaySupermarket_AndroidAppTheme {
                var currentScreen by remember { mutableStateOf("main") }
                // Store selected product for details view
                var selectedProduct by remember { mutableStateOf<Product?>(null) }

                when (currentScreen) {
                    "main" -> MainScreen(
                        onLoginClick = { currentScreen = "login" },
                        onRegisterClick = { currentScreen = "register" }
                    )
                    "login" -> LoginScreen(
                        onLoginClick = {
                            // Navigate to products on successful login
                            currentScreen = "products"
                        },
                        onRegisterClick = { currentScreen = "register" },
                        onBackClick = { currentScreen = "main" } // New back navigation
                    )
                    "register" -> RegisterScreen(
                        onRegisterClick = { name, username, email, password, creditCard ->
                            // After registration, go back to login
                            println("Registered user: $name, $username, $email")
                            currentScreen = "login"
                        },
                        onBackToLoginClick = { currentScreen = "login" },
                        onBackClick = { currentScreen = "main" } // New back navigation
                    )
                    "products" -> ProductListScreen(
                        onBackClick = { currentScreen = "login" },
                        onProductClick = { product ->
                            // For future product details screen
                            selectedProduct = product
                            // currentScreen = "productDetails"
                        },
                        onCartClick = {
                            // For future cart screen
                            // currentScreen = "cart"
                        }
                    )
                }
            }
        }
    }
}