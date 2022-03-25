package arpit.parekh.mvvmmodule
import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location

import androidx.core.app.ActivityCompat.startActivityForResult

import android.location.LocationRequest
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider


import com.gda.citizenapp.BuildConfig
import com.gda.citizenapp.R
import com.gda.citizenapp.base.BaseAppCompatPermissionActivity
import com.gda.citizenapp.base.PermissionListener
import com.gda.citizenapp.databinding.ActivityAddLocationBinding
import com.gda.citizenapp.listeners.OnRecyclerRowViewClickListener
import com.gda.citizenapp.materialfilepicker.MaterialFilePicker
import com.gda.citizenapp.materialfilepicker.ui.FilePickerActivity
import com.gda.citizenapp.materialfilepicker.utils.FileTypeUtils
import com.gda.citizenapp.model.Area
import com.gda.citizenapp.model.Department
import com.gda.citizenapp.model.GalleryItem
import com.gda.citizenapp.model.Zone
import com.gda.citizenapp.network.NetworkManager
import com.gda.citizenapp.network.RequestListener
import com.gda.citizenapp.request.PARAMS
import com.gda.citizenapp.request.RequestBuilder
import com.gda.citizenapp.utils.Constants
import com.gda.citizenapp.utils.DialogUtils
import com.gda.citizenapp.utils.ImageUtils
import com.gda.citizenapp.utils.Log
import com.gda.citizenapp.utils.Utils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.ClassCastException
import java.lang.Exception
import java.util.*


