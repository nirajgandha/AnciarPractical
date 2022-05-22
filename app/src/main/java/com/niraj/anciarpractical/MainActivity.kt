package com.niraj.anciarpractical

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.niraj.anciarpractical.adapter.RecyclerviewAdapter
import com.niraj.anciarpractical.custom_ui.StickyHeaderItemDecorator
import com.niraj.anciarpractical.databinding.ActivityMainBinding
import com.niraj.anciarpractical.model.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mTag: String = "MainActivity"
    private val SPAN_SIZE = 2
    private var mExamReviewList: MutableList<GeneralizedModel>? = null
    private var mHeaderPositionList: ArrayList<Int> = arrayListOf()
    private var mRecyclerviewAdapter: RecyclerviewAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val jsonStringFromAsset = readJsonFileFromAssets()
        if (BuildConfig.DEBUG) {
            Log.d(mTag, "initView: $jsonStringFromAsset")
        }
        val gson = Gson()
        val jsonDataModel = gson.fromJson(jsonStringFromAsset, JsonDataModel::class.java)
        loadStickyAdapterEcho(jsonDataModel.jsonDataModelItem)

    }

    private fun loadStickyAdapterEcho(jsonDataModelItem: ArrayList<JsonDataModelItem>) {
        mExamReviewList = arrayListOf()
        mHeaderPositionList.clear()
        var headerPosition = 0
        jsonDataModelItem.forEach { it ->
            mExamReviewList?.add(ModelTypeHeader(headerPosition, it.title))
            it.imageList.forEach { url ->
                mExamReviewList?.add(ModelTypeItem(headerPosition, url))
            }
            if (!mHeaderPositionList.contains(headerPosition)) {
                mHeaderPositionList.add(headerPosition)
            }
            headerPosition = mExamReviewList!!.size
        }
        mRecyclerviewAdapter = RecyclerviewAdapter(mExamReviewList!!,mHeaderPositionList, this)
        val layoutManager = StaggeredGridLayoutManager(SPAN_SIZE, StaggeredGridLayoutManager.VERTICAL)
        binding.mainRecyclerView.layoutManager = layoutManager
        binding.mainRecyclerView.adapter = mRecyclerviewAdapter
        StickyHeaderItemDecorator(mRecyclerviewAdapter!!)
            .attachToRecyclerView(binding.mainRecyclerView)
    }

    private fun readJsonFileFromAssets(): String {
        val inputStream = assets.open("fakedata.json")
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)
        inputStream.close()
        return String(byteArray)
    }
}