package com.trackster.tracksterapp.main.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.Toast
import com.trackster.tracksterapp.R
import com.trackster.tracksterapp.base.App
import com.trackster.tracksterapp.base.BaseMainViewFragment
import com.trackster.tracksterapp.main.DaggerMainComponent
import com.trackster.tracksterapp.main.MainActivityContextModule
import com.trackster.tracksterapp.main.MainContract
import com.trackster.tracksterapp.main.MainPresenterModule
import com.trackster.tracksterapp.model.Shipment
import io.reactivex.disposables.CompositeDisposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SettingsFragment : BaseMainViewFragment(), View.OnClickListener, FragmentManager.OnBackStackChangedListener {
    override fun updateLoads(loadsList: ArrayList<Shipment>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackStackChanged() {
        if (fragmentPosition == requireFragmentManager().backStackEntryCount) {
            this.onResume()
        }
    }

    @Inject
    lateinit var presenter: MainContract.Presenter
    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun getLayoutId(): Int {
        return super.getLayoutId()
    }

    private val GALLERY = 1
    private val CAMERA = 2
    private val jpg = ".jpg"
    private val dateTime = System.currentTimeMillis()
    private val pictureName = dateTime.toString() + jpg
    var fragmentPosition: Int = 0
    var langChaged = false

    private val IMAGE_DIRECTORY = "/demonuts"

    private fun getUserDetails() {

//        val createUsername = "${EditProfileObject.editUserProfile?.firstName
//            ?: ""} ${EditProfileObject.editUserProfile?.lastName ?: ""}"
//        UserNameTextView?.text = createUsername
//        nickNameTextView?.text = EditProfileObject.editUserProfile?.nickName
//        val birthday = EditProfileObject.editUserProfile?.birthday
//        if (birthday != null) {
//            birthdateTextView?.text = DateFormat.formatDate(birthday, DateFormat.DATE_FORMAT_MENU_DATE)
//        }
//        phoneTextView?.text = EditProfileObject.editUserProfile?.phoneNumber
//        emailNameTextView?.text = EditProfileObject.editUserProfile?.email
    }

    private fun setUI() {
//        val profileImageUrl = PreferenceUtils.getUserProfilePicture(activity!!)
//        if (!TextUtils.isEmpty(profileImageUrl)) {
//            Picasso.get()
//                .load(profileImageUrl)
//                .placeholder(R.drawable.ic_avatar)
//                .error(R.drawable.ic_avatar)
//                .into(userProfileImageView)
//        }
    }

    fun onButtonShowPopupWindowClick(view: View) {

        val inflater = LayoutInflater.from(context)

//        val popupView = inflater!!.inflate(R.layout.choose_photo_dialog, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
//        val popupWindow = PopupWindow(popupView, width, height, focusable)


//        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener { v, event ->
//            popupWindow.dismiss()
//            true
//        }
//        popupView.button_camera.setOnClickListener {
//            if (checkPermission()) {
//
//                takePhotoFromCamera()
//                popupWindow.dismiss()
//
//            } else {
//                (activity as MainActivity).requestCameraPermission()
//                popupWindow.dismiss()
//            }
//        }
//        popupView.button_gallery.setOnClickListener {
//            if (Build.VERSION.SDK_INT <= 19) {
//                val i = Intent()
//                i.type = "image/*"
//                i.action = Intent.ACTION_GET_CONTENT
//                i.addCategory(Intent.CATEGORY_OPENABLE)
//                startActivityForResult(i, GALLERY)
//            } else if (Build.VERSION.SDK_INT > 19) {
//
//                choosePhotoFromGallary()
//                popupWindow.dismiss()
//
//            }
//        }
//        popupView.cancel_button_picture.setOnClickListener { popupWindow.dismiss() }
//    }

//        override fun onResume() {
//            super.onResume()
//            getUserDetails()
//        }


//    private fun getScaledBitmap(b: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
//        val bWidth = b.width
//        val bHeight = b.height
//
//        var nWidth = bWidth
//        var nHeight = bHeight
//
//        if (nWidth > reqWidth) {
//            val ratio = bWidth / reqWidth
//            if (ratio > 0) {
//                nWidth = reqWidth
//                nHeight = bHeight / ratio
//            }
//        }
//
//        if (nHeight > reqHeight) {
//            val ratio = bHeight / reqHeight
//            if (ratio > 0) {
//                nHeight = reqHeight
//                nWidth = bWidth / ratio
//            }
//        }
//
//        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true)
//    }

        fun saveImage(myBitmap: Bitmap): String {
            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
            val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY
            )
            // have the object build the directory structure, if needed.
            Log.d("fee", wallpaperDirectory.toString())
            if (!wallpaperDirectory.exists()) {

                wallpaperDirectory.mkdirs()
            }

            try {
                Log.d("heel", wallpaperDirectory.toString())
                val f = File(
                    wallpaperDirectory, ((Calendar.getInstance()
                        .getTimeInMillis()).toString() + ".jpg")
                )
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                MediaScannerConnection.scanFile(
                    context!!,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null
                )
                fo.close()
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

                return f.getAbsolutePath()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

            return ""
        }


        fun resizeBase64Image(base64image: String): String {
            val encodeByte = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
            val options = BitmapFactory.Options()
            options.inPurgeable = true
            var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)


            if (image.height <= 300 && image.width <= 300) {
                return base64image
            }
            image = Bitmap.createScaledBitmap(image, 310, 350, false)

            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 90, baos)

            val b = baos.toByteArray()
            System.gc()
            return Base64.encodeToString(b, Base64.NO_WRAP)

        }

        fun getBase64(bitmap: Bitmap): String? {
            try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                return Base64.encodeToString(byteArray, Base64.NO_WRAP)
            } catch (e: Exception) {
                return null
            }

        }

        fun choosePhotoFromGallary() {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(galleryIntent, GALLERY)
        }

