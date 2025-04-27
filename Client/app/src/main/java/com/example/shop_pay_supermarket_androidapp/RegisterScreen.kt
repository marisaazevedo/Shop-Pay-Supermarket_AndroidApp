package com.example.shop_pay_supermarket_androidapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shop_pay_supermarket_androidapp.ui.theme.ShopPaySupermarket_AndroidAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterClick: (name: String, username: String, email: String, password: String, creditCard: String) -> Unit = { _, _, _, _, _ -> },
    onBackToLoginClick: () -> Unit = {},
    onBackClick: () -> Unit = {} // New parameter for back to main navigation
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var creditCardNumber by remember { mutableStateOf("") }

    // Error states
    var nameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var creditCardError by remember { mutableStateOf<String?>(null) }

    // Form validation state
    var isFormValid by remember { mutableStateOf(false) }

    // Luhn algorithm for credit card validation
    fun isLuhnValid(number: String): Boolean {
        if (number.length < 13) return false
        val digits = number.map { it.toString().toInt() }
        val reversedDigits = digits.reversed()

        var sum = 0
        reversedDigits.forEachIndexed { index, digit ->
            val value = if (index % 2 == 1) {
                val doubled = digit * 2
                if (doubled > 9) doubled - 9 else doubled
            } else {
                digit
            }
            sum += value
        }

        return sum % 10 == 0
    }

    // Format credit card input with spaces
    fun formatCreditCard(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }
        val formatted = StringBuilder()

        digitsOnly.forEachIndexed { index, char ->
            if (index > 0 && index % 4 == 0) formatted.append(" ")
            formatted.append(char)
        }

        return formatted.toString()
    }

    // Validation functions
    fun validateName() {
        nameError = when {
            name.isBlank() -> "Name cannot be empty"
            name.length < 2 -> "Name must be at least 2 characters long"
            else -> null
        }
    }

    fun validateUsername() {
        usernameError = when {
            username.isBlank() -> "Username cannot be empty"
            username.length < 3 -> "Username must be at least 3 characters long"
            username.contains(" ") -> "Username cannot contain spaces"
            else -> null
        }
    }

    fun validateEmail() {
        emailError = when {
            email.isBlank() -> "Email cannot be empty"
            !email.contains("@") || !email.contains(".") -> "Please enter a valid email address"
            else -> null
        }
    }

    fun validatePassword() {
        passwordError = when {
            password.isBlank() -> "Password cannot be empty"
            password.length < 6 -> "Password must be at least 6 characters long"
            !password.any { it.isDigit() } -> "Password must contain at least one number"
            !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            else -> null
        }
    }

    fun validateConfirmPassword() {
        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Please confirm your password"
            confirmPassword != password -> "Passwords do not match"
            else -> null
        }
    }

    fun validateCreditCard() {
        val digitsOnly = creditCardNumber.filter { it.isDigit() }
        creditCardError = when {
            digitsOnly.isBlank() -> "Credit card number cannot be empty"
            digitsOnly.length < 13 || digitsOnly.length > 19 -> "Credit card number must be between 13-19 digits"
            !isLuhnValid(digitsOnly) -> "Invalid credit card number"
            else -> null
        }
    }

    fun validateForm() {
        validateName()
        validateUsername()
        validateEmail()
        validatePassword()
        validateConfirmPassword()
        validateCreditCard()

        isFormValid = nameError == null && usernameError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null && creditCardError == null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Account") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Main Screen"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Form fields in a scrollable column would be ideal here for smaller screens
            Spacer(modifier = Modifier.height(16.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    validateName()
                },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    validateUsername()
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = usernameError != null,
                supportingText = { usernameError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    validateEmail()
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    validatePassword()
                    if (confirmPassword.isNotEmpty()) {
                        validateConfirmPassword()
                    }
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    validateConfirmPassword()
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmPasswordError != null,
                supportingText = { confirmPasswordError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Credit Card Field
            OutlinedTextField(
                value = creditCardNumber,
                onValueChange = { rawInput ->
                    // Only handle input if it would result in 19 or fewer digits
                    val digitCount = rawInput.count { it.isDigit() }
                    if (digitCount <= 19) {
                        creditCardNumber = formatCreditCard(rawInput)
                        validateCreditCard()
                    }
                },
                label = { Text("Credit Card Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = creditCardError != null,
                supportingText = { creditCardError?.let { Text(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    validateForm()
                    if (isFormValid) {
                        onRegisterClick(name, username, email, password, creditCardNumber.filter { it.isDigit() })
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotBlank() && username.isNotBlank() && email.isNotBlank() &&
                        password.isNotBlank() && confirmPassword.isNotBlank() && creditCardNumber.isNotBlank()
            ) {
                Text(text = "Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to Login Link
            TextButton(
                onClick = onBackToLoginClick
            ) {
                Text(text = "Already have an account? Log In")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ShopPaySupermarket_AndroidAppTheme {
        RegisterScreen()
    }
}