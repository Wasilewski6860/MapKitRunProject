package com.example.mapkitproject.presentation.run_fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapkitproject.MainViewModel
import com.example.mapkitproject.R
import com.example.mapkitproject.databinding.FragmentRunBinding
import com.example.mapkitproject.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.mapkitproject.other.TrackingUtility
import com.example.mapkitproject.presentation.run_fragment.adapter.RunAdapter

import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel

class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var runAdapter: RunAdapter

    private var _binding: FragmentRunBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRunBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setupRecyclerView()

        when(viewModel.sortType) {
            com.example.mapkitproject.other.SortType.DATE -> binding.spFilter.setSelection(0)
            com.example.mapkitproject.other.SortType.RUNNING_TIME -> binding.spFilter.setSelection(1)
            com.example.mapkitproject.other.SortType.DISTANCE -> binding.spFilter.setSelection(2)
            com.example.mapkitproject.other.SortType.AVG_SPEED -> binding.spFilter.setSelection(3)
            com.example.mapkitproject.other.SortType.CALORIES_BURNED -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.sortRuns(com.example.mapkitproject.other.SortType.DATE)
                    1 -> viewModel.sortRuns(com.example.mapkitproject.other.SortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(com.example.mapkitproject.other.SortType.DISTANCE)
                    3 -> viewModel.sortRuns(com.example.mapkitproject.other.SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(com.example.mapkitproject.other.SortType.CALORIES_BURNED)
                }
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun setupRecyclerView() = binding.rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}