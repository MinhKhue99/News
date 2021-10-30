package com.minhkhue.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minhkhue.news.databinding.ItemArticlePreviewBinding
import com.minhkhue.news.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
	inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding) :
		RecyclerView.ViewHolder(binding.root)
	
	private val differCallback = object : DiffUtil.ItemCallback<Article>() {
		override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
			return oldItem.url == newItem.url
		}
		
		override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
			return oldItem == newItem
		}
	}
	val differ = AsyncListDiffer(this, differCallback)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
		val binding = ItemArticlePreviewBinding.inflate(
			LayoutInflater.from(parent.context),
			parent, false
		)
		return ArticleViewHolder(binding)
	}
	
	override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
		val article = differ.currentList[position]
		with(holder) {
			Glide.with(holder.itemView.context)
				.load(article.urlToImage).into(binding.ivArticleImage)
			binding.tvSource.text = article.source?.name
			binding.tvTitle.text = article.title
			binding.tvDescription.text = article.description
			binding.tvPublishedAt.text = article.publishedAt
			holder.itemView.setOnClickListener {
				onItemClickListener?.let { it(article) }
			}
		}
		
	}
	
	override fun getItemCount(): Int {
		return differ.currentList.size
	}
	
	private var onItemClickListener: ((Article) -> Unit)? = null
	
	fun setOnItemClickListener(listener: (Article) -> Unit) {
		onItemClickListener = listener
	}
}