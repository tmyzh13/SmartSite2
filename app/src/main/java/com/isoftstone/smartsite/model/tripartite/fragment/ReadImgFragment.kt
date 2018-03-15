package com.isoftstone.smartsite.model.tripartite.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.isoftstone.smartsite.R
import com.isoftstone.smartsite.base.BaseFragment
import com.isoftstone.smartsite.utils.ImageUtils
import kotlinx.android.synthetic.main.fragment_read_img.*
import java.io.File

/**
 * Created by yanyongjun on 2017/12/23.
 */
class ReadImgFragment : BaseFragment() {
    var path = ""
    var uri = ""

    override fun getLayoutRes(): Int {
        Log.e(TAG, "yanlog getLayoutRes")
        return R.layout.fragment_read_img
    }

    override fun afterCreated(savedInstanceState: Bundle?) {
    }

    public fun setData(path: String, uri: String) {
        Log.e(TAG, "yanlog setData path:" + path + "uri:" + uri)
        this.path = path
        this.uri = uri
    }

    override fun onResume() {
        super.onResume()
        if (File(path).exists() && false) {
            ImageUtils.loadImageViewDiskCache(activity, path, img)
        } else {
            ImageUtils.loadImageWithPlaceHolder(activity, img, uri)
        }
    }
}