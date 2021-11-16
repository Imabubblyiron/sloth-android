package com.depromeet.sloth.ui.home

import android.animation.ObjectAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.addListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.sloth.R
import com.depromeet.sloth.data.network.home.LessonResponse
import kotlin.math.ceil

class ClassLessonAdapter(
    private val bodyType: BodyType
) : ListAdapter<LessonResponse, ClassLessonAdapter.ClassLessonViewHolder>(
    ClassLessonDiffCallback
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassLessonViewHolder {
        val view = when (bodyType) {
            BodyType.DOING -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_class_lesson_doing, parent, false)
            BodyType.PLANNING -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_class_lesson_planning, parent, false)
            BodyType.PASSED -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_class_lesson_passed, parent, false)
        }

        return ClassLessonViewHolder(bodyType, view)
    }

    override fun onBindViewHolder(holder: ClassLessonViewHolder, position: Int) {
        val lesson = getItem(position)
        holder.onBind(lesson)
    }

    override fun onCurrentListChanged(
        previousList: List<LessonResponse?>,
        currentList: List<LessonResponse?>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        notifyDataSetChanged()
    }

    inner class ClassLessonViewHolder(
        private val bodyType: BodyType,
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val classLessonCategory = itemView.findViewById<TextView>(R.id.tv_class_lesson_category)
        private val classLessonName = itemView.findViewById<TextView>(R.id.tv_class_lesson_name)
        private val classLessonPrice = itemView.findViewById<TextView>(R.id.tv_class_lesson_price)
        private val classLessonLayout = itemView.findViewById<ConstraintLayout>(R.id.cl_class_lesson_layoutc)
        private val classLessonGoalLine = itemView.findViewById<View>(R.id.vw_class_lesson_goal_line)
        private val classLessonGoal = itemView.findViewById<TextView>(R.id.tv_class_lesson_goal)
        private val classLessonGoalGroup = itemView.findViewById<ConstraintLayout>(R.id.cl_class_goal_group)
        private val classLessonBar = itemView.findViewById<ProgressBar>(R.id.pb_class_lesson_bar)
        private val classLessonPercent = itemView.findViewById<TextView>(R.id.tv_class_lesson_percent)
        private val classLessonCurrentNumber = itemView.findViewById<TextView>(R.id.tv_class_lesson_current_number)
        private val classLessonTotalNumber = itemView.findViewById<TextView>(R.id.tv_class_lesson_total_number)
        private val classLessonPlanningDate = itemView.findViewById<TextView>(R.id.tv_class_lesson_planning_date)
        private val classLessonPassedCount = itemView.findViewById<TextView>(R.id.tv_class_lesson_total_count)

        fun onBind(lesson: LessonResponse) {
            classLessonCategory.text = lesson.categoryName
            classLessonName.text = lesson.lessonName
            classLessonPrice.text = lesson.price.toString()

            when (bodyType) {
                BodyType.DOING -> {
                    val progressRate = lesson.currentProgressRate / lesson.totalNumber.toFloat()
                    classLessonCurrentNumber.text = lesson.currentProgressRate.toString()
                    classLessonTotalNumber.text = lesson.totalNumber.toString()
                    classLessonPercent.text = ceil((100 - (progressRate * 100)).toDouble()).toInt().toString()
                    classLessonGoal.text = (progressRate * 100).toInt().toString()

                    val set = ConstraintSet()
                    set.clone(classLessonLayout)
                    set.setHorizontalBias(classLessonGoalLine.id, progressRate)
                    set.applyTo(classLessonLayout)
                    classLessonGoalLine.bringToFront()

                    val animation = ObjectAnimator.ofInt(
                        classLessonBar,
                        "progress",
                        classLessonBar.progress,
                        (progressRate * 10000).toInt()
                    )
                    Log.e("test2", (ceil(progressRate.toDouble()) * 10000).toInt().toString())
                    animation.apply {
                        addListener(
                            onEnd = {
                                classLessonGoalGroup.visibility = View.VISIBLE
                                classLessonGoalGroup.bringToFront()
                            }
                        )
                        duration = 500
                        interpolator = DecelerateInterpolator()
                    }.start()
                }

                BodyType.PLANNING -> {
                    classLessonPlanningDate.text = lesson.startDate.split(" ")[0]
                }

                BodyType.PASSED -> {
                    classLessonPassedCount.text = lesson.totalNumber.toString()
                }
            }
        }
    }

    enum class BodyType {
        DOING,
        PLANNING,
        PASSED
    }
}

object ClassLessonDiffCallback : DiffUtil.ItemCallback<LessonResponse>() {
    override fun areItemsTheSame(
        oldItem: LessonResponse,
        newItem: LessonResponse
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: LessonResponse,
        newItem: LessonResponse
    ): Boolean {
        return oldItem.categoryName == newItem.categoryName
    }
}