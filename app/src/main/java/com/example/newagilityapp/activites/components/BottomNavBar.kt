package com.example.newagilityapp.activites.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.newagilityapp.navBaritems

data class BottomNavegationItem(
    val title: String,
    val selectedIcon:ImageVector,
    val unselectdIcon: ImageVector,
    val notification: Boolean,
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(){
    var selectedItemIndex by rememberSaveable { mutableStateOf(1) }
    NavigationBar {
        navBaritems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = { selectedItemIndex = index
                    //Todo Navegacion
                },
                label = {
                    Text(text = item.title)
                },

                icon = {
                    BadgedBox(
                        badge = {
                            if(item.notification) {
                                Badge {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription =item.title
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if(index==selectedItemIndex)
                            {item.selectedIcon}
                            else{item.unselectdIcon},
                            contentDescription = item.title)
                    }
                }
            )
        }
    }
}