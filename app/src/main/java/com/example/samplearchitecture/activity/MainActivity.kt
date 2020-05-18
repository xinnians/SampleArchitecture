package com.example.samplearchitecture.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import com.example.samplearchitecture.Navigator
import com.example.samplearchitecture.R
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MainActivity : AppCompatActivity() {

    private val navigator: Navigator by lazy {
        XInjectionManager.findComponent<Navigator>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        navigator.bind(findNavController(this,R.id.layout_fragment))
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(this,R.id.layout_fragment).navigateUp()
}
