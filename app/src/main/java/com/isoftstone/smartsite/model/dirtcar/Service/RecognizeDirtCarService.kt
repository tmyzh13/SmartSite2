package com.isoftstone.smartsite.model.dirtcar.Service

import android.app.DownloadManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.AsyncTask
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.isoftstone.smartsite.common.App
import com.isoftstone.smartsite.http.HttpPost
import com.isoftstone.smartsite.http.muckcar.BayonetGrabInfoBean
import com.isoftstone.smartsite.http.pageable.PageableBean
import com.isoftstone.smartsite.model.dirtcar.activity.RecognizeDirtCarActivity
import com.isoftstone.smartsite.utils.SharedPreferencesUtils
import java.io.File
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Created by yanyongjun on 2017/11/22.
 */
open class RecognizeDirtCarService : Service() {
    val TAG = "RecognizeDirtCarService"
    var mHttpPost = HttpPost()
    var mDataList = ArrayList<BayonetGrabInfoBean>()
    open var mHandler = Handler()
    var mDownloadObser: downloadObserver? = null

    override fun onCreate() {
        Log.i(TAG, "yanlog onCreate")
        mDownloadObser = downloadObserver(this)
        //contentResolver.registerContentObserver(Uri.parse("content://downloads/my_downloads"), true,
           //     mDownloadObser)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "yanlog onStartCommand")

        if(SharedPreferencesUtils.getReceiveNotice(App.getAppContext())) {
            var sync = intent?.getBooleanExtra("sync", true)
            Log.e(TAG, "yanlog sync:" + sync)
            if ((if (sync == null) true else sync) && mDataList.size == 0) {
                queryData()
            } else {
                popDialog()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

    fun queryData() {
        var query = object : AsyncTask<Void, Void, Boolean>() {

            override fun doInBackground(vararg params: Void?): Boolean {
                try {
                    var pageBean = PageableBean()
                    pageBean.page = "0"
                    var list = mHttpPost.getUnRecList("", pageBean)
                    Log.e(TAG, "yanlog queryData:" + list.size)
                    if (list != null && list.content != null) {
                        Log.e(TAG,"yanlog list.rawRecords size:"+list.content.size)
                        mDataList.clear()
                        mDataList.addAll(list.content)
                        // mDataList.addAll(null)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }

            override fun onPostExecute(result: Boolean?) {
                popDialog()
                super.onPostExecute(result)
            }
        }
        query.execute()

    }

    override fun onDestroy() {
        Log.i(TAG, "yanlog onDestroy")
        //contentResolver.unregisterContentObserver(mDownloadObser)
        super.onDestroy()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    fun popDialog() {
        var query = object : AsyncTask<Void, Void, Intent?>() {

            override fun doInBackground(vararg params: Void?): Intent? {
                if (mDataList.size > 0) {
                    var data = mDataList.get(mDataList.size - 1)
                    mDataList.remove(data)
                    var gson = Gson()
                    var dataGson = gson.toJson(data)
                    var i = Intent(this@RecognizeDirtCarService, RecognizeDirtCarActivity::class.java)
                    i.putExtra("data", dataGson)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    Log.e(TAG,"yanlog dataGson:"+dataGson)
                    return i
//                    var juedui = mHttpPost.getReportPath(0, data.imgList)
//                    Log.e(TAG,"yanlog juedui:"+juedui);
//                    i.putExtra("path", juedui)
//                    if (File(juedui).exists()) {
//                        return i
//                    }
//                    var downloadId = mHttpPost.downloadReportFile(0, data.imgList)
//                    mDownloadObser?.addIntent(downloadId,i)
                }
                return null
            }

            override fun onPostExecute(result: Intent?) {
                if (result != null) {
                    startActivity(result)
                }
                super.onPostExecute(result)
            }
        }
        query.execute()
    }

    class downloadObserver(context: Context) : ContentObserver(Handler()) {
        var mContext = context
        var mDownloadManager: DownloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        var mMap = HashMap<Long, Intent>()

        /**
         * @param downloadId 绝对路径
         * @param adapter
         * @param oPath      相对路径
         */
        @Synchronized
        fun addIntent(downloadId: Long, intent: Intent) {
            mMap.put(downloadId, intent)
        }

        override fun onChange(selfChange: Boolean, uri: Uri) {
            try {
                Log.e("downloadObserver", "yanlog onChange:" + uri)
                val list = uri.pathSegments
                val uriId = java.lang.Long.parseLong(list[list.size - 1])
                if (mDownloadManager.getUriForDownloadedFile(uriId) != null) {
                    var i = mMap.get(uriId)
                    mContext.startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            super.onChange(selfChange, uri)
        }

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
        }
    }


}