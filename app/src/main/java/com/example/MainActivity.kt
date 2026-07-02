package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.MahadevLogo
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

enum class AppRoute {
    SPLASH, HOME, BOOK_RIDE, FARE_ENQUIRY, ABOUT, CONTACT, MY_BOOKINGS
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val bookingViewModel: BookingViewModel = viewModel()
            val userDarkModeOverride by bookingViewModel.isDarkMode.collectAsState()
            
            // Fallback to system preference if no manual choice has been set
            val systemTheme = isSystemInDarkTheme()
            val useDarkTheme = userDarkModeOverride ?: systemTheme

            MyApplicationTheme(darkTheme = useDarkTheme) {
                var currentRoute by remember { mutableStateOf(AppRoute.SPLASH) }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // When user clicks back, handle route navigation manually if not on Home
                val handleBackPress = {
                    if (currentRoute != AppRoute.HOME && currentRoute != AppRoute.SPLASH) {
                        currentRoute = AppRoute.HOME
                    } else if (currentRoute == AppRoute.HOME) {
                        finish()
                    }
                }

                if (currentRoute == AppRoute.SPLASH) {
                    SplashScreen(
                        onSplashComplete = { currentRoute = AppRoute.HOME }
                    )
                } else {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier
                                    .width(310.dp)
                                    .testTag("nav_drawer_sheet"),
                                drawerContainerColor = MaterialTheme.colorScheme.surface
                            ) {
                                DrawerHeader()
                                
                                Spacer(modifier = Modifier.height(16.dp))

                                // Navigation Items
                                DrawerItem(
                                    label = "Home Dashboard",
                                    icon = Icons.Default.Home,
                                    selected = currentRoute == AppRoute.HOME,
                                    onClick = {
                                        currentRoute = AppRoute.HOME
                                        scope.launch { drawerState.close() }
                                    },
                                    tag = "drawer_item_home"
                                )

                                DrawerItem(
                                    label = "Book a Cab",
                                    icon = Icons.Default.DirectionsCar,
                                    selected = currentRoute == AppRoute.BOOK_RIDE,
                                    onClick = {
                                        currentRoute = AppRoute.BOOK_RIDE
                                        scope.launch { drawerState.close() }
                                    },
                                    tag = "drawer_item_book"
                                )

                                DrawerItem(
                                    label = "Fare Estimates",
                                    icon = Icons.Default.LocalAtm,
                                    selected = currentRoute == AppRoute.FARE_ENQUIRY,
                                    onClick = {
                                        currentRoute = AppRoute.FARE_ENQUIRY
                                        scope.launch { drawerState.close() }
                                    },
                                    tag = "drawer_item_fares"
                                )

                                DrawerItem(
                                    label = "My Ride Bookings",
                                    icon = Icons.Default.Book,
                                    selected = currentRoute == AppRoute.MY_BOOKINGS,
                                    onClick = {
                                        currentRoute = AppRoute.MY_BOOKINGS
                                        scope.launch { drawerState.close() }
                                    },
                                    tag = "drawer_item_bookings"
                                )

                                DrawerItem(
                                    label = "About Us",
                                    icon = Icons.Default.Info,
                                    selected = currentRoute == AppRoute.ABOUT,
                                    onClick = {
                                        currentRoute = AppRoute.ABOUT
                                        scope.launch { drawerState.close() }
                                    },
                                    tag = "drawer_item_about"
                                )

                                DrawerItem(
                                    label = "Contact & Office Map",
                                    icon = Icons.Default.ContactSupport,
                                    selected = currentRoute == AppRoute.CONTACT,
                                    onClick = {
                                        currentRoute = AppRoute.CONTACT
                                        scope.launch { drawerState.close() }
                                    },
                                    tag = "drawer_item_contact"
                                )

                                Spacer(modifier = Modifier.weight(1f))
                                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                                Spacer(modifier = Modifier.height(8.dp))

                                // Quick Theme Override Toggle Row inside Drawer
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp, vertical = 12.dp)
                                        .testTag("dark_mode_toggle_row"),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (useDarkTheme) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                                            contentDescription = "Theme Icon",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Dark Mode Override",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Switch(
                                        checked = useDarkTheme,
                                        onCheckedChange = {
                                            bookingViewModel.setDarkMode(it)
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = {
                                        Text(
                                            text = "Mahadev Travels",
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 20.sp,
                                            letterSpacing = 1.sp
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = { scope.launch { drawerState.open() } },
                                            modifier = Modifier.testTag("hamburger_menu_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Open Drawer",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    actions = {
                                        // Quick Toggle button inside top bar as well
                                        IconButton(
                                            onClick = {
                                                bookingViewModel.setDarkMode(!useDarkTheme)
                                            },
                                            modifier = Modifier.testTag("appbar_theme_toggle")
                                        ) {
                                            Icon(
                                                imageVector = if (useDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                                                contentDescription = "Toggle Dark Mode",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        titleContentColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            },
                            bottomBar = {
                                // Bottom Navigation for quick compact tabs (Home, Book, Fares, Bookings)
                                NavigationBar(
                                    modifier = Modifier.testTag("bottom_nav_bar"),
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 8.dp
                                ) {
                                    NavigationBarItem(
                                        selected = currentRoute == AppRoute.HOME,
                                        onClick = { currentRoute = AppRoute.HOME },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                        modifier = Modifier.testTag("bottom_tab_home")
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == AppRoute.BOOK_RIDE,
                                        onClick = { currentRoute = AppRoute.BOOK_RIDE },
                                        icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Book") },
                                        label = { Text("Book", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                        modifier = Modifier.testTag("bottom_tab_book")
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == AppRoute.FARE_ENQUIRY,
                                        onClick = { currentRoute = AppRoute.FARE_ENQUIRY },
                                        icon = { Icon(Icons.Default.LocalAtm, contentDescription = "Fares") },
                                        label = { Text("Fares", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                        modifier = Modifier.testTag("bottom_tab_fares")
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == AppRoute.MY_BOOKINGS,
                                        onClick = { currentRoute = AppRoute.MY_BOOKINGS },
                                        icon = { Icon(Icons.Default.Book, contentDescription = "Bookings") },
                                        label = { Text("Rides", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                        modifier = Modifier.testTag("bottom_tab_bookings")
                                    )
                                }
                            },
                            contentWindowInsets = WindowInsets.safeDrawing
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                when (currentRoute) {
                                    AppRoute.HOME -> HomeScreen(
                                        onNavigateToBook = { currentRoute = AppRoute.BOOK_RIDE },
                                        onNavigateToFares = { currentRoute = AppRoute.FARE_ENQUIRY }
                                    )
                                    AppRoute.BOOK_RIDE -> BookRideScreen(
                                        viewModel = bookingViewModel,
                                        onNavigateToBookings = { currentRoute = AppRoute.MY_BOOKINGS }
                                    )
                                    AppRoute.FARE_ENQUIRY -> FareEnquiryScreen()
                                    AppRoute.ABOUT -> AboutUsScreen()
                                    AppRoute.CONTACT -> ContactScreen()
                                    AppRoute.MY_BOOKINGS -> MyBookingsScreen(
                                        viewModel = bookingViewModel,
                                        onNavigateToBook = { currentRoute = AppRoute.BOOK_RIDE }
                                    )
                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF004786), // Deep Sleek Blue
                        Color(0xFF111318)  // Deep Sleek Slate Dark Background
                    )
                )
            ),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            MahadevLogo(size = 54.dp, animate = false)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Mahadev Travels",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Ahmedabad Helpdesk: +91 9157404508",
                color = Color(0xFFA8C7FF), // Sleek secondary ice-blue color
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .testTag(tag),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = Color.Transparent,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
