package com.isoftstone.smartsite.model.tripartite.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.google.gson.Gson
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseActivity
import com.isoftstone.smartsite.http.HttpPost
import com.isoftstone.smartsite.http.patrolreport.ReportBean
import com.isoftstone.smartsite.model.tripartite.fragment.ReadImgFragment
import com.isoftstone.smartsite.utils.FilesUtils
import kotlinx.android.synthetic.main.activity_read_msg.*

/**
 * Created by yanyongjun on 2017/12/23.
 */
open class ReadImgActivity : BaseActivity() {
    var mHttpPost = HttpPost()
    override fun getLayoutRes(): Int {
        return R.layout.activity_read_msg
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
        var reportData = intent.getStringExtra("reportBean")
        var gson = Gson()
        //var data = gson.fromJson<ReportBean::class.java>(reportData)
        var data = gson.fromJson(reportData, ReportBean::class.java) as ReportBean
        initViewPager(data)
    }

    fun initViewPager(data: ReportBean) {
        var position = intent.getIntExtra("position",0)
        try {
            var mFragList = ArrayList<ReadImgFragment>()
            var adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return mFragList[position]
                }

                override fun getCount(): Int {
                    return mFragList.size
                }
            }

            var reportFiles = data.reportFiles
            var tempLoc = 0
            var curPostion = position
            for (absPath in reportFiles) {
                val formatPath = FilesUtils.getFormatString(absPath)
                if (TripartiteActivity.mImageList.contains(formatPath)) {
                    val filePath = mHttpPost.getReportPath(data.id, absPath)
                    val uri = mHttpPost.getFileUrl(absPath)
                    var fragment = ReadImgFragment()
                    fragment.setData(filePath, uri)
                    mFragList.add(fragment)
                }else{
                    if(tempLoc < position){
                        curPostion--
                    }
                }
                tempLoc++
            }
            view_pager.adapter = adapter

            view_pager.currentItem = curPostion
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}