package com.example.testmonth;

import java.util.Calendar;

import android.view.View;

public class CalenDateHelper {

	private static int daysOfMonth = 0;      //某月的天数
	private static int dayOfWeek = 0;        //具体某一天是星期几

	//得到某月有多少天数
	public static int getDaysOfMonth(boolean isLeapyear, int month) {

		switch (month) {
		case -1:
		case 0:
		case 2:
		case 4:
		case 6:
		case 7:
		case 9:
		case 11:
			daysOfMonth = 31;
			break;
		case 3:
		case 5:
		case 8:
		case 10:
			daysOfMonth = 30;
			break;
		case 1:
			if (isLeapyear) {
				daysOfMonth = 29;
			} else {
				daysOfMonth = 28;
			}

		}
		return daysOfMonth;
	}

	//指定某年中的某月的第一天是星期几
	public static int getWeekdayOfMonth1(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		return dayOfWeek;
	}
	

	//指定某年中的某月的最后一天是星期几
	public static int getWeekdayOfMonth2(int year, int month,int days){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, days);
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		return dayOfWeek;
	}
	
	
	public static void setWeekHeightOfMonth(boolean isCustom, View f5, View f6,
			int mRow) {
		if (isCustom) {
			if (mRow == 3) {
				f5.setVisibility(View.VISIBLE);
				f6.setVisibility(View.GONE);
			} else if (mRow == 4) {
				f5.setVisibility(View.VISIBLE);
				f6.setVisibility(View.VISIBLE);
			} else {
				f5.setVisibility(View.GONE);
				f6.setVisibility(View.GONE);
			}
		} else {
			if (mRow == 5) {
				f5.setVisibility(View.VISIBLE);
				f6.setVisibility(View.GONE);
			} else if (mRow == 6) {
				f5.setVisibility(View.VISIBLE);
				f6.setVisibility(View.VISIBLE);
			} else {
				f5.setVisibility(View.GONE);
				f6.setVisibility(View.GONE);
			}
		}
	}


}
