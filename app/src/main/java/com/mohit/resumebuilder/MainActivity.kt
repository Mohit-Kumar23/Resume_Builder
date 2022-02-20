package com.mohit.resumebuilder

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.isNotEmpty
import com.google.android.material.textfield.TextInputEditText
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.education_dialog_layout.*
import kotlinx.android.synthetic.main.education_dialog_layout.view.*
import kotlinx.android.synthetic.main.past_ach_dialog_layout.view.*
import kotlinx.android.synthetic.main.skill_dialog_layout.view.*

class MainActivity : AppCompatActivity() {

    var degreeVal: String? = null
    var streamVal: String? = null
    var collegeVal: String? = null
    var batchVal: String? = null
    var cgpaVal: String? = null
    var eduInfo: Spanned? = null

    var skill_spinner_1: String? = null
    var skill_spinner_2: String? = null
    var skill_et_1: String? = null
    var skill_et_2: String? = null
    var skillInfo : Spanned? = null

    var pastAch_spinner:String? = null
    var pastAch_val:String? = null
    var pastAchInfo:Spanned? = null

    var fullName:String? = null
    var email:String? = null
    var contactNo:String? = null
    var currLoc:String? = null
    var portfolio:String? = null
    var personalDesc:String? = null

    var userImgUri:Uri?=null

    private lateinit var cropActivityResultLauncher : ActivityResultLauncher<Any?>

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>()
    {
        override fun createIntent(context: Context, input: Any?): Intent
        {
            return CropImage.activity()
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1,1)
                    .getIntent(this@MainActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri?
        {
            return CropImage.getActivityResult(intent)?.uri
        }

    }


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ib_edu.setOnClickListener {
            val eduDialog = LayoutInflater.from(this).inflate(R.layout.education_dialog_layout, null);
            val mEduDialogBuilder = AlertDialog.Builder(this)
                    .setView(eduDialog)
                    .setTitle("Education Details")
            val eduAlertDialog = mEduDialogBuilder.show()

            eduDialog.btn_edu_dialog_done.setOnClickListener {
                degreeVal = eduDialog.et_dialog_edu_degree.text.toString()
                streamVal = eduDialog.et_dialog_edu_stream.text.toString()
                collegeVal = eduDialog.et_dialog_edu_college.text.toString()
                batchVal = eduDialog.et_dialog_edu_batch.text.toString()
                cgpaVal = eduDialog.et_dialog_edu_cgpa.text.toString()

                eduInfo = HtmlCompat.fromHtml("<b>${degreeVal} - ${streamVal} </b>" +
                        "<br><b>College : </b>${collegeVal}</br>" +
                        "<br><b>Batch : </b>${batchVal}</br>" +
                        "<br><b>CGPA : </b>${cgpaVal}/10.00</br>", Html.FROM_HTML_MODE_LEGACY)

                if (degreeVal.isNullOrEmpty() || streamVal.isNullOrEmpty() || collegeVal.isNullOrEmpty() || batchVal.isNullOrEmpty() || cgpaVal.isNullOrEmpty()) {
                        Toast.makeText(this,"Fill all Details",Toast.LENGTH_LONG).show()
                }
                else {
                    tv_ma_edu.text = eduInfo
                    tv_ma_edu.visibility = View.VISIBLE
                    eduAlertDialog.dismiss()
                }

            }

            eduDialog.btn_edu_dialog_cancel.setOnClickListener {
                eduAlertDialog.dismiss()
            }
        }

        ib_skill.setOnClickListener {
            val skillDialog = LayoutInflater.from(this).inflate(R.layout.skill_dialog_layout, null)
            val mSkilldialogBuilder = AlertDialog.Builder(this)
                    .setView(skillDialog)
                    .setTitle("Skills Details")

            val skillAlertDialog = mSkilldialogBuilder.show()

            skillDialog.btn_skill_dialog_done.setOnClickListener {
                skill_spinner_1 = skillDialog.spinner_dialog_skill_1.selectedItem.toString()
                skill_spinner_2 = skillDialog.spinner_dialog_skill_2.selectedItem.toString()
                skill_et_1 = skillDialog.et_dialog_skill_1.text.toString()
                skill_et_2 = skillDialog.et_dialog_skill_2.text.toString()


                skill_et_1 = "<br>"+skill_et_1?.replace(",","</br><br>")+"</br>"

                skill_et_2 = "<br>"+skill_et_2?.replace(",","</br><br>  ")+"</br>"

                skillInfo = HtmlCompat.fromHtml("<b>${skill_spinner_1}</b>" +
                        "${skill_et_1}" + "<br></br>" +
                        "<br><b>${skill_spinner_2}</b></br>" +
                        "${skill_et_2}",Html.FROM_HTML_MODE_LEGACY)


                if(skill_et_1.isNullOrEmpty() || skill_et_2.isNullOrEmpty())
                {
                    Toast.makeText(this,"Fill all Details",Toast.LENGTH_LONG).show()
                }
                else
                {
                    tv_ma_skills.text = skillInfo
                    tv_ma_skills.visibility = View.VISIBLE
                    skillAlertDialog.dismiss()
                }


            }

            skillDialog.btn_skill_dialog_cancel.setOnClickListener {
                skillAlertDialog.dismiss()
            }
        }

        ib_pastAch.setOnClickListener {
            val pastAchDialog = LayoutInflater.from(this).inflate(R.layout.past_ach_dialog_layout, null)
            val mPastAchDialogBuilder = AlertDialog.Builder(this)
                    .setView(pastAchDialog)
                    .setTitle("Past Achievement Details")

            val pastAchAlertDialog = mPastAchDialogBuilder.show()

            pastAchDialog.btn_pastAch_dialog_done.setOnClickListener {
                pastAch_spinner = pastAchDialog.spinner_dialog_pastAch.selectedItem.toString()
                pastAch_val = pastAchDialog.et_dialog_pastAch.text.toString()

                pastAch_val = "<br>"+pastAch_val?.replace(",","</br><br>")+"</br>"

                pastAchInfo = HtmlCompat.fromHtml("<b>${pastAch_spinner}</b>" +
                        "<br>${pastAch_val}</br>",Html.FROM_HTML_MODE_LEGACY)

                if(pastAch_val.isNullOrEmpty())
                {
                    Toast.makeText(this,"Fill all Details",Toast.LENGTH_LONG).show()
                }
                else
                {
                    tv_ma_pastAch.text = pastAchInfo
                    tv_ma_pastAch.visibility = View.VISIBLE
                    pastAchAlertDialog.dismiss()
                }

            }

            pastAchDialog.btn_pastAch_dialog_cancel.setOnClickListener {
                pastAchAlertDialog.dismiss()
            }
        }


        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it?.let { uri ->
                iv_ma_userImg.setImageURI(uri)
                userImgUri = uri
            }
        }

