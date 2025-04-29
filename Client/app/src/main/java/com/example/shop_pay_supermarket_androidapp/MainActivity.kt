package com.example.shop_pay_supermarket_androidapp

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.shop_pay_supermarket_androidapp.ui.CartScreen
import com.example.shop_pay_supermarket_androidapp.ui.CartState
import com.example.shop_pay_supermarket_androidapp.ui.LoginScreen
import com.example.shop_pay_supermarket_androidapp.ui.MainScreen
import com.example.shop_pay_supermarket_androidapp.ui.Product
import com.example.shop_pay_supermarket_androidapp.ui.ProductListScreen
import com.example.shop_pay_supermarket_androidapp.ui.RegisterScreen
import com.example.shop_pay_supermarket_androidapp.ui.theme.ShopPaySupermarket_AndroidAppTheme
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopPaySupermarket_AndroidAppTheme {
                var currentScreen by remember { mutableStateOf("main") }
                // Store selected product for details view
                var selectedProduct by remember { mutableStateOf<Product?>(null) }
                // Create a cart state that persists across screen changes
                val cartState = remember { CartState() }

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
                            thread {
                                addUser(this, baseAddress = "10.0.2.2", port = 8000, userName = username)
                            }
                            println("Registered user: $name, $username, $email")
                            currentScreen = "login"
                        },
                        onBackToLoginClick = { currentScreen = "login" },
                        onBackClick = { currentScreen = "main" } // New back navigation
                    )
                    "products" -> ProductListScreen(
                        onBackClick = { currentScreen = "login" },
                        onProductClick = { product ->
                            selectedProduct = product
                        },
                        onCartClick = { currentScreen = "cart" },
                        navigateToCartScreen = { _ -> currentScreen = "cart" },
                        cartState = cartState
                    )
                    "cart" -> CartScreen(
                        cartState = cartState,
                        onBackClick = { currentScreen = "products" },
                        onCheckoutClick = {
                            // For future checkout implementation
                            // currentScreen = "checkout"
                        }
                    )
                }
            }
        }
    }
}