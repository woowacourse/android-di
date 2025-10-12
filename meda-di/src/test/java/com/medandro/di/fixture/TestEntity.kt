package com.medandro.di.fixture

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_table")
data class TestEntity(
    @PrimaryKey val id: Int,
    val name: String,
)
