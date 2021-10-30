package com.minhkhue.news.database

import androidx.room.TypeConverter
import com.minhkhue.news.model.Source

class Converter {
	@TypeConverter
	fun fromSource(source: Source): String {
		return source.name
	}
	
	@TypeConverter
	fun toSource(name:String): Source {
		return Source(name,name)
	}
}