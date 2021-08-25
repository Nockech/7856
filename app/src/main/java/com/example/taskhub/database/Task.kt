package com.example.taskhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.jetbrains.annotations.NotNull

@TypeConverters(TaskListConverter::class)
@Entity(tableName = "task_packs_data_table")
data class TaskPacks(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_pack_id")
    var id : Int,

    @ColumnInfo(name = "task_pack_date")
    var date: String,

    @TypeConverters(TaskListConverter::class)
    @ColumnInfo(name = "task_pack_tasks")
    var tasks: List<Task>,
)


@TypeConverters(StepListConverter::class)
@Entity(tableName = "task_data_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var id : Int,

    @ColumnInfo(name = "task_title")
    var title : String,

    @ColumnInfo(name = "task_date")
    var date : String,

    @TypeConverters(StepListConverter::class)
    @NotNull
    @ColumnInfo(name = "task_steps")
    var steps: List<Step>,

    @ColumnInfo(name = "task_time")
    var time: String = ""
    )


data class Step(
    var text: String,
    var isCompleted: Boolean = false)