class AddLocationActivity : BaseAppCompatPermissionActivity(), PermissionListener,
    View.OnClickListener, RequestListener {
    private var mBinding: ActivityAddLocationBinding? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private val mMediaList: ArrayList<GalleryItem>? = ArrayList<GalleryItem>()
    private var mSelectedImageView: ImageView? = null
    private var fileUri: Uri? = null
    private var mPermissionGivenCount = 0
    private var mReqIdAddLocation = 0
    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null
    private var mNetworkManager: NetworkManager? = null
    private var mImageLoader: ImageLoader? = null
    private var defaultOptions: DisplayImageOptions? = null
    private var mPath: String? = ""
    private var mLong = 0.0
    private var mLat = 0.0
    private var mLocationRequest: LocationRequest? = null
    private var mIntent: Intent? = null
    private var mDepartmentID = 0
    private var mZoneID = 0
    private var mLocationAreaId = 0
    private var pref: SharedPreferences? = null
    private var mDepartmentList: ArrayList<Department> = ArrayList<Department>()
    private var mZoneList: ArrayList<Zone> = ArrayList<Zone>()
    private val mAreaList: ArrayList<Area> = ArrayList<Area>()
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddLocationBinding.inflate(getLayoutInflater())
        val view: View = mBinding.getRoot()
        setContentView(view)
        mNetworkManager = NetworkManager.getInstance()
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        initActionBar()
        setTitle(getString(R.string.add_location_activity))


        // initialize location service
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(UPDATE_INTERVAL)
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL)
        initData()
    }

    private fun initData() {
        mImageLoader = ImageLoader.getInstance()
        defaultOptions = Builder()
            .resetViewBeforeLoading(true)
            .showImageOnLoading(R.drawable.screen_bg)
            .showImageForEmptyUri(R.drawable.screen_bg)
            .showImageOnFail(R.drawable.screen_bg)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .postProcessor(null)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()

        // method to get the location
        lastLocation
        mMediaList!!.clear()
        mBinding.imvItem1.setOnClickListener(this)
        mBinding.imvItem2.setOnClickListener(this)
        mBinding.imvItem3.setOnClickListener(this)
        mBinding.imvItem4.setOnClickListener(this)
        mBinding.imvItem5.setOnClickListener(this)
        mBinding.imvItem6.setOnClickListener(this)
        mBinding.imvItem7.setOnClickListener(this)
        mBinding.imvItem8.setOnClickListener(this)
        mBinding.imvItem9.setOnClickListener(this)
        mBinding.imvItem10.setOnClickListener(this)
        mBinding.imvItem11.setOnClickListener(this)
        mBinding.imvItem12.setOnClickListener(this)
        mBinding.imvItem13.setOnClickListener(this)
        mBinding.imvItem14.setOnClickListener(this)
        mBinding.imvItem15.setOnClickListener(this)
        mBinding.imvItem16.setOnClickListener(this)
        mBinding.imvItem17.setOnClickListener(this)
        mBinding.imvItem18.setOnClickListener(this)
        mBinding.imvItem19.setOnClickListener(this)
        mBinding.imvItem20.setOnClickListener(this)
        spinnerLoad()
        mBinding.btnSubmit.setOnClickListener { view ->
            if (checkValidation()) {
                createRequest()
            }
        }
        mBinding.btnLocation.setOnClickListener { view ->
            try {
                if (mFusedLocationClient != null) {
                    if (!isPermissionGranted(
                            this@AddLocationActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        askForPermission(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            this@AddLocationActivity,
                            "Location"
                        )
                    } else {
                        startLocationUpdates(INTENT_GPS_SETTINGS_FOR_LOCATION)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun spinnerLoad() {
        var listType = object : TypeToken<List<Department?>?>() {}.type
        mDepartmentList = Gson().fromJson<ArrayList<Department>>(
            pref!!.getString(PARAMS.KEY_DEPARTMENT, "[]"),
            listType
        )
        listType = object : TypeToken<List<Zone?>?>() {}.type
        mZoneList =
            Gson().fromJson<ArrayList<Zone>>(pref!!.getString(PARAMS.KEY_ZONE, "[]"), listType)
        if (mDepartmentList.size > 0) {
            mBinding.llDepartment.setVisibility(View.VISIBLE)
            val queryList = ArrayList<String>()
            for (i in mDepartmentList.indices) {
                queryList.add(mDepartmentList[i].getDepartment())
            }
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<Any?>(this, R.layout.row_spinner, queryList)
            adapter.setDropDownViewResource(R.layout.row_spinner)
            mBinding.spnDepartment.setAdapter(adapter)
        } else {
            mBinding.llDepartment.setVisibility(View.GONE)
        }
        mBinding.spnDepartment.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                mDepartmentID = mDepartmentList[position].getDepartmentId()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        if (mZoneList.size > 0) {
            mBinding.llZone.setVisibility(View.VISIBLE)
            val queryList = ArrayList<String>()
            for (i in mZoneList.indices) {
                queryList.add(mZoneList[i].getZone())
            }
            val adapter: ArrayAdapter<String> =
                ArrayAdapter<Any?>(this, R.layout.row_spinner, queryList)
            adapter.setDropDownViewResource(R.layout.row_spinner)
            mBinding.spnZone.setAdapter(adapter)
        } else {
            mBinding.llZone.setVisibility(View.GONE)
        }
        mBinding.spnZone.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                mAreaList.clear()
                mAreaList.addAll(mZoneList[position].getAreaList())
                Log.e("mAreaList == >", "" + mAreaList.size)
                mZoneID = mZoneList[position].getZoneId()
                if (mAreaList.size > 0) {
                    mBinding.llColony.setVisibility(View.VISIBLE)
                    val queryList = ArrayList<String>()
                    for (i in mAreaList.indices) {
                        queryList.add(mAreaList[i].getArea())
                    }
                    val displayMetrics = DisplayMetrics()
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
                    val width = displayMetrics.widthPixels - 75
                    mBinding.spnColony.setDropDownWidth(width)
                    val adapter: ArrayAdapter<String> = ArrayAdapter<Any?>(
                        this@AddLocationActivity,
                        R.layout.row_spinner,
                        queryList
                    )
                    adapter.setDropDownViewResource(R.layout.row_spinner)
                    mBinding.spnColony.setAdapter(adapter)
                } else {
                    mBinding.llColony.setVisibility(View.GONE)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        mBinding.spnColony.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                mLocationAreaId = mAreaList[position].getAreaId()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    // Here 1 represent max location result to returned, by documents it recommended 1 to 5
    @get:SuppressLint("MissingPermission")
    private val lastLocation: Unit
        private get() {
            mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful() && task.getResult() != null) {
                        val lastLocation: Location = task.getResult()
                        Constants.LATITUDE = lastLocation.latitude
                        Constants.LONGITUDE = lastLocation.longitude
                        geocoder = Geocoder(this@AddLocationActivity, Locale.getDefault())
                        try {
                            addresses = geocoder!!.getFromLocation(
                                lastLocation.latitude,
                                lastLocation.longitude,
                                1
                            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        var address = ""
                        if (addresses!!.size > 0) {
                            address = addresses!![0].getAddressLine(0)
                        }
                        if (address.toLowerCase().contains("ghaziabad")) {
                            mBinding.txtLocation.setText(address)
                            mLat = lastLocation.latitude
                            mLong = lastLocation.longitude
                            val url: String = Utils.getSatelliteMapImageUrl(
                                lastLocation.latitude,
                                lastLocation.longitude,
                                900,
                                400
                            )
                            mBinding.frmMap.setVisibility(View.VISIBLE)
                            mImageLoader.displayImage(url, mBinding.imvMap, defaultOptions)
                        }
                    }
                }
        }

    fun checkValidation(): Boolean {
        var isSuccess = true
        if (mDepartmentID == 0) {
            isSuccess = false
            mBinding.txtErrDepartment.setSelected(true)
            mBinding.txtErrDepartment.setText(R.string.err_select_department)
            mBinding.txtErrDepartment.setVisibility(View.VISIBLE)
        } else {
            mBinding.txtErrDepartment.setSelected(false)
            mBinding.txtErrDepartment.setText("")
            mBinding.txtErrDepartment.setVisibility(View.GONE)
        }
        if (mZoneID == 0) {
            isSuccess = false
            mBinding.txtZoneError.setSelected(true)
            mBinding.txtZoneError.setText(R.string.err_select_zone)
            mBinding.txtZoneError.setVisibility(View.VISIBLE)
        } else {
            mBinding.txtZoneError.setSelected(false)
            mBinding.txtZoneError.setText("")
            mBinding.txtZoneError.setVisibility(View.GONE)
        }
        if (mLocationAreaId == 0) {
            isSuccess = false
            mBinding.txtErrColony.setSelected(true)
            mBinding.txtErrColony.setText(R.string.err_select_area)
            mBinding.txtErrColony.setVisibility(View.VISIBLE)
        } else {
            mBinding.txtErrColony.setSelected(false)
            mBinding.txtErrColony.setText("")
            mBinding.txtErrColony.setVisibility(View.GONE)
        }
        if (TextUtils.isEmpty(mBinding.txtLocation.getText().toString().trim())) {
            isSuccess = false
            mBinding.txtLocation.setSelected(true)
            mBinding.txtErrLocation.setText(R.string.err_enter_location)
            mBinding.txtErrLocation.setVisibility(View.VISIBLE)
        } else {
            mBinding.txtLocation.setSelected(false)
            mBinding.txtErrLocation.setText("")
            mBinding.txtErrLocation.setVisibility(View.GONE)
        }
        if (TextUtils.isEmpty(mBinding.edtTitle.getText().toString().trim())) {
            isSuccess = false
            mBinding.edtTitle.setSelected(true)
            mBinding.txtErrTitle.setText(R.string.err_enter_title)
            mBinding.txtErrTitle.setVisibility(View.VISIBLE)
        } else {
            mBinding.edtTitle.setSelected(false)
            mBinding.txtErrTitle.setText("")
            mBinding.txtErrTitle.setVisibility(View.GONE)
        }
        if (TextUtils.isEmpty(mBinding.edtDescription.getText().toString().trim())) {
            isSuccess = false
            mBinding.edtDescription.setSelected(true)
            mBinding.txtErrDescription.setText(R.string.err_enter_description)
            mBinding.txtErrDescription.setVisibility(View.VISIBLE)
        } else {
            mBinding.edtDescription.setSelected(false)
            mBinding.txtErrDescription.setText("")
            mBinding.txtErrDescription.setVisibility(View.GONE)
        }
        return isSuccess
    }

    /**
     * Prepare request to add request
     */
    private fun createRequest() {
        val fileMap = HashMap<String, File>()
        if (mBinding.imvItem1.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem1.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem2.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem2.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem3.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem3.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem4.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem4.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem5.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem5.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem6.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem6.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem7.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem7.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem8.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem8.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem9.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem9.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem10.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem10.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem11.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem11.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem12.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem12.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem13.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem13.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem14.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem14.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem15.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem15.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem16.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem16.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem17.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem17.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem18.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem18.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem19.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem19.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (mBinding.imvItem20.getTag() != null) {
            val file: File = File(java.lang.String.valueOf(mBinding.imvItem20.getTag()))
            fileMap[file.name] = file
            try {
                val exif = ExifInterface(file.path)
                ImageUtils.updateLocationToExif(exif)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.e("mMediaList :: ", "" + mMediaList)
        if (mMediaList != null && !mMediaList.isEmpty() && mMediaList.size > 0) {
            for (i in mMediaList.indices) {
                fileMap[mMediaList[i].getFileName()] = mMediaList[i].getFile()
            }
        }
        mNetworkManager.isProgressBarVisible(true)
        mReqIdAddLocation = mNetworkManager.addMultipartRequest(
            RequestBuilder.getCreateLocationReqParams(
                mBinding.edtTitle.getText().toString(),
                mBinding.edtDescription.getText().toString(),
                mDepartmentID.toString(),
                mZoneID.toString(),
                mLocationAreaId.toString(),
                mBinding.txtLocation.getText().toString(),
                mLat.toString(),
                mLong.toString()
            ), fileMap, this@AddLocationActivity, RequestBuilder.METHOD_CREATE_LOCATION_ACTIVITY
        )
    }

    /**
     * Trigger new location updates at interval
     */
    protected fun startLocationUpdates(requestCode: Int) {
        if (Constants.LATITUDE === 0 && Constants.LONGITUDE === 0) {
            lastLocation
        }

        // Create LocationSettingsRequest object using location request
        val builder: LocationSettingsRequest.Builder = Builder()
        builder.setAlwaysShow(true)
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest: LocationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(locationSettingsRequest)
        task.addOnCompleteListener(OnCompleteListener<LocationSettingsResponse?> { task1: Task<LocationSettingsResponse?> ->
            try {
                val response: LocationSettingsResponse? =
                    task1.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location requests here.
                if (requestCode == INTENT_GPS_SETTINGS_FOR_CAMERA) {
                    startActivityForResult(
                        mIntent,
                        REQ_CODE_CAMERA
                    )
                } else if (requestCode == INTENT_GPS_SETTINGS_FOR_LOCATION) {
                    if (mLat == 0.0 && mLong == 0.0) {
                        mLat = Constants.LATITUDE
                        mLong = Constants.LONGITUDE
                    }
                    val intent = Intent(this@AddLocationActivity, PlaceActivity::class.java)
                    intent.putExtra(PlaceActivity.EXTRA_LATITUDE, mLat)
                    intent.putExtra(PlaceActivity.EXTRA_LONGITUDE, mLong)
                    intent.putExtra(PlaceActivity.IS_FROM_LOCATION, true)
                    startActivityForResult(
                        intent,
                        REQ_CODE_LOCATION
                    )
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable =
                                exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this@AddLocationActivity,
                                requestCode
                            )
                        } catch (e: SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        })
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(5)
        mLocationRequest.setFastestInterval(0)
        mLocationRequest.setNumUpdates(1)

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    @SuppressLint("MissingPermission")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mMediaList!!.clear()
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_CODE_DOCUMENT -> {
                    val imagePath = ArrayList<String>()
                    imagePath.add("bmp")
                    imagePath.add("gif")
                    imagePath.add("ico")
                    imagePath.add("jpeg")
                    imagePath.add("jpg")
                    imagePath.add("pcx")
                    imagePath.add("png")
                    imagePath.add("psd")
                    imagePath.add("tga")
                    imagePath.add("tiff")
                    imagePath.add("tif")
                    imagePath.add("xcf")
                    var filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                    try {
                        val fileExtension: String = FileTypeUtils.getExtension(File(filePath).name)
                        if (fileExtension.contains("jpg") || fileExtension.contains("jpeg")) {
                            filePath =
                                ImageUtils.compressImage(this@AddLocationActivity, filePath, false)
                        }
                        mSelectedImageView!!.tag = filePath
                        if (fileExtension.contains("docx")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_doc)
                        } else if (fileExtension.contains("doc")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_doc)
                        } else if (fileExtension.contains("pdf")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_pdf)
                        } else if (fileExtension.contains("mov")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("wmv")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("mkv")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("3gp")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("f4v")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("flv")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("mp4")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("mpeg")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("webm")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_video)
                        } else if (fileExtension.contains("txt")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_file)
                        } else if (fileExtension.contains("xlsx")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_file)
                        } else if (fileExtension.contains("xltx")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_file)
                        } else if (fileExtension.contains("xls")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_file)
                        } else if (fileExtension.contains("ppt")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_file)
                        } else if (fileExtension.contains("pptx")) {
                            mSelectedImageView!!.setImageResource(R.drawable.ic_file)
                        } else if (imagePath.contains(fileExtension)) {
                            if (filePath!!.startsWith("http")) {
                                mImageLoader.displayImage(
                                    filePath,
                                    mSelectedImageView,
                                    defaultOptions
                                )
                            } else if (!TextUtils.isEmpty(filePath)) {
                                mImageLoader.displayImage(
                                    "file://$filePath",
                                    mSelectedImageView,
                                    defaultOptions
                                )
                            } else {
                                mImageLoader.displayImage(null, mSelectedImageView, defaultOptions)
                            }
                        } else {
                            DialogUtils.showMessage(
                                this,
                                "",
                                getString(R.string.msg_file_extension)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                REQ_CODE_CAMERA -> try {
                    Log.e(TAG, "Image from camera got path :->$mPath")
                    mPath = ImageUtils.compressImage(this@AddLocationActivity, mPath, false)
                    val mTmpMediaList: ArrayList<GalleryItem> =
                        ImageUtils.getImageListFromPath(mPath)
                    Log.e("fileSize==>", mPath!!.length.toString() + "")
                    val path: String = mTmpMediaList[0].getAttachmentLink()
                    mSelectedImageView!!.tag = path
                    if (path.startsWith("http")) {
                        mImageLoader.displayImage(path, mSelectedImageView, defaultOptions)
                    } else if (!TextUtils.isEmpty(path)) {
                        mImageLoader.displayImage(
                            "file://$path",
                            mSelectedImageView,
                            defaultOptions
                        )
                    } else {
                        mImageLoader.displayImage(null, mSelectedImageView, defaultOptions)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                REQ_CODE_LOCATION -> {
                    mLat = data.getDoubleExtra(PARAMS.TAG_LATITUDE, 0.0)
                    mLong = data.getDoubleExtra(PARAMS.TAG_LONGITUDE, 0.0)
                    val address = data.getStringExtra(PARAMS.TAG_ADDRESS)
                    mBinding.txtLocation.setText(address)
                    val url: String = Utils.getSatelliteMapImageUrl(mLat, mLong, 900, 400)
                    mBinding.frmMap.setVisibility(View.VISIBLE)
                    mImageLoader.displayImage(url, mBinding.imvMap, defaultOptions)
                    mBinding.txtLocation.setSelected(false)
                    mBinding.txtErrLocation.setText("")
                    mBinding.txtErrLocation.setVisibility(View.GONE)
                    try {
                        Log.e(
                            TAG,
                            "Video from camera got path :->$mPath"
                        )
                        if (mPath != null || mPath !== "") {
                            val mTmpMediaList: ArrayList<GalleryItem> =
                                ImageUtils.getImageListFromPath(mPath)
                            mMediaList.addAll(mTmpMediaList)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                REQ_CODE_VIDEO -> try {
                    Log.e(TAG, "Video from camera got path :->$mPath")
                    if (mPath != null || mPath !== "") {
                        val mTmpMediaList: ArrayList<GalleryItem> =
                            ImageUtils.getImageListFromPath(mPath)
                        mMediaList.addAll(mTmpMediaList)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                INTENT_GPS_SETTINGS_FOR_CAMERA -> {
                    if (mFusedLocationClient != null) {
                        mFusedLocationClient.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                    }
                    startActivityForResult(mIntent, REQ_CODE_CAMERA)
                }
                INTENT_GPS_SETTINGS_FOR_LOCATION -> {
                    if (mFusedLocationClient != null) {
                        mFusedLocationClient.requestLocationUpdates(
                            mLocationRequest,
                            mLocationCallback,
                            Looper.myLooper()
                        )
                    }
                    if (mLat == 0.0 && mLong == 0.0) {
                        mLat = Constants.LATITUDE
                        mLong = Constants.LONGITUDE
                    }
                    val intent = Intent(this@AddLocationActivity, PlaceActivity::class.java)
                    intent.putExtra(PlaceActivity.EXTRA_LATITUDE, mLat)
                    intent.putExtra(PlaceActivity.EXTRA_LONGITUDE, mLong)
                    startActivityForResult(intent, REQ_CODE_LOCATION)
                }
                REQ_CODE_GALLERY -> {
                    val mTmpMediaList: ArrayList<GalleryItem> =
                        ImageUtils.getImageListFromIntent(this@AddLocationActivity, data)
                    var path: String = mTmpMediaList[0].getAttachmentLink()
                    path = ImageUtils.compressImage(this@AddLocationActivity, path, false)
                    Log.e("fileSize==>", path.length.toString() + "")
                    mSelectedImageView!!.tag = path
                    if (!TextUtils.isEmpty(path)) {
                        mImageLoader.displayImage(
                            "file://$path",
                            mSelectedImageView,
                            defaultOptions
                        )
                    } else {
                        mImageLoader.displayImage(null, mSelectedImageView, defaultOptions)
                    }
                }
            }
        }
    }

    protected fun onDestroy() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        }
        super.onDestroy()
    }

    var mLocationCallback: LocationCallback = object : LocationCallback() {
        fun onLocationResult(locationResult: LocationResult) {
            Constants.LATITUDE = locationResult.getLastLocation().getLatitude()
            Constants.LONGITUDE = locationResult.getLastLocation().getLongitude()
            Log.e(TAG, "Lat-Long => " + Constants.LATITUDE.toString() + " | " + Constants.LONGITUDE)
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(view: View) {
        mSelectedImageView = null
        when (view.id) {
            R.id.imvItem1 -> mSelectedImageView = mBinding.imvItem1
            R.id.imvItem2 -> mSelectedImageView = mBinding.imvItem2
            R.id.imvItem3 -> mSelectedImageView = mBinding.imvItem3
            R.id.imvItem4 -> mSelectedImageView = mBinding.imvItem4
            R.id.imvItem5 -> mSelectedImageView = mBinding.imvItem5
            R.id.imvItem6 -> mSelectedImageView = mBinding.imvItem6
            R.id.imvItem7 -> mSelectedImageView = mBinding.imvItem7
            R.id.imvItem8 -> mSelectedImageView = mBinding.imvItem8
            R.id.imvItem9 -> mSelectedImageView = mBinding.imvItem9
            R.id.imvItem10 -> mSelectedImageView = mBinding.imvItem10
            R.id.imvItem11 -> mSelectedImageView = mBinding.imvItem11
            R.id.imvItem12 -> mSelectedImageView = mBinding.imvItem12
            R.id.imvItem13 -> mSelectedImageView = mBinding.imvItem13
            R.id.imvItem14 -> mSelectedImageView = mBinding.imvItem14
            R.id.imvItem15 -> mSelectedImageView = mBinding.imvItem15
            R.id.imvItem16 -> mSelectedImageView = mBinding.imvItem16
            R.id.imvItem17 -> mSelectedImageView = mBinding.imvItem17
            R.id.imvItem18 -> mSelectedImageView = mBinding.imvItem18
            R.id.imvItem19 -> mSelectedImageView = mBinding.imvItem19
            R.id.imvItem20 -> mSelectedImageView = mBinding.imvItem20
        }
        if (mSelectedImageView != null) {
            val items = ArrayList<String>()
            items.add(getString(R.string.capture_image))
            //        items.add(getString(R.string.capture_video));
//            items.add(getString(R.string.select_from_gallery));
            items.add(getString(R.string.select_document))
            DialogUtils.showOptionList(
                this@AddLocationActivity,
                getString(R.string.select_option),
                items,
                object : OnRecyclerRowViewClickListener() {
                    fun onViewClick(view: View?, position: Int) {
                        when (position) {
                            0 -> {
                                // Create the File where the photo should go
                                var photoFile: File? = null
                                try {
                                    photoFile =
                                        ImageUtils.createImageFile(this@AddLocationActivity, ".png")
                                    mPath = photoFile.absolutePath
                                } catch (ex: IOException) {
                                    // Error occurred while creating the File
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    fileUri = FileProvider.getUriForFile(
                                        this@AddLocationActivity,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        photoFile
                                    )
                                }
                                mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                mIntent!!.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                if (fileUri != null) {
                                    mIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                                }
                                if (isPermissionsGranted(
                                        this@AddLocationActivity,
                                        arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    )
                                ) {
                                    startLocationUpdates(INTENT_GPS_SETTINGS_FOR_CAMERA)
                                    //                                startActivityForResult(mIntent, REQ_CODE_CAMERA);
                                } else {
                                    mPermissionGivenCount = 0
                                    askForPermissions(
                                        arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ), this@AddLocationActivity, "Camera"
                                    )
                                }
                            }
                            11 -> {
                                var photoFile1: File? = null
                                try {
                                    photoFile1 =
                                        ImageUtils.createImageFile(this@AddLocationActivity, ".mp4")
                                    mPath = photoFile1.absolutePath
                                } catch (ex: IOException) {
                                    // Error occurred while creating the File
                                }
                                // Continue only if the File was successfully created
                                if (photoFile1 != null) {
                                    fileUri = FileProvider.getUriForFile(
                                        this@AddLocationActivity,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        photoFile1
                                    )
                                }
                                mIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                                mIntent!!.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                if (fileUri != null) mIntent!!.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    fileUri
                                )
                                if (isPermissionsGranted(
                                        this@AddLocationActivity,
                                        arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        )
                                    )
                                ) {
                                    startActivityForResult(mIntent, REQ_CODE_VIDEO)
                                } else {
                                    askForPermissions(
                                        arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ), this@AddLocationActivity, "VideoCamera"
                                    )
                                }
                            }
                            12 -> if (isPermissionGranted(
                                    this@AddLocationActivity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            ) {
                                if (Build.VERSION.SDK_INT < 19) {
                                    mIntent = Intent(Intent.ACTION_GET_CONTENT)
                                    mIntent!!.type = "video/* image/*"
                                    //                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                                    }
                                    startActivityForResult(
                                        Intent.createChooser(
                                            mIntent,
                                            "Select Picture"
                                        ), REQ_CODE_GALLERY
                                    )
                                } else {
                                    val intent = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    //
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                    intent.type = "image/*"
                                    startActivityForResult(
                                        Intent.createChooser(
                                            intent,
                                            getString(R.string.select_option)
                                        ), REQ_CODE_GALLERY
                                    )
                                }
                            } else {
                                askForPermission(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    this@AddLocationActivity,
                                    "Gallery"
                                )
                            }
                            1 -> if (isPermissionGranted(
                                    this@AddLocationActivity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                )
                            ) {
                                MaterialFilePicker()
                                    .withActivity(this@AddLocationActivity)
                                    .withRequestCode(REQ_CODE_DOCUMENT)
                                    .withHiddenFiles(false)
                                    .withTitle(getString(R.string.select_document))
                                    .start()
                            } else {
                                askForPermission(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    this@AddLocationActivity,
                                    "Document"
                                )
                            }
                        }
                    }
                })
        }
    }

    fun permissionGranted(permission: String?, tag: String) {
        if (tag.equals("VideoCamera", ignoreCase = true)) {
            if (isPermissionGranted(this@AddLocationActivity, Manifest.permission.CAMERA)) {
                startActivityForResult(mIntent, REQ_CODE_VIDEO)
            } else {
                askForPermission(
                    Manifest.permission.CAMERA,
                    this@AddLocationActivity,
                    "VideoCamera"
                )
            }
        } else if (tag.equals("Camera", ignoreCase = true)) {
            mPermissionGivenCount++
            if (mPermissionGivenCount == 3) {
                startLocationUpdates(INTENT_GPS_SETTINGS_FOR_CAMERA)
                //                startActivityForResult(mIntent, REQ_CODE_CAMERA);
            }
        } else if (tag.equals("Gallery", ignoreCase = true)) {
            if (Build.VERSION.SDK_INT < 19) {
                mIntent = Intent(Intent.ACTION_GET_CONTENT)
                mIntent!!.type = "video/* image/*"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    mIntent!!.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                startActivityForResult(
                    Intent.createChooser(mIntent, "Select Picture"),
                    REQ_CODE_GALLERY
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                //                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setType("image/*");
//                        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
//                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(
                    Intent.createChooser(
                        intent,
                        getString(R.string.select_option)
                    ), REQ_CODE_GALLERY
                )
            }
        } else if (tag.equals("Document", ignoreCase = true)) {
            MaterialFilePicker()
                .withActivity(this@AddLocationActivity)
                .withRequestCode(REQ_CODE_DOCUMENT)
                .withHiddenFiles(false)
                .withTitle(getString(R.string.select_document))
                .start()
        }
    }

    fun permissionDenied(permission: String?) {}
    fun permissionForeverDenied(permission: String?) {}
    fun permissionSettingDialogClosed(permission: String?) {}
    fun onSuccess(id: Int, response: String?) {
        try {
            if (!TextUtils.isEmpty(response)) {
                if (id == mReqIdAddLocation) {
                    val jObjResponse = JSONObject(response)
                    if (jObjResponse.getInt(PARAMS.TAG_STATUS) == 1) {
                        mBinding.edtTitle.setText("")
                        mBinding.edtDescription.setText("")
                        mBinding.imvMap.setImageBitmap(null)
                        mLat = 0.0
                        mLong = 0.0
                        mMediaList!!.clear()
                        mBinding.imvMap.setVisibility(View.GONE)
                        mBinding.lltMedia.setVisibility(View.GONE)
                        mBinding.lltMedia.removeAllViews()
                        DialogUtils.showDialog(this,
                            "",
                            jObjResponse.getString(PARAMS.TAG_MESSAGE),
                            getString(R.string.ok),
                            DialogInterface.OnClickListener { dialog, which ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            })
                    } else {
                        displayMessage(jObjResponse.getString(PARAMS.TAG_MESSAGE))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onError(id: Int, message: String?) {
        displayError(message)
    }

    fun onTokenExpired(id: Int, message: String?) {
        displayLoginError(message)
    }

    fun onStart() {
        super.onStart()
        mNetworkManager.setListener(this)
    }

    fun onStop() {
        super.onStop()
        mNetworkManager.removeListener(this)
    }

    companion object {
        val TAG = AddLocationActivity::class.java.simpleName
        private const val UPDATE_INTERVAL = (3 * 1000 // 3 secs
                ).toLong()
        private const val FASTEST_INTERVAL: Long = 1000 // 1 secs
        private const val REQ_CODE_CAMERA = 998
        private const val REQ_CODE_GALLERY = 889
        private const val REQ_CODE_DOCUMENT = 679
        private const val REQ_CODE_VIDEO = 779
        private const val REQ_CODE_LOCATION = 669
        private const val INTENT_GPS_SETTINGS_FOR_CAMERA = 999
        private const val INTENT_GPS_SETTINGS_FOR_LOCATION = 9999
    }
}