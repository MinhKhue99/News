package com.minhkhue.news.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.minhkhue.news.databinding.FragmentArticleBinding
import com.minhkhue.news.ui.NewsActivity
import com.minhkhue.news.ui.viewmodel.NewsViewModel

class ArticleFragment : Fragment() {
	private var _binding: FragmentArticleBinding? = null
	private val binding get() = _binding!!
	private lateinit var viewMode: NewsViewModel
	private val args: ArticleFragmentArgs by navArgs()
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentArticleBinding.inflate(inflater, container, false)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewMode = (activity as NewsActivity).viewModel
		val article = args.article
		binding.webView.apply {
			webViewClient = WebViewClient()
			loadUrl(article.url!!)
		}
		binding.fab.setOnClickListener {
			viewMode.saveArticle(article)
			Snackbar.make(view, "Save Article Successfully", Snackbar.LENGTH_SHORT).show()
		}
	}
	
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}