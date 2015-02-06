package com.example.testmonth;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnPageChangeListener{
	
	private int mDayNumber;
	private int mRawNumber;
	private GregorianCalendar currentGc;
	private GregorianCalendar mStartCalendar;
	
	private Integer lastposition = 6000;
	
	private TextView month;
	
	private int mDAYS_OF_SOME_MONTH;      //某月的天数
	private int mDAY_OF_WEEK;        //某月的第一天是星期几
	private int mDaysOfLastMonth;

	private Activity mActivity;
	
	private ViewPager viewPager;
	private FragmentPagerAdapter	Calendaradapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_month);
		
		currentGc  = new GregorianCalendar();
		currentGc.setTimeInMillis(System.currentTimeMillis());
		
		this.mStartCalendar = (GregorianCalendar) getweek(currentGc);

		mActivity = MainActivity.this;
		
		LinearLayout mWeek_Layout = (LinearLayout)findViewById(R.id.week_layout);
		View  mWeeksView = LayoutInflater.from(mActivity).inflate(R.layout.weeks_layout, null);
		mWeek_Layout.addView(mWeeksView);
		
		month = (TextView) findViewById(R.id.month);
		month.setText(currentGc.get(Calendar.YEAR)+"年"+(currentGc.get(Calendar.MONTH)+1)+"月"+currentGc.get(Calendar.DAY_OF_MONTH)+"日");
		
		viewPager = (ViewPager) findViewById(R.id.calendar_viewpager);
		Calendaradapter = new FragmentPagerAdapter(mActivity, getSupportFragmentManager());
		//设置viewpager两个页卡间的间隙
		viewPager.setPageMargin(20);
		viewPager.setAdapter(Calendaradapter);
		viewPager.setCurrentItem(6000);
		viewPager.setOnPageChangeListener(this);
	} 
	
	//获得月份的头一天以及最后一天，同时将事件放进list 重要算法
	private GregorianCalendar getweek(GregorianCalendar temp1) {
 		GregorianCalendar temp = (GregorianCalendar) temp1.clone();

		int y = temp.get(Calendar.YEAR);
		int m = temp.get(Calendar.MONTH);

		System.out.println("getWeek:"+y+"-"+(m+1));
		
		mDAYS_OF_SOME_MONTH = CalenDateHelper.getDaysOfMonth(temp.isLeapYear(y), m);  //某月的总天数
		mDAY_OF_WEEK = CalenDateHelper.getWeekdayOfMonth1(y, m);      //某月第一天为星期几  

		int ZhouJiKaiShi = 0;//  0代表周日
		mDaysOfLastMonth = (mDAY_OF_WEEK - ZhouJiKaiShi) < 0 ? (7 - (ZhouJiKaiShi - mDAY_OF_WEEK)) : (mDAY_OF_WEEK - ZhouJiKaiShi);

		mDAY_OF_WEEK = CalenDateHelper.getWeekdayOfMonth2(y, m,mDAYS_OF_SOME_MONTH);      //某月第最后一天为星期几

		temp.set(Calendar.DAY_OF_MONTH, 1);
		temp.add(Calendar.DAY_OF_MONTH, -mDaysOfLastMonth);

		temp.set(Calendar.HOUR, 0);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 1);

		GregorianCalendar last = (GregorianCalendar)temp.clone(); 

		int day = (mDAY_OF_WEEK - ZhouJiKaiShi) < 0 ? (ZhouJiKaiShi - mDAY_OF_WEEK) : (7 - (mDAY_OF_WEEK - ZhouJiKaiShi));

		if(mDaysOfLastMonth>0){
			last.add(Calendar.MONTH, 1);
		}

		last.set(Calendar.DAY_OF_MONTH, mDAYS_OF_SOME_MONTH);
		last.add(Calendar.DAY_OF_MONTH, day-1);

		last.set(Calendar.HOUR_OF_DAY, 23);
		last.set(Calendar.HOUR, 11);
		last.set(Calendar.MINUTE, 59);
		last.set(Calendar.SECOND, 59);
		last.set(Calendar.MILLISECOND, 999);

		int count = 1;

		GregorianCalendar t = (GregorianCalendar) temp.clone();
		
		while (t.get(Calendar.DAY_OF_MONTH)!=last.get(Calendar.DAY_OF_MONTH)||t.get(Calendar.MONTH)!=last.get(Calendar.MONTH)||t.get(Calendar.YEAR)!=last.get(Calendar.YEAR)) {
			t.add(Calendar.DAY_OF_MONTH, 1);
			count++;
		}

		mDayNumber = count;
		mRawNumber = mDayNumber / 7;
		return temp;

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		if(lastposition==position){
			
		}else if(lastposition > position){//右滑 －
			System.out.println("右－－－>左");
			currentGc.add(Calendar.MONTH, -1);
			currentGc.set(Calendar.DAY_OF_MONTH, 1);
			mStartCalendar = getweek(currentGc);
			
			month.setText(currentGc.get(Calendar.YEAR)+"年"+(currentGc.get(Calendar.MONTH)+1)+"月"+currentGc.get(Calendar.DAY_OF_MONTH)+"日");
		}else if(lastposition < position){ //左滑 ＋
			System.out.println("左－－－>右");
			currentGc.add(Calendar.MONTH, 1);
			currentGc.set(Calendar.DAY_OF_MONTH, 1);
			mStartCalendar = getweek(currentGc);
			
			month.setText(currentGc.get(Calendar.YEAR)+"年"+(currentGc.get(Calendar.MONTH)+1)+"月"+currentGc.get(Calendar.DAY_OF_MONTH)+"日");
		}
		lastposition = position;
	}

}
