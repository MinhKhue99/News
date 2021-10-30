package com.minhkhue.news.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minhkhue.news.R
import com.minhkhue.news.adapter.NewsAdapter
import com.minhkhue.news.databinding.FragmentBreakingNewsBinding
import com.minhkhue.news.ui.NewsActivity
import com.minhkhue.news.ui.viewmodel.NewsViewModel
import com.minhkhue.news.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.minhkhue.news.utils.Resource

class BreakingNewsFragment : Fragment() {
	private var _binding: FragmentBreakingNewsBinding? = null
	private val binding get() = _binding!!
	private lateinit var newsAdapter: NewsAdapter
	private lateinit var viewModel: NewsViewModel
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = (activity as NewsActivity).viewModel
		setupRecyclerView()
		newsAdapter.setOnItemClickListener {
			val bundle = Bundle().apply {
				putSerializable("article", it)
			}
			findNavController().navigate(
				R.id.action_breakingNewsFragment_to_articleFragment,
				bundle
			)
		}
		viewModel.breakingNews.observe(viewLifecycleOwner, { response ->
			when (response) {
				is Resource.Success -> {
					hideProgressbar()
					response.data?.let { newsResponse ->
						newsAdapter.differ.submitList(newsResponse.articles.toList())
						val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE+2
						isLastPage = viewModel.breakingNewsPage==totalPages
						if (isLastPage){
							binding.rvBreakingNews.setPadding(0,0,0,0)
						}
					}
				}
				is Resource.Error -> {
					hideProgressbar()
					response.message?.let { message ->
						Toast.makeText(requireContext(),"An error: $message",Toast.LENGTH_SHORT).show()
					}
				}
				is Resource.Loading -> {
					showProgressbar()
				}
			}
			
		})
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	private fun setupRecyclerView() {
		newsAdapter = NewsAdapter()
		binding.rvBreakingNews.apply {
			adapter = newsAdapter
			layoutManager = LinearLayoutManager(activity)
			addOnScrollListener(this@BreakingNewsFragment.scrollListener)
		}
	}
	
	private fun hideProgressbar() {
		binding.paginationProgressBar.visibility = View.INVISIBLE
		isLoading = false
	}
	
	private fun showProgressbar() {
		binding.paginationProgressBar.visibility = View.VISIBLE
		isLoading = true
	}
	
	var isLoading = false
	var isScrolling = false
	var isLastPage = false
	private val scrollListener = object : RecyclerView.OnScrollListener() {
		override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
			super.onScrolled(recyclerView, dx, dy)
			val layoutManager = recyclerView.layoutManager as LinearLayoutManager
			val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
			val visibleItemCount = layoutManager.childCount
			val totalCount = layoutManager.itemCount
			
			val isNotLoadingAndNotlastPage = !isLoading && !isLastPage
			val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalCount
			val isNotAtBeginning = firstVisibleItemPosition >= 0
			val isTotalMoreThanVisible = totalCount >= QUERY_PAGE_SIZE
			val shouldPaginate =
				isNotLoadingAndNotlastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
			if (shouldPaginate){
				viewModel.getBreakingNews("us")
				isScrolling = false
			}
		}
		
		override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
			super.onScrollStateChanged(recyclerView, newState)
			if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				isScrolling = true
			}
		}
	}
}