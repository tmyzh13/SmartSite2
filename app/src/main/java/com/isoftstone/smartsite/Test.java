package com.isoftstone.smartsite;

import android.util.Log;

import com.isoftstone.smartsite.http.HttpPost;
import com.isoftstone.smartsite.http.muckcar.EvidencePhotoBean;
import com.isoftstone.smartsite.http.muckcar.UpdatePhotoInfoBean;
import com.isoftstone.smartsite.http.pageable.PageableBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBean;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanBeanPage;
import com.isoftstone.smartsite.http.patrolplan.PatrolPlanCommitBean;
import com.isoftstone.smartsite.http.patroluser.UserTrackBean;
import com.isoftstone.smartsite.http.user.SimpleUserBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/18.
 */

public class Test {
    public static void otTest(final String jpushId) {
        final HttpPost mHttpPost = new HttpPost();
        new Thread() {
            @Override
            public void run() {
            //mHttpPost.login("admin", "bmeB4000", jpushId);
            //mHttpPost.onePMDevicesDataListPage("","0","","",new PageableBean());
            //mHttpPost.getDevicesListPage("1","","1","",new PageableBean());
            //mHttpPost.getMessagePage("", "", "", "", new PageableBean());

            /* String day = mHttpPost.carchMonthlyComparison("29","2017-10","1").getBeforeMonth().get(4).getPushTimeOneDay();
             Log.i("test",day);
             */
            /*
            int size = mHttpPost.getWeatherConditionDay("29","2017-10").size();
			Log.i("test",size+" size ");
			*/


			/*
			mHttpPost.onePMDevicesDataList("[1,2]","0","2017-10-01 00:00:00","2017-10-11 00:00:00");
			 */


			/*
			int size = mHttpPost.getOneDevicesHistoryData("1").size();
			Log.i("test",size+" getOneDevicesHistoryData size ");
			*/


			/*
			int size =  mHttpPost.onePMDevices24Data("2","2017-10-10 10:10:10").size();
            Log.i("test",size+" size ");
            */

			/*
			int size = mHttpPost.getDevices("","","","").size();
			Log.i("test",size+" size ");
             */


			/*
			mHttpPost.readMessage("7");
			int size = mHttpPost.getMessage("","","","1").size();
			Log.i("test",size+" size ");
			*/

            /*
            String str = mHttpPost.getWeatherLive("47","2017-10").getDataTrend().get(1).getPm10();
			Log.i("test","getWeatherLive  ---------"+str);
			*/


			/*
			PatrolBean bean = new PatrolBean();
			bean.setAddress("武汉大软件元");
			bean.setCompany("wuanhan  wxa");
            mHttpPost.addPatrolReport(bean);
            */


            /*
            String address = mHttpPost.getPatrolReport("76").getAddress();
			Log.i("test","address  ---------"+address);
			*/



			/*ReportBean bean = new ReportBean();
			bean.setName("我是一个");
            bean.setContent("<p>一切正常-----------------</p>");
			bean.setCreator("我是一个");
			bean.setDate("2017-10-30 14:22:44");
			PatrolBean patrol = new PatrolBean();
			patrol.setId(27);
			bean.setPatrol(patrol);
			bean.setCategory(2);
			bean.setPatrolUser("马化腾马化腾");
			bean.setPatrolDateEnd("2017-10-30 14:23");
			bean.setPatrolDateStart("2017-10-29 14:23");
			mHttpPost.addPatrolVisit(bean);*/


                //mHttpPost.getPatrolReportList(3);





			/*
			mHttpPost.getMobileHomeData();
			 */

                //mHttpPost.imageUpload("/storage/emulated/0/test.png",39);
                //mHttpPost.reportFileUpload("/storage/emulated/0/k.log",39);

            /*UserBean user_1 = new UserBean();
			user_1.setId(1l);
			user_1.setAccount("admin");
			user_1.setPassword("bmeB4000");
			user_1.setName("isoftstone");

			mHttpPost.userUpdate(user_1);
            */

			/*String name = mHttpPost.getLoginUser().getName();
			Log.i("test","name  ---------"+name);
            */

                //mHttpPost.downloadUserImage("img\\logo.png");
                //mHttpPost.downloadReportFile(1,"img\\logo.png");

			/*Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/test.png");
			mHttpPost.userImageUpload(bitmap,Bitmap.CompressFormat.PNG);
            */

                //mHttpPost.getDictionaryList("zh",2);

			/*ArrayList<String> addresslist = mHttpPost.getPatrolAddress();
			for (String str:addresslist){
				Log.i("Test",str);
			}*/


			/*ArrayList<DictionaryBean> list = mHttpPost.getDictionaryList("zh");
			for (DictionaryBean str:list){
				Log.i("Test",str.getContent()+" "+str.getValue());
			}*/

			/*

			 */
                //mHttpPost.getDayFlow("2017-11-17","1","2017-11",1);

		    /*
		    mHttpPost.getArchMonthFlow("2017-11-17","2017-11",4L,1);
		    */
                //long[] ar = {4l,7l};
                //mHttpPost.getAlarmData("2017-11-17","2017-11",ar,1);

                //mHttpPost.recForMobile("鄂A0001",1);

              /*PageableBean pageableBean = new PageableBean();
              pageableBean.setPage(1+"");
              mHttpPost.getUnRecList("",pageableBean);*/

              /*PageableBean pageableBean = new PageableBean();
              String test = mHttpPost.getTrackList("",pageableBean).toString();
              Log.e("test","test "+test);*/

                //mHttpPost.getPhontoList("鄂A46F52","video","2017-11-17","");

              /*
              PageableBean pageableBean = new PageableBean();
              String test = mHttpPost.getEvidencePhotoList("鄂AV785B",pageableBean).toString();
              Log.e("test","test "+test);
              */

                //mHttpPost.getEvidenceDateList("鄂AV785B");

                //mHttpPost.getMapMarkers("鄂AV785B","2017-11-15");

              /*
              ArrayList list = new ArrayList();
              list.add("/storage/emulated/0/test.png");
              list.add("/storage/emulated/0/test.png");
              mHttpPost.uploadPhotos(list);
              */


              /*mHttpPost.getLoginUser();
              UpdatePhotoInfoBean evidencePhotoBean = new UpdatePhotoInfoBean();
              evidencePhotoBean.setAddr("湖北武汉");
              evidencePhotoBean.setLicence("鄂AV785B");
              evidencePhotoBean.setPhotoSrc("upload/track/images/20171117/20171117143934776.jpg");
              evidencePhotoBean.setTakePhotoTime("2017-11-18 11:11:11");
              SimpleUserBean simpleUserBean = new SimpleUserBean();
              simpleUserBean.setId(HttpPost.mLoginBean.getmUserBean().getLoginUser().getId());
              evidencePhotoBean.setTakePhoroUser(simpleUserBean);
              mHttpPost.addPhoto(evidencePhotoBean);*/


                //ArrayList<UserTrackBean> list =  mHttpPost.getUserTrack();
                /*
                UserTrackBean userTrackBean = new UserTrackBean();
                userTrackBean.setUserId(1);
                userTrackBean.setTaskId(1590);
                mHttpPost.findByUserIdAndTaskId(userTrackBean);
                */



              /*
              PatrolPlanBean patrolPlanBean = new PatrolPlanBean();
              PageableBean pageableBean = new PageableBean();
              PatrolPlanBeanPage patrolPlanBeanPage = mHttpPost.getPlanPaging(patrolPlanBean,pageableBean);
              */


             /*
              mHttpPost.planThrough(patrolPlanBeanPage.getContent().get(0));
              mHttpPost.planRefuse(patrolPlanBeanPage.getContent().get(0));
              */


              /*mHttpPost.getLoginUser();
              PatrolPlanCommitBean patrolPlanBean = new PatrolPlanCommitBean();
              patrolPlanBean.setTaskTimeStart("2017-11-19 00:00");
              patrolPlanBean.setTaskTimeEnd("2017-11-25 23:59");
              SimpleUserBean simpleUserBean = new SimpleUserBean();
              simpleUserBean.setId(HttpPost.mLoginBean.getmUserBean().getLoginUser().getId());
              patrolPlanBean.setCreator(simpleUserBean);
              mHttpPost.patrolPlanCommit(patrolPlanBean);*/

              /*PatrolTaskBeanPage patrolTaskBeanPage = mHttpPost.getPatrolTaskList(1,"","","","",new PageableBean());
              PatrolTaskBean patrolTaskBean = patrolTaskBeanPage.getContent().get(0);
              mHttpPost.patrolTaskSave(patrolTaskBean);*/

                //mHttpPost.patrolTaskFindOne(64);

                //mHttpPost.updateTaskStart(64,"巡查任务20171117");

                //mHttpPost.executeTask(64,"巡查任务20171117");

                //mHttpPost.updatePatrolPositionStatus(95,"0101");

                //mHttpPost.userTrack(1,64,114.504424,30.477807);

                //mHttpPost.queryPendingPlan();

                //mHttpPost.feedback(1,"ceshi");

                //mHttpPost.findUserAll();

                //mHttpPost.getCompanyNameByid(1);

                //mHttpPost.getPatrolReportData("2017-11");
                //mHttpPost.getDepartmentUserTaskData("2017-11",1+"");
                //mHttpPost.getDepartmentMonthDat("2017-11",1+"");
                //String[] id = {"1","2"};
                //mHttpPost.getDepartmentsMonthTasks("2017-11",id);
                //mHttpPost.getDepartmentReport("2017-11",id);

            /*PageableBean pageableBean = new PageableBean();
            mHttpPost.getPatrolTaskListAll("", "", "", "0", "", "", "", pageableBean);
            */
                //mHttpPost.getBeforeNMessageList();

            }
        }.start();
    }
}
