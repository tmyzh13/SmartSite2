package com.isoftstone.smartsite.utils;

import android.util.Log;

import com.isoftstone.smartsite.http.user.BaseUserBean;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-25.
 */

public class ZhongWen2PinYinUtils {
    public static String getPinyin(String str) throws Exception {
        if (str== null || str.length()==0) {
            return "";
        }
        char[] t1 = null;
        t1 = str.toCharArray();
        String[] t2 = new String[t1.length];
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);

        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    // 将汉字的几种全拼都存到t2数组中
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    // 如果不是汉字字符，直接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            throw e;
        }
        return t4;
    }

    /**
     * 汉字转简拼
     * @param str
     * @return String
     */
    public static String getPinYinHeadChar(String str) {
        String convert = "";
        if (str== null || str.length()==0) {
            return convert;
        }
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }

    /**
     * 汉字转简拼
     * @param str
     * @return String
     */
    public static String getPinYinFirstHeadChar(String str) {
        String convert = "";
        if (str== null || str.length()==0) {
            return convert;
        }
        char word = str.charAt(0);
        // 提取汉字的首字母
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
        if (pinyinArray != null) {
            convert += pinyinArray[0].charAt(0);
        } else {
            convert += word;
        }
        return convert.toUpperCase();
    }

    public static ArrayList<BaseUserBean> getAscendingUser(ArrayList<BaseUserBean> list_userBeans) {
//        int left = 0;                            // 初始化边界
//        int right = list_userBeans.size() - 1;
//        int lhs_ascii;
//        int rhs_ascii;
//        while (left < right)
//        {
//            for (int i = left; i < right; i++)   // 前半轮,将最大元素放到后面
//            {
//                lhs_ascii = getPinYinFirstHeadChar(list_userBeans.get(i).getName()).toUpperCase().charAt(0);
//                rhs_ascii = getPinYinFirstHeadChar(list_userBeans.get(i + 1).getName()).toUpperCase().charAt(0);
//                if (lhs_ascii > rhs_ascii)
//                {
//                    Swap(list_userBeans, i, i + 1);
//                }
//            }
//            right--;
//            for (int i = right; i > left; i--)   // 后半轮,将最小元素放到前面
//            {
//                lhs_ascii = getPinYinFirstHeadChar(list_userBeans.get(i - 1).getName()).toUpperCase().charAt(0);
//                rhs_ascii = getPinYinFirstHeadChar(list_userBeans.get(i).getName()).toUpperCase().charAt(0);
//                if (lhs_ascii > rhs_ascii)
//                {
//                    Swap(list_userBeans, i, i + 1);
//                }
//            }
//            left++;
//        }


        /*冒泡排序法*/
        for (int j = 0; j < list_userBeans.size() - 1; j++)
        {
            for (int i = 0; i < list_userBeans.size() - 1 - j; i++)
            {
                if (getPinYinFirstHeadChar(list_userBeans.get(i).getName()).toUpperCase().charAt(0) > getPinYinFirstHeadChar(list_userBeans.get(i + 1).getName()).toUpperCase().charAt(0))            // 如果条件改成A[i] >= A[i + 1],则变为不稳定的排序算法
                {
                    Swap(list_userBeans, i, i + 1);
                }
            }
        }

        return list_userBeans;
    }

    public static void Swap(ArrayList<BaseUserBean> list_userBean, int i, int j){

        Log.e("ZhongWenPaiXu","Swap前：" + list_userBean.get(i).getName() + ", " + list_userBean.get(j).getName());
        BaseUserBean userBean = new BaseUserBean();
        userBean.setAccount(list_userBean.get(i).getAccount());
        userBean.setAccountType(list_userBean.get(i).getAccountType());
        userBean.setAddress(list_userBean.get(i).getAddress());
        userBean.setCreateTime(list_userBean.get(i).getCreateTime());
        userBean.setCreator(list_userBean.get(i).getCreator());
        userBean.setDelFlag(list_userBean.get(i).getDelFlag());
        userBean.setDepartmentId(list_userBean.get(i).getDepartmentId());
        userBean.setDescription(list_userBean.get(i).getDescription());
        userBean.setEmail(list_userBean.get(i).getEmail());
        userBean.setEmployeeCode(list_userBean.get(i).getEmployeeCode());
        userBean.setFax(list_userBean.get(i).getFax());
        userBean.setLocked(list_userBean.get(i).getLocked());
        userBean.setId(list_userBean.get(i).getId());
        userBean.setImageData(list_userBean.get(i).getImageData());
        userBean.setName(list_userBean.get(i).getName());
        userBean.setPassword(list_userBean.get(i).getPassword());
        userBean.setResetPwd(list_userBean.get(i).getResetPwd());
        userBean.setRegisterId(list_userBean.get(i).getRegisterId());
        userBean.setSex(list_userBean.get(i).getSex());
        userBean.setTelephone(list_userBean.get(i).getTelephone());

        list_userBean.get(i).setAccount(list_userBean.get(j).getAccount());
        list_userBean.get(i).setAccountType(list_userBean.get(j).getAccountType());
        list_userBean.get(i).setAddress(list_userBean.get(j).getAddress());
        list_userBean.get(i).setCreateTime(list_userBean.get(j).getCreateTime());
        list_userBean.get(i).setCreator(list_userBean.get(j).getCreator());
        list_userBean.get(i).setDelFlag(list_userBean.get(j).getDelFlag());
        list_userBean.get(i).setDepartmentId(list_userBean.get(j).getDepartmentId());
        list_userBean.get(i).setDescription(list_userBean.get(j).getDescription());
        list_userBean.get(i).setEmail(list_userBean.get(j).getEmail());
        list_userBean.get(i).setEmployeeCode(list_userBean.get(j).getEmployeeCode());
        list_userBean.get(i).setFax(list_userBean.get(j).getFax());
        list_userBean.get(i).setLocked(list_userBean.get(j).getLocked());
        list_userBean.get(i).setId(list_userBean.get(j).getId());
        list_userBean.get(i).setImageData(list_userBean.get(j).getImageData());
        list_userBean.get(i).setName(list_userBean.get(j).getName());
        list_userBean.get(i).setPassword(list_userBean.get(j).getPassword());
        list_userBean.get(i).setResetPwd(list_userBean.get(j).getResetPwd());
        list_userBean.get(i).setRegisterId(list_userBean.get(j).getRegisterId());
        list_userBean.get(i).setSex(list_userBean.get(j).getSex());
        list_userBean.get(i).setTelephone(list_userBean.get(j).getTelephone());

        list_userBean.get(j).setAccount(userBean.getAccount());
        list_userBean.get(j).setAccountType(userBean.getAccountType());
        list_userBean.get(j).setAddress(userBean.getAddress());
        list_userBean.get(j).setCreateTime(userBean.getCreateTime());
        list_userBean.get(j).setCreator(userBean.getCreator());
        list_userBean.get(j).setDelFlag(userBean.getDelFlag());
        list_userBean.get(j).setDepartmentId(userBean.getDepartmentId());
        list_userBean.get(j).setDescription(userBean.getDescription());
        list_userBean.get(j).setEmail(userBean.getEmail());
        list_userBean.get(j).setEmployeeCode(userBean.getEmployeeCode());
        list_userBean.get(j).setFax(userBean.getFax());
        list_userBean.get(j).setLocked(userBean.getLocked());
        list_userBean.get(j).setId(userBean.getId());
        list_userBean.get(j).setImageData(userBean.getImageData());
        list_userBean.get(j).setName(userBean.getName());
        list_userBean.get(j).setPassword(userBean.getPassword());
        list_userBean.get(j).setResetPwd(userBean.getResetPwd());
        list_userBean.get(j).setRegisterId(userBean.getRegisterId());
        list_userBean.get(j).setSex(userBean.getSex());
        list_userBean.get(j).setTelephone(userBean.getTelephone());
        Log.e("ZhongWenPaiXu","Swap后：" + list_userBean.get(i).getName() + ", " + list_userBean.get(j).getName());
    }
}
