package com.example.newagilityapp.activites.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newagilityapp.model.Screens
import kotlinx.coroutines.launch

data class MenuItem(
    val title: String,
    val selected:ImageVector,
    val unselected: ImageVector,
    val badge: Boolean,
    val destination: String
)

val destinatiosDrawer = listOf(
    Screens.DashboardScreen,
    Screens.ListScreen
)

@Composable
fun DrawerContent(
    items:List<MenuItem>,
    modifier: Modifier,
    navHostController: NavHostController,
    drawerstate: DrawerState
){
    val scope= rememberCoroutineScope()
    var selectedItem by rememberSaveable { mutableIntStateOf(0)}
        items.forEachIndexed { index,item ->
            Spacer(modifier = Modifier.height(5.dp))
            NavigationDrawerItem(
                label = { Text(text = item.title)},
                selected = index == selectedItem,
                onClick = {
                    navHostController.navigate(item.destination)
                    scope.launch { drawerstate.close()}
                    selectedItem = index
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItem) {
                            item.selected
                        } else item.unselected,
                        contentDescription = item.title,
                    )
                },
                badge = {
                    if(item.badge) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription ="badge" )
                    }
                },
                modifier = modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
}