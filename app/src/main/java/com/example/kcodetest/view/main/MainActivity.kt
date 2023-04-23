package com.example.kcodetest.view.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kcodetest.R
import com.example.kcodetest.databinding.ActivityMainBinding
import com.example.kcodetest.util.launchAndRepeatWithViewLifecycle
import com.example.kcodetest.viewmodel.main.ErrorViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val errorViewModel: ErrorViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.mainActivityNavHostFragment)
        binding.mainActivityBottomNavView.setupWithNavController(navController)

        launchAndRepeatWithViewLifecycle {
            launch {
                errorViewModel.displayErrorSnackbar.collect {
                    Snackbar.make(
                        binding.mainActivityCoordinatorLayout,
                        it.message.getString(this@MainActivity),
                        it.duration
                    ).show()
                }
            }
        }

    }
}