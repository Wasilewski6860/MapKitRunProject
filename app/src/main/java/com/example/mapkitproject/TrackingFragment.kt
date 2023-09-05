package com.example.mapkitproject

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mapkitproject.databinding.FragmentTrackingBinding
import com.example.mapkitproject.domain.models.Run
import com.example.mapkitproject.domain.services.Polyline
import com.example.mapkitproject.domain.services.TrackingService
import com.example.mapkitproject.other.Constants.ACTION_PAUSE_SERVICE
import com.example.mapkitproject.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.mapkitproject.other.Constants.ACTION_STOP_SERVICE
import com.example.mapkitproject.other.Constants.MAP_ZOOM
import com.example.mapkitproject.other.Constants.POLYLINE_COLOR
import com.example.mapkitproject.other.Constants.POLYLINE_WIDTH
import com.example.mapkitproject.other.TrackingUtility
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Calendar
import kotlin.math.round


class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModel()

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!


    private var curTimeInMillis = 0L

    private var menu: Menu? = null

    var weight = 80f



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        _binding = FragmentTrackingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(requireContext());

        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }

        binding.btnFinishRun.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
        }

        addAllPolylines()

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints.addAll(it)


            Log.d("POINTS",pathPoints.size.toString())
            addLatestPolyline()
            moveCameraToUser()
//            zoomToSeeWholeTrack()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.tvTimer.text = formattedTime
        })
    }

    private fun toggleRun() {
        if(isTracking) {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_tracking_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(curTimeInMillis > 0L) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.miCancelTracking -> {
                showCancelTrackingDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                stopRun()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        } else {
            binding.btnToggleRun.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {


            binding.mapView.map?.move(
                CameraPosition(
                    Point(pathPoints.last().last().latitude,pathPoints.last().last().longitude), MAP_ZOOM,0.0f,0.0f
                ),
                Animation(
                    Animation.Type.SMOOTH,1f
                ),
                null
            )
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        var northeast = bounds.build().northeast
        var southwest = bounds.build().southwest

        var boundingBox = BoundingBox(Point(southwest.latitude,southwest.longitude), Point(northeast.latitude,northeast.longitude)) // getting BoundingBox between two points
        var cameraPosition = binding.mapView.map?.cameraPosition(boundingBox) // getting cameraPosition
        var target = cameraPosition?.target ?: Point(pathPoints.last().last().latitude,pathPoints.last().last().longitude)
        cameraPosition = CameraPosition(target, (cameraPosition?.zoom ?: MAP_ZOOM) - 0.8f,
            cameraPosition?.azimuth ?: 0.0F, cameraPosition?.tilt ?: 0.0F) // Zoom 80%
        binding.mapView.map?.move(cameraPosition, Animation(Animation.Type.SMOOTH, 0f), null) // move camera

//        map?.moveCamera(
//            CameraUpdateFactory.newLatLngBounds(
//                bounds.build(),
//                binding.mapView.width,
//                binding.mapView.height,
//                ( binding.mapView.height * 0.05f).toInt()
//            )
//        )
    }

    private fun endRunAndSaveToDb() {

        val enabled: Boolean = binding.mapView.isDrawingCacheEnabled()

//        binding.mapView.setDrawingCacheEnabled(true)
//
//        // this is the important code :)
//        // Without it the view will have a dimension of 0,0 and the bitmap will be null
//
//        // this is the important code :)
//        // Without it the view will have a dimension of 0,0 and the bitmap will be null
//        binding.mapView.measure(
//            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
//        )
//        binding.mapView.layout(0, 0, binding.mapView.getMeasuredWidth(), binding.mapView.getMeasuredHeight())
//        //Forces the drawing cache to be built if the drawing cache is invalid.
//        //Forces the drawing cache to be built if the drawing cache is invalid.
//        binding.mapView.buildDrawingCache(true)
//        val b: Bitmap = Bitmap.createBitmap(binding.mapView.getDrawingCache())
//        binding.mapView.setDrawingCacheEnabled(false) // clear drawing cache

        val bitmap = getScreenShotFromView(binding.fragmentTrackingCl)
        var uri =bitmap?.let {
            saveMediaToStorage(it)
        }





        var distanceInMeters = 0
        for(polyline in pathPoints) {
            distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
        }
        val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
        val dateTimestamp = Calendar.getInstance().timeInMillis
        val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
        val run = Run(0,
            uri.toString(), dateTimestamp, avgSpeed, distanceInMeters, curTimeInMillis, caloriesBurned)
        viewModel.insertRun(run)
        Snackbar.make(
            requireActivity().findViewById(R.id.rootView),
            "Run saved successfully",
            Snackbar.LENGTH_LONG
        ).show()
        stopRun()


    }

    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            var points = mutableListOf<Point>()
            for (latlng in polyline){
                points.add(Point(latlng.latitude,latlng.longitude))
            }
            val polyline = com.yandex.mapkit.geometry.Polyline(points)
            val polylineObject = binding.mapView.map.mapObjects.addPolyline(polyline)
            polylineObject.apply {
                strokeWidth = 5f
                setStrokeColor(ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black))
                outlineWidth = 1f
                outlineColor = ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black)
            }

        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            var prelastPoint = Point(preLastLatLng.latitude,preLastLatLng.longitude)
            var lastPoint = Point(lastLatLng.latitude,lastLatLng.longitude)
            var points = listOf<Point>(prelastPoint,lastPoint)
            val polyline = com.yandex.mapkit.geometry.Polyline(points)
            val polylineObject = binding.mapView.map.mapObjects.addPolyline(polyline)
            polylineObject.apply {
                strokeWidth = 5f
                setStrokeColor(ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black))
                outlineWidth = 1f
                outlineColor = ContextCompat.getColor(this@TrackingFragment.requireContext(), com.google.android.material.R.color.m3_ref_palette_black)
            }
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    private fun captureScreen() {
        binding.mapView.setDrawingCacheEnabled(true);
        var bmp: Bitmap = Bitmap.createBitmap(binding.mapView.getDrawingCache())
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        // create a bitmap object
        var screenshot: Bitmap? = null
        try {
            // inflate screenshot object
            // with Bitmap.createBitmap it
            // requires three parameters
            // width and height of the view and
            // the background color
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            // Now draw this bitmap on a canvas
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        // return the bitmap
        return screenshot
    }

    private fun saveMediaToStorage(bitmap: Bitmap) : String {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        // Output stream
        var fos: OutputStream? = null
        var stringUri = ""

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.requireActivity().contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                stringUri = imageUri.toString()

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this.requireContext() , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
        }

        return stringUri
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onLowMemory() {
        super.onLowMemory()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }
}