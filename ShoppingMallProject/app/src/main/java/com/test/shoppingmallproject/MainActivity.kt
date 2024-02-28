package com.test.shoppingmallproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.test.shoppingmallproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val adapter by lazy { PagingListAdapter()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerView.adapter = adapter
        }
        observeViewModel()
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest {
                if (it != null) {
                    adapter.submitData(lifecycle,it)
                }
            }
        }
    }
}