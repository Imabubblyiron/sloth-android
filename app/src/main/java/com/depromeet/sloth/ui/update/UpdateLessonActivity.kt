package com.depromeet.sloth.ui.update

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.room.Update
import com.depromeet.sloth.R
import com.depromeet.sloth.data.db.PreferenceManager
import com.depromeet.sloth.data.db.model.LessonModel
import com.depromeet.sloth.data.db.model.UpdateLessonModel
import com.depromeet.sloth.data.network.update.UpdateLessonState
import com.depromeet.sloth.databinding.ActivityUpdateLessonBinding
import com.depromeet.sloth.ui.base.BaseActivity
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat

class UpdateLessonActivity : BaseActivity<UpdateLessonViewModel, ActivityUpdateLessonBinding>() {

    override val viewModel: UpdateLessonViewModel = UpdateLessonViewModel()

    override fun getViewBinding(): ActivityUpdateLessonBinding
        = ActivityUpdateLessonBinding.inflate(layoutInflater)

    private val pm = PreferenceManager(this)

    private var lessonModel:LessonModel? = null

    lateinit var accessToken: String

    lateinit var siteArraySize: Number

    lateinit var lessonId: String

    companion object {
        fun newIntent(activity: Activity,lessonId: String, lessonModel: LessonModel) = Intent(activity, UpdateLessonActivity::class.java).apply {
            putExtra(LESSON_ID, lessonId)
            putExtra(LESSON_MODEL, lessonModel)
        }

        private const val LESSON_ID = "lessonId"
        private const val LESSON_MODEL = "lessonModel"

    }

    override fun initViews() = with(binding) {
        tbUpdateLesson.setNavigationOnClickListener { finish() }

        accessToken = pm.getAccessToken().toString()

        /*intent.apply {
            lessonId = getStringExtra(LESSON_ID).toString()
            lessonModel = getParcelableExtra(LESSON_MODEL)
        }*/

        /*test*/
        lessonId = "74"


        lessonModel = LessonModel(
            alertDays = null,
            categoryId = 11,
            endDate = "2021-11-02",
            lessonName=  "ㅇㅇㅇ",
            message = "아자아자 화이팅",
            price = 32000,
            siteId = 2,
            startDate = "2021-10-31",
            totalNumber = 32
        )

        focusInputForm(etUpdateLessonName, btnUpdateLesson)
        focusInputForm(etUpdateLessonCount, btnUpdateLesson)
        focusSpinnerForm(spnUpdateLessonCategory, btnUpdateLesson)
        focusSpinnerForm(spnUpdateLessonSite, btnUpdateLesson)

        initLessonInfo(lessonModel)

        siteArraySize = resources.getStringArray(R.array.site_array).size - 1

        btnUpdateLesson.setOnClickListener {
            mainScope {
                val updateLessonModel = UpdateLessonModel(
                    categoryId = spnUpdateLessonCategory.selectedItemPosition + siteArraySize as Int,
                    lessonName = etUpdateLessonName.text.toString(),
                    siteId = spnUpdateLessonSite.selectedItemPosition,
                    totalNumber = etUpdateLessonCount.text.toString().toInt()
                )

                viewModel.updateLessonInfo(accessToken = accessToken, lessonId = lessonId, updateLessonModel = updateLessonModel).let {
                    when(it) {
                        is UpdateLessonState.Success -> {
                            Log.d("Update Success", "${it.data}")
                        }

                        is UpdateLessonState.Error -> {
                            Log.d("Update Error", "${it.exception}")
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initLessonInfo(lessonModel: LessonModel?) = with(binding) {
        etUpdateLessonName.setText(lessonModel!!.lessonName)
        etUpdateLessonCount.setText(lessonModel.totalNumber.toString())
        spnUpdateLessonCategory.setSelection(lessonModel.categoryId)
        spnUpdateLessonSite.setSelection(lessonModel.siteId)
        tvUpdateEndLessonDate.text = lessonModel.endDate
        tvUpdateStartLessonDate.text = lessonModel.startDate

        val df = DecimalFormat("#,###")
        val changedPriceFormat = df.format(lessonModel.price)

        tvUpdateLessonPriceInfo.text = "${changedPriceFormat}원"
        tvUpdateLessonMessageInfo.text = lessonModel.message.toString()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun unlockButton(button: Button) {
        button.isEnabled = true
        button.setBackgroundColor(
            resources.getColor(
                R.color.primary_500,
                theme
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun lockButton(button: Button) {
        button.isEnabled = false
        button.setBackgroundColor(
            resources.getColor(
                R.color.gray_300,
                theme
            )
        )
    }

    private fun focusInputForm(editText: EditText, button: Button) {
        editText.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun beforeTextChanged(charSequence: CharSequence?, i1: Int, i2: Int, i3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, i1: Int, i2: Int, i3: Int) {}

            @RequiresApi(Build.VERSION_CODES.M)
            override fun afterTextChanged(editable: Editable?) {
                if (editable.isNullOrEmpty()) {
                    lockButton(button)
                } else {
                    unlockButton(button)
                }
            }
        })
    }

    private fun focusSpinnerForm(spinner: Spinner, button: Button) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val spinnerId = spinner.selectedItemPosition
                if(spinnerId == 0) {
                    lockButton(button)
                }
                else {
                    unlockButton(button)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                unlockButton(button)
            }
        }
    }
}