//        private fun takePhotoFromCamera() {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(intent, CAMERA)
//        }
//
//        private fun checkPermission(): Boolean {
//            return ContextCompat.checkSelfPermission(
//                context!!,
//                Manifest.permission.CAMERA
//            ) == PackageManager.PERMISSION_GRANTED
//        }


       fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == GALLERY) {
                    if (data != null) {
                        val contentURI = data!!.data
                        try {
                            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, contentURI)
                            val path = saveImage(bitmap)
                            Toast.makeText(context!!, "Image Saved!", Toast.LENGTH_SHORT).show()
//                            val newBitmap = getScaledBitmap(bitmap, 200, 300)
//                            userProfileImageView.setImageBitmap(newBitmap)
//                            val photo = getBase64(newBitmap)
//                            val photoToUpload = resizeBase64Image(photo!!)
//                            presenter.postProfilePicture(ProfilePictureRequest(photoToUpload, pictureName))

                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(context!!, "Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else if (requestCode == CAMERA) {

                    val thumbnail = data!!.extras!!.get("data") as Bitmap
                    val name = saveImage(thumbnail)
                    Toast.makeText(context!!, "Image Saved!", Toast.LENGTH_SHORT).show()
//                    val newBitmapCamera = getScaledBitmap(thumbnail, 400, 600)
//                    userProfileImageView!!.setImageBitmap(newBitmapCamera)
//                    val photoCamera = getBase64(newBitmapCamera)
//                    val photoToUploadCamera = resizeBase64Image(photoCamera!!)
//                    presenter.postProfilePicture(ProfilePictureRequest(photoToUploadCamera, pictureName))

                }
            }
        }
        }


        fun reloadStrings() {
//        MyAccountTextView.text = getString(R.string.my_account)
//        FirstNameTextView.text = getString(R.string.first_name_last_name)
//        NickNameTextView.text = getString(R.string.nickname)
//        BirthdateTextView.text = getString(R.string.date_of_birth)
//        PhoneTextView.text = getString(R.string.phone)
//        EmailTextView.text = getString(R.string.email)
//        PasswordTextView.text = getString(R.string.password)
//        MoreInformationTextView.text = getString(R.string.more_information)
//        MembershipTextView.text = getString(R.string.clubcity_membership)
//        SupportTextView.text = getString(R.string.support)
//        TermsAndConditionsTextView.text = getString(R.string.terms_and_conditions)
//        PrivacyTextView.text = getString(R.string.privacy_policy)
//        AccountActionsTextView.text = getString(R.string.account_actions)
//        LogOffTextView.text = getString(R.string.log_out)
//        NotificationPreferencesTextView.text = getString(R.string.profile_settings_notificaiton_preference)
//        PreferencesSettingsTextView.text = getString(R.string.my_preferences)
        }

//
//    fun logOffUser() {
//        DialogUtils.showChooseDialog(requireActivity(), getString(R.string.are_you_sure_log_out), "",
//            (DialogInterface.OnClickListener { dialogInterface, i ->
//                run {
//                    (activity as MainActivity).logOut()
//                }
//            }))
//    }
//
//    override fun displayUser(editUserProfileResponse: EditUserProfileResponse) {
//        this.userProfile = editUserProfileResponse
//        EditProfileObject.editUserProfile = userProfile
//        getUserDetails()
//        mainPresenter.getMe()
//    }
//
//    override fun userRetrieved(user: User) {
//        EditProfileObject.editUserProfile?.setNotificationPreference(EditProfileNotifications().apply { loadDataFromUser(user) })
//    }
////        override fun getLayoutId(): Int = R.layout.fragment_profile_settings
// //
//    }

//    override fun onResume() {
//        super.onResume()
//    }

    override fun updateView() {
        super.updateView()
    }

    override fun handleApiError(throwable: Throwable) {
        super.handleApiError(throwable)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun setSoftInputStyle() {
        super.setSoftInputStyle()
    }

    override fun getProgressBar(): ProgressBar? = null

    override fun onStop() {
        requireFragmentManager().removeOnBackStackChangedListener(this)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mainComponent = DaggerMainComponent.builder()
            .appComponent((activity!!.application as App).getAppComponent())
            .mainActivityContextModule(MainActivityContextModule(activity!!))
            .mainPresenterModule(MainPresenterModule(this))
            .build()
        mainComponent.injectSettingsfragment(this)
//            presenter.getUserProfile()
        setUI()

        fragmentPosition = requireFragmentManager().backStackEntryCount
        requireFragmentManager().addOnBackStackChangedListener(this)
    }
}