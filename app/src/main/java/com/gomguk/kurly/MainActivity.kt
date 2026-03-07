package com.gomguk.kurly

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gomguk.kurly.databinding.ActivityMainBinding
import com.gomguk.kurly.ui.main.MainAdapter
import com.gomguk.kurly.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSwipeRefreshLayout()

        mainViewModel.sectionInfoList.observe(this) { items ->
            mainAdapter.updateItems(items)
        }

        mainViewModel.isRefreshing.observe(this) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        mainAdapter =
            MainAdapter(emptyList(), mainViewModel::loadSectionItems, mainViewModel::toggleFavorite)
        binding.recyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) { // 스크롤을 아래로 내릴 때만 체크
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                        val totalItemCount = layoutManager.itemCount

                        // lastVisibleItemPosition은 0부터 시작하므로, totalItemCount - 1과 비교해야 끝에 도달한 것입니다.
                        // 조금 더 미리 로드하기 위해 totalItemCount - 2 정도로 설정할 수도 있습니다.
                        if (lastVisibleItemPosition >= totalItemCount - 1) {
                            mainViewModel.loadItems()
                        }
                    }
                }
            })
        }
    }
}
