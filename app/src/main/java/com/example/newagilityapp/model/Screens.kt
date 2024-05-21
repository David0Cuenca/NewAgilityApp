package com.example.newagilityapp.model

sealed class Screens (val route:String){
    object DashboardScreen:Screens("dashboard")
    object TaskScreen:Screens("task")
    object ListScreen:Screens("list")
    object SessionScreen:Screens("session")
    object ProjectScreen:Screens("project")
    object CalendarScreen:Screens("calendar")
    object NewProjectScreen:Screens("new_project")

}