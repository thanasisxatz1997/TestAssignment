package com.example.testassignment

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testassignment.databinding.ActivityStatsBinding
import com.example.testassignment.utils.StatsStorage
import com.example.testassignment.viewModels.StatsViewModel
import com.example.testassignment.viewModels.StatsViewModelFactory

class StatsActivity : AppCompatActivity() {
    private var binding:ActivityStatsBinding?=null
    private val viewModel: StatsViewModel by viewModels {
        StatsViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding?.toolbarStats)

        if(supportActionBar!=null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarStats?.setNavigationOnClickListener {
            onBackPressed()
        }

        observeStats()

        binding?.btnResetStats?.setOnClickListener {
            viewModel.resetStats()
        }

    }

    private fun observeStats() {
        viewModel.frameCount.observe(this) { frames ->
            binding?.tvFramesSent?.text = "Frames sent: $frames"
        }

        viewModel.bytesSent.observe(this) { bytes ->
            binding?.tvBytesSent?.text = "Bytes sent: $bytes"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}