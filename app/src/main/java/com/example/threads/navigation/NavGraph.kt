package com.example.threads.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threads.screens.AddThreads
import com.example.threads.screens.BottomNav
import com.example.threads.screens.Home
import com.example.threads.screens.Login
import com.example.threads.screens.Notification
import com.example.threads.screens.OtherUsers
import com.example.threads.screens.Profile
import com.example.threads.screens.Register
import com.example.threads.screens.Search
import com.example.threads.screens.Splash

@Composable
fun NavGraph(navController: NavHostController){

    NavHost(navController = navController, startDestination = Routes.Splash.routes){


        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.AddThread.routes){
            AddThreads(navController)
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.Login.routes){
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUsers.routes){
            val data = it.arguments!!.getString("data")
            OtherUsers(navController, data!!)
        }

    }
}