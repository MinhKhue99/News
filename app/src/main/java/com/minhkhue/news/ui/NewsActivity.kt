package com.minhkhue.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.minhkhue.news.R
import com.minhkhue.news.database.ArticleDatabase
import com.minhkhue.news.databinding.ActivityNewsBinding
import com.minhkhue.news.repository.NewsRepository
import com.minhkhue.news.ui.viewmodel.NewsViewModel
import com.minhkhue.news.ui.viewmodel.NewsViewModelProviderFactory


class NewsActivity : AppCompatActivity() {
	private lateinit var binding: ActivityNewsBinding
	lateinit var viewModel: NewsViewModel
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityNewsBinding.inflate(layoutInflater)
		setContentView(binding.root)
		val newsRepository = NewsRepository(ArticleDatabase(this))
		val viewModelProviderFactory  = NewsViewModelProviderFactory(application,newsRepository)
		viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
		val navController =
			(supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment).navController
		binding.bottomNavigationView.setupWithNavController(navController)
	}
}