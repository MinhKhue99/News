package com.minhkhue.news.repository

import com.minhkhue.news.api.RetrofitInstance
import com.minhkhue.news.database.ArticleDatabase
import com.minhkhue.news.model.Article

class NewsRepository(private val db: ArticleDatabase) {
	
	suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
		RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
	
	suspend fun searchNews(searchQuery:String,pageNumber: Int) =
		RetrofitInstance.api.searchNews(searchQuery, pageNumber)
	
	suspend fun upsertArticle(article: Article) = db.getArticleDao().upsert(article)
	
	fun getSavedArticle() = db.getArticleDao().getAllArticles()
	
	suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}