        cv_ma_user.setOnClickListener {

            cropActivityResultLauncher.launch(null)

        }

        btn_ma_sumbit.setOnClickListener {
            if(tiet_ma_fullName.text.isNullOrEmpty() || tiet_ma_contactNo.text.isNullOrEmpty() || tiet_ma_email.text.isNullOrEmpty()
                || tiet_ma_currentLoc.text.isNullOrEmpty() || tiet_ma_portfolio.text.isNullOrEmpty() || tiet_ma_personalDesc.text.isNullOrEmpty()
                || tv_ma_edu.text.isNullOrEmpty() || tv_ma_skills.text.isNullOrEmpty() || tv_ma_pastAch.text.isNullOrEmpty() || userImgUri == null)
            {
                Toast.makeText(this,"Fill All Detials",Toast.LENGTH_LONG).show()
            }
            else
            {

                fullName = tiet_ma_fullName.text.toString()
                email = tiet_ma_email.text.toString()
                contactNo = tiet_ma_contactNo.text.toString()
                currLoc = tiet_ma_currentLoc.text.toString()
                portfolio = tiet_ma_portfolio.text.toString()
                personalDesc = tiet_ma_personalDesc.text.toString()

                var intent = Intent(this@MainActivity,Pdf_Screen::class.java)

                intent.putExtra("fullName",fullName)
                intent.putExtra("email",email)
                intent.putExtra("contactNo",contactNo)
                intent.putExtra("currLoc",currLoc)
                intent.putExtra("portfolio",portfolio)
                intent.putExtra("personalDesc",personalDesc)
                intent.putExtra("eduDetails",eduInfo)
                intent.putExtra("skillDetails",skillInfo)
                intent.putExtra("pastAchDetails",pastAchInfo)
                intent.putExtra("userImgUri",userImgUri)

                startActivity(intent)

            }
        }



    }


}


