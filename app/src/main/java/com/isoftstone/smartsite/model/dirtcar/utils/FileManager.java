package com.isoftstone.smartsite.model.dirtcar.utils;

/**
 * Created by zhangyinfu on 2017/11/15.
 */


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.isoftstone.smartsite/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.isoftstone.smartsite/files";
		}
	}
}
