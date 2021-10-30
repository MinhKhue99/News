package com.minhkhue.news.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.minhkhue.news.R
import com.minhkhue.news.adapter.NewsAdapter
import com.minhkhue.news.databinding.FragmentSavedNewsBinding
import com.minhkhue.news.ui.NewsActivity
import com.minhkhue.news.ui.viewmodel.NewsViewModel

class SavedNewsFragment : Fragment() {
	private var _binding: FragmentSavedNewsBinding? = null
	private val binding get() = _binding!!
	private lateinit var viewMode: NewsViewModel
	private lateinit var newsAdapter: NewsAdapter
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewMode = (activity as NewsActivity).viewModel
		setupRecyclerView()
		newsAdapter.setOnItemClickListener {
			val bundle = Bundle().apply {
				putSerializable("article", it)
			}
			findNavController().navigate(
				R.id.action_savedNewsFragment_to_articleFragment,
				bundle
			)
		}
		
		val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
			ItemTouchHelper.UP or ItemTouchHelper.DOWN,
			ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
		) {
			override fun onMove(
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				target: RecyclerView.ViewHolder
			): Boolean {
				return true
			}
			
			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				val position = viewHolder.adapterPosition
				val article = newsAdapter.differ.currentList[position]
				viewMode.deleteArticle(article)
				Snackbar.make(view, "Delete Article Successfully", Snackbar.LENGTH_LONG).apply {
					setAction("Undo") {
						viewMode.saveArticle(article)
					}
					show()
				}
			}
		}
		ItemTouchHelper(itemTouchHelperCallback).apply {
			attachToRecyclerView(binding.rvSavedNews)
		}
		viewMode.getSavedArticle().observe(viewLifecycleOwner, { articles ->
			newsAdapter.differ.submitList(articles)
			
		})
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
	
	private fun setupRecyclerView() {
		newsAdapter = NewsAdapter()
		binding.rvSavedNews.apply {
			adapter = newsAdapter
			layoutManager = LinearLayoutManager(activity)
		}
	}
}