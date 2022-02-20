package com.mohit.resumebuilder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeResource
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
/*import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory

import com.itextpdf.kernel.*;
import com.itextpdf.kernel.color.Color
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.annot.PdfAnnotation
import com.itextpdf.kernel.pdf.annot.PdfCircleAnnotation
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace
import com.itextpdf.kernel.pdf.layer.PdfLayer
import com.itextpdf.layout.*;*/
import com.itextpdf.text.Image;
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import kotlinx.android.synthetic.main.activity_pdf__screen.*
import java.io.*


class

Pdf_Screen : AppCompatActivity() {

    lateinit var fullName: String
    lateinit var email: String
    lateinit var contactNo: String
    lateinit var currLoc: String
    lateinit var portfolio: String
    lateinit var personalDesc: String
    lateinit var eduInfo: Spanned
    lateinit var skillInfo: Spanned
    lateinit var pastAchInfo: Spanned
    lateinit var userUri: Uri
    var userImgWidth: Float = 120f
    var userImgHeight: Float = 120f
    var vectorImgWidth: Float = 20f
    var vectorImgHeight: Float = 20f


    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf__screen)

        var intent_val = intent
        userUri = intent_val.extras?.get("userImgUri") as Uri
        fullName = intent_val.getStringExtra("fullName")!!
        email = intent_val.getStringExtra("email")!!
        contactNo = intent_val.getStringExtra("contactNo")!!
        currLoc = intent_val.getStringExtra("currLoc")!!
        portfolio = intent_val.getStringExtra("portfolio")!!
        eduInfo = intent_val.extras?.get("eduDetails") as Spanned
        personalDesc = intent_val.getStringExtra("personalDesc")!!
        pastAchInfo = intent_val.extras?.get("pastAchDetails") as Spanned
        skillInfo = intent_val.extras?.get("skillDetails") as Spanned


        try {
            createPdf()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun createPdf() {
        var pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
        var folder: File = File(pdfPath, "Resume Builder")
        if (!folder.exists()) {
            folder.mkdir()
            Log.i("folder create", "folder created")
        }

        val simpleDateTime: String = System.currentTimeMillis().toString()
        var fileName = File(folder.absolutePath + "/${simpleDateTime}.pdf")
        Log.i("path", fileName.absolutePath)

        var outputStream: OutputStream = FileOutputStream(fileName!!)
        var document = Document()
        var writer = PdfWriter.getInstance(document, outputStream)


        document.open()

        var contentByte: PdfContentByte = writer.directContent
        var ct1: ColumnText = ColumnText(contentByte)
        var headingFont: Font = Font(Font.FontFamily.TIMES_ROMAN)
        var textFont: Font = Font(Font.FontFamily.TIMES_ROMAN)


        headingFont.setStyle("bold")
        headingFont.size = 16F
        textFont.size = 12F


        contentByte.setLineWidth(3.0f)
        contentByte.moveTo(100f, 780f)
        contentByte.lineTo(100f, 30f)
        contentByte.setRGBColorStroke(255, 165, 0)
        contentByte.stroke()


        var stream: ByteArrayOutputStream = ByteArrayOutputStream()
        var bitmap: Bitmap = BitmapFactory.decodeResource(baseContext.resources, R.drawable.profile_hold_bg)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        var streamU: ByteArrayOutputStream = ByteArrayOutputStream()
        var bitmapUser: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, userUri)
        bitmapUser.compress(Bitmap.CompressFormat.JPEG, 100, streamU)

        var streamEmail: ByteArrayOutputStream = ByteArrayOutputStream()
        var bitmapEmail: Bitmap? = this.getBitmapFromVectorDrawable(R.drawable.ic_baseline_email_24)
        bitmapEmail?.compress(Bitmap.CompressFormat.PNG, 100, streamEmail)


        var streamContact: ByteArrayOutputStream = ByteArrayOutputStream()
        var bitmapContact: Bitmap = this.getBitmapFromVectorDrawable(R.drawable.ic_baseline_phone_24)
        bitmapContact.compress(Bitmap.CompressFormat.PNG, 100, streamContact)

        var streamPortfolio: ByteArrayOutputStream = ByteArrayOutputStream()
        var bitmapPortfolio: Bitmap = this.getBitmapFromVectorDrawable(R.drawable.ic_baseline_work_24)
        bitmapPortfolio.compress(Bitmap.CompressFormat.PNG, 100, streamPortfolio)

        var streamLoc: ByteArrayOutputStream = ByteArrayOutputStream()
        var bitmapLoc: Bitmap = this.getBitmapFromVectorDrawable(R.drawable.ic_baseline_location_on_24)
        bitmapLoc.compress(Bitmap.CompressFormat.PNG, 100, streamLoc)


        lateinit var profileImgObj: Image
        lateinit var userImg: Image
        lateinit var emailImg: Image
        lateinit var contactImg: Image
        lateinit var portfolioImg: Image
        lateinit var locImg: Image


        try {
            profileImgObj = Image.getInstance(stream.toByteArray())
            userImg = Image.getInstance(streamU.toByteArray())
            emailImg = Image.getInstance(streamEmail.toByteArray())
            contactImg = Image.getInstance(streamContact.toByteArray())
            portfolioImg = Image.getInstance(streamPortfolio.toByteArray())
            locImg = Image.getInstance(streamLoc.toByteArray())
        } catch (e: BadElementException) {
            Log.i("Image Exception", "${e}")
        } catch (e: IOException) {
            Log.i("Image IO Exception", "${e}")
        }

        profileImgObj.scaleToFit(160f, 600f)
        profileImgObj.setAbsolutePosition(430f, 600f)
        document.add(profileImgObj)


        var template: PdfTemplate = contentByte.createTemplate(userImgWidth, userImgHeight)
        template.ellipse(0f, 0f, userImgWidth, userImgHeight)
        template.clip()
        template.newPath()
        template.addImage(userImg, userImgHeight, 0f, 0f, userImgHeight, 0f, 0f)
        var clippedUserImg = Image.getInstance(template)

        clippedUserImg.setAbsolutePosition(447f, 626f)

        document.add(clippedUserImg);


        var fullPara: Paragraph = Paragraph(fullName)
        fullPara.font = textFont
        fullPara.alignment = Element.ALIGN_CENTER
        ct1.setSimpleColumn(427f, 560f, 580f, 620f)
        ct1.addElement(fullPara)
        ct1.go()

        var emailPara: Paragraph = Paragraph(email)
        emailPara.font = textFont
        emailPara.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(455f, 548f, 580f, 588f)
        ct1.addElement(emailPara)
        ct1.go()

        emailImg.scaleToFit(vectorImgWidth, vectorImgHeight)
        emailImg.setAbsolutePosition(432f, 565f)
        document.add(emailImg)

        var contactNoPara: Paragraph = Paragraph(contactNo)
        contactNoPara.font = textFont
        contactNoPara.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(458f, 518f, 580f, 538f)
        ct1.addElement(contactNoPara)
        ct1.go()

        contactImg.scaleToFit(vectorImgWidth, vectorImgHeight)
        contactImg.setAbsolutePosition(432f, 515f)
        document.add(contactImg)

        var portfolioPara: Paragraph = Paragraph(portfolio)
        portfolioPara.font = textFont
        portfolioPara.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(458f, 458f, 580f, 498f)
        ct1.addElement(portfolioPara)
        ct1.go()

        portfolioImg.scaleToFit(vectorImgWidth, vectorImgHeight)
        portfolioImg.setAbsolutePosition(432f, 475f)
        document.add(portfolioImg)

        var locPara: Paragraph = Paragraph(currLoc)
        locPara.font = textFont
        locPara.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(458f, 428f, 580f, 448f)
        ct1.addElement(locPara)
        ct1.go()

        locImg.scaleToFit(vectorImgWidth, vectorImgHeight)
        locImg.setAbsolutePosition(432f, 425f)
        document.add(locImg)


        var eduHeading: Paragraph = Paragraph("EDUCATION")
        eduHeading.font = headingFont
        eduHeading.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(120f, 750f, 220f, 780f)
        ct1.addElement(eduHeading)
        ct1.go()

        var eduVal = Paragraph(eduInfo.toString())
        eduVal.font = textFont
        eduVal.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(120f, 600f, 450f, 740f)
        ct1.addElement(eduVal)
        ct1.go()

        var skillHeading: Paragraph = Paragraph("SKILLS")
        skillHeading.font = headingFont
        skillHeading.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(20f, 750f, 95f, 780f)
        ct1.addElement(skillHeading)
        ct1.go()

        var skillVal: Paragraph = Paragraph(skillInfo.toString())
        skillVal.font = textFont
        skillVal.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(20f, 40f, 95f, 740f)
        ct1.addElement(skillVal)
        ct1.go()

        var achievementHeading: Paragraph = Paragraph("ACHIEVEMENTS")
        achievementHeading.font = headingFont
        achievementHeading.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(120f, 580f, 300f, 610f)
        ct1.addElement(achievementHeading)
        ct1.go()

        var achVal: Paragraph = Paragraph(pastAchInfo.toString())
        achVal.font = textFont
        achVal.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(120f, 430f, 440f, 570f)
        ct1.addElement(achVal)
        ct1.go()

        var personalDescHeading: Paragraph = Paragraph("PERSONAL DESCRIPTION")
        personalDescHeading.font = headingFont
        personalDescHeading.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(120f, 380f, 350f, 420f)
        ct1.addElement(personalDescHeading)
        ct1.go()

        var personalDescVal: Paragraph = Paragraph(personalDesc)
        personalDescVal.font = textFont
        personalDescVal.alignment = Element.ALIGN_JUSTIFIED_ALL
        personalDescVal.alignment = Element.ALIGN_LEFT
        ct1.setSimpleColumn(120f, 30f, 420f, 370f)
        ct1.addElement(personalDescVal)
        ct1.go()

        document.close();

        tv_locStorage.text = fileName.absolutePath

    }

    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}