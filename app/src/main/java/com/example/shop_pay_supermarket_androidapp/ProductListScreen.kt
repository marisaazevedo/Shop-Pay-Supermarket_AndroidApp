package com.example.shop_pay_supermarket_androidapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shop_pay_supermarket_androidapp.ui.theme.ShopPaySupermarket_AndroidAppTheme
// Import coroutines
import kotlinx.coroutines.launch

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: String,
    val imageUrl: String? = null
)

// Cart item that tracks quantity
data class CartItem(
    val product: Product,
    val quantity: Int = 1
)

// Cart state class
class CartState {
    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items.toList()

    // Total number of items in cart (sum of quantities)
    val itemCount: Int get() = _items.sumOf { it.quantity }

    // Total price of all items in cart
    val totalPrice: Double get() = _items.sumOf { it.product.price * it.quantity }

    // Add an item to cart or increase quantity if already in cart
    fun addItem(product: Product) {
        val existingItem = _items.find { it.product.id == product.id }
        if (existingItem != null) {
            // Replace with updated quantity
            val index = _items.indexOf(existingItem)
            _items[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            _items.add(CartItem(product))
        }
    }

    // Remove an item from cart entirely
    fun removeItem(productId: Int) {
        _items.removeIf { it.product.id == productId }
    }

    // Update quantity of an item in cart
    fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeItem(productId)
            return
        }

        val existingItem = _items.find { it.product.id == productId }
        existingItem?.let {
            val index = _items.indexOf(it)
            _items[index] = it.copy(quantity = quantity)
        }
    }

    // Clear the cart
    fun clear() {
        _items.clear()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onBackClick: () -> Unit = {},
    onProductClick: (Product) -> Unit = {},
    onCartClick: () -> Unit = {},
    navigateToCartScreen: (CartState) -> Unit = {},
    cartState: CartState = remember { CartState() }
) {
    // Sample product data
    val products = remember {
        listOf(
            Product(1, "Fresh Apples", 3.99, "Red delicious apples, fresh from the orchard", "Fruits"),
            Product(2, "Organic Bananas", 2.49, "Organic bananas, perfect for smoothies", "Fruits"),
            Product(3, "Whole Milk", 4.29, "Fresh whole milk from local dairy farms", "Dairy"),
            Product(4, "Eggs (12-pack)", 5.99, "Large free-range eggs", "Dairy"),
            Product(5, "White Bread", 2.99, "Freshly baked white bread loaf", "Bakery"),
            Product(6, "Chicken Breast", 9.99, "Boneless, skinless chicken breast", "Meat"),
            Product(7, "Ground Beef", 8.49, "80% lean ground beef", "Meat"),
            Product(8, "Atlantic Salmon", 14.99, "Fresh Atlantic salmon fillets", "Seafood"),
            Product(9, "Spinach", 3.49, "Fresh spinach leaves", "Vegetables"),
            Product(10, "Tomatoes", 2.99, "Roma tomatoes", "Vegetables"),
            Product(11, "Potato Chips", 3.99, "Classic potato chips", "Snacks"),
            Product(12, "Chocolate Chip Cookies", 4.49, "Freshly baked chocolate chip cookies", "Bakery"),
            Product(13, "Orange Juice", 4.99, "100% pure squeezed orange juice", "Beverages"),
            Product(14, "Coffee Beans", 11.99, "Premium arabica coffee beans", "Beverages"),
            Product(15, "Pasta", 1.99, "Spaghetti pasta", "Dry Goods"),
            Product(16, "Rice", 3.49, "White rice, 2 lb bag", "Dry Goods"),
            Product(17, "Paper Towels", 8.99, "6-roll pack of paper towels", "Household"),
            Product(18, "Dish Soap", 3.99, "Liquid dish soap", "Household"),
            Product(19, "Toothpaste", 4.29, "Mint flavored toothpaste", "Personal Care"),
            Product(20, "Shampoo", 5.99, "Moisturizing shampoo", "Personal Care")
        )
    }

    var searchQuery by remember { mutableStateOf("") }

    // Add a snackbar host state to show feedback when products are added
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Products") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Modified cart button to show item count
                    BadgedBox(
                        badge = {
                            if (cartState.itemCount > 0) {
                                Badge {
                                    Text(text = cartState.itemCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onCartClick) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search products...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true
            )

            // Products list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products.filter {
                    searchQuery.isEmpty() ||
                            it.name.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true) ||
                            it.category.contains(searchQuery, ignoreCase = true)
                }) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product) },
                        onAddToCart = {
                            cartState.addItem(product)
                            // Show snackbar feedback using coroutineScope
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${product.name} added to cart",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        // Find quantity of this product in cart, if any
                        quantityInCart = cartState.items.find { it.product.id == product.id }?.quantity ?: 0
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onAddToCart: () -> Unit,
    quantityInCart: Int = 0
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.name.first().toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Product details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, end = 8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Show quantity in cart if any
                        if (quantityInCart > 0) {
                            Text(
                                text = "In cart: $quantityInCart",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }

                        // Add to cart button
                        IconButton(
                            onClick = onAddToCart,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add to cart"
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartState: CartState,
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total price display
                    Column {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            text = "$${String.format("%.2f", cartState.totalPrice)}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Checkout button
                    Button(
                        onClick = {
                            if (cartState.itemCount > 0) {
                                onCheckoutClick()
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Your cart is empty",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Checkout (${cartState.itemCount} items)")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartState.items.isEmpty()) {
            // Empty cart view
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your cart is empty",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add items to start shopping",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            // Cart items list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartState.items) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onIncreaseQuantity = {
                            cartState.addItem(cartItem.product)
                        },
                        onDecreaseQuantity = {
                            if (cartItem.quantity > 1) {
                                cartState.updateQuantity(cartItem.product.id, cartItem.quantity - 1)
                            } else {
                                cartState.removeItem(cartItem.product.id)
                            }
                        },
                        onRemoveItem = {
                            cartState.removeItem(cartItem.product.id)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "${cartItem.product.name} removed from cart",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cartItem.product.name.first().toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Product details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${cartItem.product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Item total
                Text(
                    text = "Total: $${String.format("%.2f", cartItem.product.price * cartItem.quantity)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Quantity controls
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Remove button
                    IconButton(
                        onClick = onRemoveItem,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, // Use a trash/delete icon in your actual implementation
                            contentDescription = "Remove",
                            tint = Color.Red
                        )
                    }

                    // Quantity controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 4.dp)
                    ) {
                        IconButton(
                            onClick = onDecreaseQuantity,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text(
                                text = "−",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Text(
                            text = "${cartItem.quantity}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = onIncreaseQuantity,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text(
                                text = "+",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val cartState = CartState().apply {
        addItem(Product(1, "Fresh Apples", 3.99, "Red delicious apples", "Fruits"))
        addItem(Product(2, "Organic Bananas", 2.49, "Organic bananas", "Fruits"))
        addItem(Product(1, "Fresh Apples", 3.99, "Red delicious apples", "Fruits")) // Add a second one to show quantity
    }

    ShopPaySupermarket_AndroidAppTheme {
        CartScreen(cartState = cartState)
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    ShopPaySupermarket_AndroidAppTheme {
        ProductListScreen()
    }
}