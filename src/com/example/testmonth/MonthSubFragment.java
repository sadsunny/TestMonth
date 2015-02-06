package com.example.testmonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MonthSubFragment extends Fragment {

	private ArrayList<GregorianCalendar> mDayNumberList = new ArrayList<GregorianCalendar>();
	
	private ArrayList<RelativeLayout> mDayLayouts = new ArrayList<RelativeLayout>();
	public ArrayList<TextView> mMonthTextViews = new ArrayList<TextView>();
	public ArrayList<TextView> mMonthBgViews = new ArrayList<TextView>();
	public ArrayList<ImageView> mMonthImageViews = new ArrayList<ImageView>();
	private GregorianCalendar mStartCalendar;
	private int mDayNumber;
	private int mRawNumber;

	private Activity mActivity;

	private int mDAYS_OF_SOME_MONTH; // 某月的天数
	private int mDAY_OF_WEEK; // 某月的第一天是星期几
	private int mDaysOfLastMonth;
	
	private View dayView;
	private Integer currentMonth;//当前月
	private static GregorianCalendar currentGre;//当前时间
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		GregorianCalendar gc = (GregorianCalendar) mStartCalendar.clone();

		if (mDayNumberList != null)
			mDayNumberList.clear();

		for (int i = 0; i < mDayNumber; i++) {
			GregorianCalendar temp = (GregorianCalendar) gc.clone();
			mDayNumberList.add(temp);
			gc.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		mHandler.sendEmptyMessage(1);
	}

	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == 1) {
				if (mMonthTextViews != null && !mMonthTextViews.isEmpty()
						&& mMonthBgViews != null && !mMonthBgViews.isEmpty()
						&& mMonthImageViews != null
						&& !mMonthImageViews.isEmpty()) {

					for (int i = 0; i < mDayNumberList.size(); i++) {
						final GregorianCalendar gc = mDayNumberList.get(i);
						
						//设置当前时间背景
						if (gc.get(Calendar.MONTH) + 1 != currentMonth) {

						} else {
							if (gc.equals(currentGre)) {
								mMonthBgViews.get(i).setVisibility(View.VISIBLE);
								mMonthBgViews.get(i).setBackgroundColor(Color.BLACK);
								mMonthBgViews.get(i).setAlpha(0.2f);
							}
						}
						
						//设置监听事件
						mDayLayouts.get(i).setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(dayView != null){
									dayView.setBackgroundColor(Color.TRANSPARENT);
								}
								dayView = v;
								dayView.setBackgroundResource(R.drawable.click_shape);
							}
						});
					}
				}
			}
			return false;
		}
	});

	public static Fragment getFragment(Activity context, int position) {
		currentGre= new GregorianCalendar();
		currentGre.setTimeInMillis(System.currentTimeMillis());
		currentGre.add(Calendar.MONTH, position - 6000);
		if(position != 6000){
			currentGre.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		MonthSubFragment subFragment = new MonthSubFragment();
		Bundle args = new Bundle();
		args.putSerializable("calen", currentGre);
		subFragment.setArguments(args);
		return subFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (mActivity == null) {
			mActivity = activity;
		}
		
		GregorianCalendar temp = (GregorianCalendar) getArguments().getSerializable("calen");
		this.mStartCalendar = (GregorianCalendar) getweek(temp).clone();

		currentMonth = temp.get(Calendar.MONTH)+1;
	}

	// 获得月份的头一天以及最后一天，同时将事件放进list 重要算法
	private GregorianCalendar getweek(GregorianCalendar temp1) {
		GregorianCalendar temp = (GregorianCalendar) temp1.clone();

		int y = temp.get(Calendar.YEAR);
		int m = temp.get(Calendar.MONTH);

		mDAYS_OF_SOME_MONTH = CalenDateHelper.getDaysOfMonth(temp.isLeapYear(y), m); // 某月的总天数
		mDAY_OF_WEEK = CalenDateHelper.getWeekdayOfMonth1(y, m); // 某月第一天为星期几

		int ZhouJiKaiShi = 0;// 0代表周日
		
		mDaysOfLastMonth = (mDAY_OF_WEEK - ZhouJiKaiShi) < 0 ? (7 - (ZhouJiKaiShi - mDAY_OF_WEEK)) : (mDAY_OF_WEEK - ZhouJiKaiShi);

		mDAY_OF_WEEK = CalenDateHelper.getWeekdayOfMonth2(y, m,mDAYS_OF_SOME_MONTH); // 某月第最后一天为星期几

		temp.set(Calendar.DAY_OF_MONTH, 1);
		temp.add(Calendar.DAY_OF_MONTH, -mDaysOfLastMonth);

		temp.set(Calendar.HOUR, 0);
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 1);

		GregorianCalendar last = (GregorianCalendar) temp.clone();

		int day = (mDAY_OF_WEEK - ZhouJiKaiShi) < 0 ? (ZhouJiKaiShi - mDAY_OF_WEEK): (7 - (mDAY_OF_WEEK - ZhouJiKaiShi));

		if (mDaysOfLastMonth > 0) {
			last.add(Calendar.MONTH, 1);
		}

		last.set(Calendar.DAY_OF_MONTH, mDAYS_OF_SOME_MONTH);
		last.add(Calendar.DAY_OF_MONTH, day - 1);

		last.set(Calendar.HOUR_OF_DAY, 23);
		last.set(Calendar.HOUR, 11);
		last.set(Calendar.MINUTE, 59);
		last.set(Calendar.SECOND, 59);
		last.set(Calendar.MILLISECOND, 999);

		int count = 1;

		GregorianCalendar t = (GregorianCalendar) temp.clone();

		while (t.get(Calendar.DAY_OF_MONTH) != last.get(Calendar.DAY_OF_MONTH) || t.get(Calendar.MONTH) != last.get(Calendar.MONTH)
				|| t.get(Calendar.YEAR) != last.get(Calendar.YEAR)) {
			t.add(Calendar.DAY_OF_MONTH, 1);
			count++;
		}

		mDayNumber = count;
		mRawNumber = mDayNumber / 7;
		return temp;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(mActivity).inflate(R.layout.calendar, null);

		View subView1 = (View) v.findViewById(R.id.month_container1);
		View subView2 = (View) v.findViewById(R.id.month_container2);
		View subView3 = (View) v.findViewById(R.id.month_container3);
		View subView4 = (View) v.findViewById(R.id.month_container4);
		View subView5 = (View) v.findViewById(R.id.month_container5);
		View subView6 = (View) v.findViewById(R.id.month_container6);

		CalenDateHelper.setWeekHeightOfMonth(false, subView5, subView6,mRawNumber);
		
		initView(subView1, subView2, subView3, subView4, subView5, subView6);

		if (mMonthTextViews != null && !mMonthTextViews.isEmpty()) {
			for (int i = 0; i < mDayNumberList.size(); i++) {
				TextView ev = mMonthTextViews.get(i);
				ev.setText(mDayNumberList.get(i).get(Calendar.DAY_OF_MONTH)+ "");
				ev.setTextColor(R.color.black);
			}
			
			//上一个月数据
			for (int j = 0; j < mDaysOfLastMonth; j++) {
				TextView tv = mMonthTextViews.get(j);
				tv.setTextColor(Color.GRAY);
			}

			//下一个月的数据
			int last = mDayNumber - mDaysOfLastMonth - mDAYS_OF_SOME_MONTH;
			int size = mDayNumberList.size() - 1;
			for (int k = size; k > size - last; k--) {
				TextView tv = mMonthTextViews.get(k);
				tv.setTextColor(Color.GRAY);
			}
		}

		return v;

	}


	// 初始化View
	private void initView(View v1, View v2, View v3, View v4, View v5, View v6) {
		// 第一行-------------------------------------------------------
		RelativeLayout t11 = (RelativeLayout) v1.findViewById(R.id.month_layout1);
		TextView bgView11 = (TextView) t11.findViewById(R.id.day_bg);
		TextView textView11 = (TextView) t11.findViewById(R.id.day_txt);
		ImageView imageView11 = (ImageView) t11.findViewById(R.id.day_img);

		RelativeLayout t12 = (RelativeLayout) v1.findViewById(R.id.month_layout2);
		TextView bgView12 = (TextView) t12.findViewById(R.id.day_bg);
		TextView textView12 = (TextView) t12.findViewById(R.id.day_txt);
		ImageView imageView12 = (ImageView) t12.findViewById(R.id.day_img);

		RelativeLayout t13 = (RelativeLayout) v1.findViewById(R.id.month_layout3);
		TextView bgView13 = (TextView) t13.findViewById(R.id.day_bg);
		TextView textView13 = (TextView) t13.findViewById(R.id.day_txt);
		ImageView imageView13 = (ImageView) t13.findViewById(R.id.day_img);

		RelativeLayout t14 = (RelativeLayout) v1.findViewById(R.id.month_layout4);
		TextView bgView14 = (TextView) t14.findViewById(R.id.day_bg);
		TextView textView14 = (TextView) t14.findViewById(R.id.day_txt);
		ImageView imageView14 = (ImageView) t14.findViewById(R.id.day_img);

		RelativeLayout t15 = (RelativeLayout) v1.findViewById(R.id.month_layout5);
		TextView bgView15 = (TextView) t15.findViewById(R.id.day_bg);
		TextView textView15 = (TextView) t15.findViewById(R.id.day_txt);
		ImageView imageView15 = (ImageView) t15.findViewById(R.id.day_img);

		RelativeLayout t16 = (RelativeLayout) v1.findViewById(R.id.month_layout6);
		TextView bgView16 = (TextView) t16.findViewById(R.id.day_bg);
		TextView textView16 = (TextView) t16.findViewById(R.id.day_txt);
		ImageView imageView16 = (ImageView) t16.findViewById(R.id.day_img);

		RelativeLayout t17 = (RelativeLayout) v1.findViewById(R.id.month_layout7);
		TextView bgView17 = (TextView) t17.findViewById(R.id.day_bg);
		TextView textView17 = (TextView) t17.findViewById(R.id.day_txt);
		ImageView imageView17 = (ImageView) t17.findViewById(R.id.day_img);

		// 第二行-------------------------------------------------------
		RelativeLayout t21 = (RelativeLayout) v2.findViewById(R.id.month_layout1);
		TextView bgView21 = (TextView) t21.findViewById(R.id.day_bg);
		TextView textView21 = (TextView) t21.findViewById(R.id.day_txt);
		ImageView imageView21 = (ImageView) t21.findViewById(R.id.day_img);

		RelativeLayout t22 = (RelativeLayout) v2.findViewById(R.id.month_layout2);
		TextView bgView22 = (TextView) t22.findViewById(R.id.day_bg);
		TextView textView22 = (TextView) t22.findViewById(R.id.day_txt);
		ImageView imageView22 = (ImageView) t22.findViewById(R.id.day_img);

		RelativeLayout t23 = (RelativeLayout) v2.findViewById(R.id.month_layout3);
		TextView bgView23 = (TextView) t23.findViewById(R.id.day_bg);
		TextView textView23 = (TextView) t23.findViewById(R.id.day_txt);
		ImageView imageView23 = (ImageView) t23.findViewById(R.id.day_img);

		RelativeLayout t24 = (RelativeLayout) v2.findViewById(R.id.month_layout4);
		TextView bgView24 = (TextView) t24.findViewById(R.id.day_bg);
		TextView textView24 = (TextView) t24.findViewById(R.id.day_txt);
		ImageView imageView24 = (ImageView) t24.findViewById(R.id.day_img);

		RelativeLayout t25 = (RelativeLayout) v2.findViewById(R.id.month_layout5);
		TextView bgView25 = (TextView) t25.findViewById(R.id.day_bg);
		TextView textView25 = (TextView) t25.findViewById(R.id.day_txt);
		ImageView imageView25 = (ImageView) t25.findViewById(R.id.day_img);

		RelativeLayout t26 = (RelativeLayout) v2.findViewById(R.id.month_layout6);
		TextView bgView26 = (TextView) t26.findViewById(R.id.day_bg);
		TextView textView26 = (TextView) t26.findViewById(R.id.day_txt);
		ImageView imageView26 = (ImageView) t26.findViewById(R.id.day_img);

		RelativeLayout t27 = (RelativeLayout) v2.findViewById(R.id.month_layout7);
		TextView bgView27 = (TextView) t27.findViewById(R.id.day_bg);
		TextView textView27 = (TextView) t27.findViewById(R.id.day_txt);
		ImageView imageView27 = (ImageView) t27.findViewById(R.id.day_img);

		// 第三行-------------------------------------------------------
		RelativeLayout t31 = (RelativeLayout) v3.findViewById(R.id.month_layout1);
		TextView bgView31 = (TextView) t31.findViewById(R.id.day_bg);
		TextView textView31 = (TextView) t31.findViewById(R.id.day_txt);
		ImageView imageView31 = (ImageView) t31.findViewById(R.id.day_img);

		RelativeLayout t32 = (RelativeLayout) v3.findViewById(R.id.month_layout2);
		TextView bgView32 = (TextView) t32.findViewById(R.id.day_bg);
		TextView textView32 = (TextView) t32.findViewById(R.id.day_txt);
		ImageView imageView32 = (ImageView) t32.findViewById(R.id.day_img);

		RelativeLayout t33 = (RelativeLayout) v3.findViewById(R.id.month_layout3);
		TextView bgView33 = (TextView) t33.findViewById(R.id.day_bg);
		TextView textView33 = (TextView) t33.findViewById(R.id.day_txt);
		ImageView imageView33 = (ImageView) t33.findViewById(R.id.day_img);

		RelativeLayout t34 = (RelativeLayout) v3.findViewById(R.id.month_layout4);
		TextView bgView34 = (TextView) t34.findViewById(R.id.day_bg);
		TextView textView34 = (TextView) t34.findViewById(R.id.day_txt);
		ImageView imageView34 = (ImageView) t34.findViewById(R.id.day_img);

		RelativeLayout t35 = (RelativeLayout) v3.findViewById(R.id.month_layout5);
		TextView bgView35 = (TextView) t35.findViewById(R.id.day_bg);
		TextView textView35 = (TextView) t35.findViewById(R.id.day_txt);
		ImageView imageView35 = (ImageView) t35.findViewById(R.id.day_img);

		RelativeLayout t36 = (RelativeLayout) v3.findViewById(R.id.month_layout6);
		TextView bgView36 = (TextView) t36.findViewById(R.id.day_bg);
		TextView textView36 = (TextView) t36.findViewById(R.id.day_txt);
		ImageView imageView36 = (ImageView) t36.findViewById(R.id.day_img);

		RelativeLayout t37 = (RelativeLayout) v3.findViewById(R.id.month_layout7);
		TextView bgView37 = (TextView) t37.findViewById(R.id.day_bg);
		TextView textView37 = (TextView) t37.findViewById(R.id.day_txt);
		ImageView imageView37 = (ImageView) t37.findViewById(R.id.day_img);

		// 第四行-------------------------------------------------------
		RelativeLayout t41 = (RelativeLayout) v4.findViewById(R.id.month_layout1);
		TextView bgView41 = (TextView) t41.findViewById(R.id.day_bg);
		TextView textView41 = (TextView) t41.findViewById(R.id.day_txt);
		ImageView imageView41 = (ImageView) t41.findViewById(R.id.day_img);

		RelativeLayout t42 = (RelativeLayout) v4.findViewById(R.id.month_layout2);
		TextView bgView42 = (TextView) t42.findViewById(R.id.day_bg);
		TextView textView42 = (TextView) t42.findViewById(R.id.day_txt);
		ImageView imageView42 = (ImageView) t42.findViewById(R.id.day_img);

		RelativeLayout t43 = (RelativeLayout) v4.findViewById(R.id.month_layout3);
		TextView bgView43 = (TextView) t43.findViewById(R.id.day_bg);
		TextView textView43 = (TextView) t43.findViewById(R.id.day_txt);
		ImageView imageView43 = (ImageView) t43.findViewById(R.id.day_img);

		RelativeLayout t44 = (RelativeLayout) v4.findViewById(R.id.month_layout4);
		TextView bgView44 = (TextView) t44.findViewById(R.id.day_bg);
		TextView textView44 = (TextView) t44.findViewById(R.id.day_txt);
		ImageView imageView44 = (ImageView) t44.findViewById(R.id.day_img);

		RelativeLayout t45 = (RelativeLayout) v4.findViewById(R.id.month_layout5);
		TextView bgView45 = (TextView) t45.findViewById(R.id.day_bg);
		TextView textView45 = (TextView) t45.findViewById(R.id.day_txt);
		ImageView imageView45 = (ImageView) t45.findViewById(R.id.day_img);

		RelativeLayout t46 = (RelativeLayout) v4.findViewById(R.id.month_layout6);
		TextView bgView46 = (TextView) t46.findViewById(R.id.day_bg);
		TextView textView46 = (TextView) t46.findViewById(R.id.day_txt);
		ImageView imageView46 = (ImageView) t46.findViewById(R.id.day_img);

		RelativeLayout t47 = (RelativeLayout) v4.findViewById(R.id.month_layout7);
		TextView bgView47 = (TextView) t47.findViewById(R.id.day_bg);
		TextView textView47 = (TextView) t47.findViewById(R.id.day_txt);
		ImageView imageView47 = (ImageView) t47.findViewById(R.id.day_img);

		// 第五行-------------------------------------------------------
		RelativeLayout t51 = (RelativeLayout) v5.findViewById(R.id.month_layout1);
		TextView bgView51 = (TextView) t51.findViewById(R.id.day_bg);
		TextView textView51 = (TextView) t51.findViewById(R.id.day_txt);
		ImageView imageView51 = (ImageView) t51.findViewById(R.id.day_img);

		RelativeLayout t52 = (RelativeLayout) v5.findViewById(R.id.month_layout2);
		TextView bgView52 = (TextView) t52.findViewById(R.id.day_bg);
		TextView textView52 = (TextView) t52.findViewById(R.id.day_txt);
		ImageView imageView52 = (ImageView) t52.findViewById(R.id.day_img);

		RelativeLayout t53 = (RelativeLayout) v5.findViewById(R.id.month_layout3);
		TextView bgView53 = (TextView) t53.findViewById(R.id.day_bg);
		TextView textView53 = (TextView) t53.findViewById(R.id.day_txt);
		ImageView imageView53 = (ImageView) t53.findViewById(R.id.day_img);

		RelativeLayout t54 = (RelativeLayout) v5.findViewById(R.id.month_layout4);
		TextView bgView54 = (TextView) t54.findViewById(R.id.day_bg);
		TextView textView54 = (TextView) t54.findViewById(R.id.day_txt);
		ImageView imageView54 = (ImageView) t54.findViewById(R.id.day_img);

		RelativeLayout t55 = (RelativeLayout) v5.findViewById(R.id.month_layout5);
		TextView bgView55 = (TextView) t55.findViewById(R.id.day_bg);
		TextView textView55 = (TextView) t55.findViewById(R.id.day_txt);
		ImageView imageView55 = (ImageView) t55.findViewById(R.id.day_img);

		RelativeLayout t56 = (RelativeLayout) v5.findViewById(R.id.month_layout6);
		TextView bgView56 = (TextView) t56.findViewById(R.id.day_bg);
		TextView textView56 = (TextView) t56.findViewById(R.id.day_txt);
		ImageView imageView56 = (ImageView) t56.findViewById(R.id.day_img);

		RelativeLayout t57 = (RelativeLayout) v5.findViewById(R.id.month_layout7);
		TextView bgView57 = (TextView) t57.findViewById(R.id.day_bg);
		TextView textView57 = (TextView) t57.findViewById(R.id.day_txt);
		ImageView imageView57 = (ImageView) t57.findViewById(R.id.day_img);

		// 第六行-------------------------------------------------------
		RelativeLayout t61 = (RelativeLayout) v6.findViewById(R.id.month_layout1);
		TextView bgView61 = (TextView) t61.findViewById(R.id.day_bg);
		TextView textView61 = (TextView) t61.findViewById(R.id.day_txt);
		ImageView imageView61 = (ImageView) t61.findViewById(R.id.day_img);

		RelativeLayout t62 = (RelativeLayout) v6.findViewById(R.id.month_layout2);
		TextView bgView62 = (TextView) t62.findViewById(R.id.day_bg);
		TextView textView62 = (TextView) t62.findViewById(R.id.day_txt);
		ImageView imageView62 = (ImageView) t62.findViewById(R.id.day_img);

		RelativeLayout t63 = (RelativeLayout) v6.findViewById(R.id.month_layout3);
		TextView bgView63 = (TextView) t63.findViewById(R.id.day_bg);
		TextView textView63 = (TextView) t63.findViewById(R.id.day_txt);
		ImageView imageView63 = (ImageView) t63.findViewById(R.id.day_img);

		RelativeLayout t64 = (RelativeLayout) v6.findViewById(R.id.month_layout4);
		TextView bgView64 = (TextView) t64.findViewById(R.id.day_bg);
		TextView textView64 = (TextView) t64.findViewById(R.id.day_txt);
		ImageView imageView64 = (ImageView) t64.findViewById(R.id.day_img);

		RelativeLayout t65 = (RelativeLayout) v6.findViewById(R.id.month_layout5);
		TextView bgView65 = (TextView) t65.findViewById(R.id.day_bg);
		TextView textView65 = (TextView) t65.findViewById(R.id.day_txt);
		ImageView imageView65 = (ImageView) t65.findViewById(R.id.day_img);

		RelativeLayout t66 = (RelativeLayout) v6.findViewById(R.id.month_layout6);
		TextView bgView66 = (TextView) t66.findViewById(R.id.day_bg);
		TextView textView66 = (TextView) t66.findViewById(R.id.day_txt);
		ImageView imageView66 = (ImageView) t66.findViewById(R.id.day_img);

		RelativeLayout t67 = (RelativeLayout) v6.findViewById(R.id.month_layout7);
		TextView bgView67 = (TextView) t67.findViewById(R.id.day_bg);
		TextView textView67 = (TextView) t67.findViewById(R.id.day_txt);
		ImageView imageView67 = (ImageView) t67.findViewById(R.id.day_img);

		// 将view添加到集合中保存
		if (mMonthTextViews != null || mMonthBgViews != null || mMonthImageViews != null) {
			mMonthTextViews.clear();
			mMonthBgViews.clear();
			mMonthImageViews.clear();
			// -----------------------------------
			mDayLayouts.add(t11);
			mDayLayouts.add(t12);
			mDayLayouts.add(t13);
			mDayLayouts.add(t14);
			mDayLayouts.add(t15);
			mDayLayouts.add(t16);
			mDayLayouts.add(t17);
			
			mMonthTextViews.add(textView11);
			mMonthTextViews.add(textView12);
			mMonthTextViews.add(textView13);
			mMonthTextViews.add(textView14);
			mMonthTextViews.add(textView15);
			mMonthTextViews.add(textView16);
			mMonthTextViews.add(textView17);

			mMonthBgViews.add(bgView11);
			mMonthBgViews.add(bgView12);
			mMonthBgViews.add(bgView13);
			mMonthBgViews.add(bgView14);
			mMonthBgViews.add(bgView15);
			mMonthBgViews.add(bgView16);
			mMonthBgViews.add(bgView17);

			mMonthImageViews.add(imageView11);
			mMonthImageViews.add(imageView12);
			mMonthImageViews.add(imageView13);
			mMonthImageViews.add(imageView14);
			mMonthImageViews.add(imageView15);
			mMonthImageViews.add(imageView16);
			mMonthImageViews.add(imageView17);
			// -----------------------------------
			mDayLayouts.add(t21);
			mDayLayouts.add(t22);
			mDayLayouts.add(t23);
			mDayLayouts.add(t24);
			mDayLayouts.add(t25);
			mDayLayouts.add(t26);
			mDayLayouts.add(t27);
			
			mMonthTextViews.add(textView21);
			mMonthTextViews.add(textView22);
			mMonthTextViews.add(textView23);
			mMonthTextViews.add(textView24);
			mMonthTextViews.add(textView25);
			mMonthTextViews.add(textView26);
			mMonthTextViews.add(textView27);

			mMonthBgViews.add(bgView21);
			mMonthBgViews.add(bgView22);
			mMonthBgViews.add(bgView23);
			mMonthBgViews.add(bgView24);
			mMonthBgViews.add(bgView25);
			mMonthBgViews.add(bgView26);
			mMonthBgViews.add(bgView27);

			mMonthImageViews.add(imageView21);
			mMonthImageViews.add(imageView22);
			mMonthImageViews.add(imageView23);
			mMonthImageViews.add(imageView24);
			mMonthImageViews.add(imageView25);
			mMonthImageViews.add(imageView26);
			mMonthImageViews.add(imageView27);
			// -----------------------------------
			mDayLayouts.add(t31);
			mDayLayouts.add(t32);
			mDayLayouts.add(t33);
			mDayLayouts.add(t34);
			mDayLayouts.add(t35);
			mDayLayouts.add(t36);
			mDayLayouts.add(t37);
			
			mMonthTextViews.add(textView31);
			mMonthTextViews.add(textView32);
			mMonthTextViews.add(textView33);
			mMonthTextViews.add(textView34);
			mMonthTextViews.add(textView35);
			mMonthTextViews.add(textView36);
			mMonthTextViews.add(textView37);

			mMonthBgViews.add(bgView31);
			mMonthBgViews.add(bgView32);
			mMonthBgViews.add(bgView33);
			mMonthBgViews.add(bgView34);
			mMonthBgViews.add(bgView35);
			mMonthBgViews.add(bgView36);
			mMonthBgViews.add(bgView37);

			mMonthImageViews.add(imageView31);
			mMonthImageViews.add(imageView32);
			mMonthImageViews.add(imageView33);
			mMonthImageViews.add(imageView34);
			mMonthImageViews.add(imageView35);
			mMonthImageViews.add(imageView36);
			mMonthImageViews.add(imageView37);
			// --------------------------------------
			mDayLayouts.add(t41);
			mDayLayouts.add(t42);
			mDayLayouts.add(t43);
			mDayLayouts.add(t44);
			mDayLayouts.add(t45);
			mDayLayouts.add(t46);
			mDayLayouts.add(t47);
			
			mMonthTextViews.add(textView41);
			mMonthTextViews.add(textView42);
			mMonthTextViews.add(textView43);
			mMonthTextViews.add(textView44);
			mMonthTextViews.add(textView45);
			mMonthTextViews.add(textView46);
			mMonthTextViews.add(textView47);

			mMonthBgViews.add(bgView41);
			mMonthBgViews.add(bgView42);
			mMonthBgViews.add(bgView43);
			mMonthBgViews.add(bgView44);
			mMonthBgViews.add(bgView45);
			mMonthBgViews.add(bgView46);
			mMonthBgViews.add(bgView47);

			mMonthImageViews.add(imageView41);
			mMonthImageViews.add(imageView42);
			mMonthImageViews.add(imageView43);
			mMonthImageViews.add(imageView44);
			mMonthImageViews.add(imageView45);
			mMonthImageViews.add(imageView46);
			mMonthImageViews.add(imageView47);
//---------------------------------------
			mDayLayouts.add(t51);
			mDayLayouts.add(t52);
			mDayLayouts.add(t53);
			mDayLayouts.add(t54);
			mDayLayouts.add(t55);
			mDayLayouts.add(t56);
			mDayLayouts.add(t57);
			
			mMonthTextViews.add(textView51);
			mMonthTextViews.add(textView52);
			mMonthTextViews.add(textView53);
			mMonthTextViews.add(textView54);
			mMonthTextViews.add(textView55);
			mMonthTextViews.add(textView56);
			mMonthTextViews.add(textView57);

			mMonthBgViews.add(bgView51);
			mMonthBgViews.add(bgView52);
			mMonthBgViews.add(bgView53);
			mMonthBgViews.add(bgView54);
			mMonthBgViews.add(bgView55);
			mMonthBgViews.add(bgView56);
			mMonthBgViews.add(bgView57);

			mMonthImageViews.add(imageView51);
			mMonthImageViews.add(imageView52);
			mMonthImageViews.add(imageView53);
			mMonthImageViews.add(imageView54);
			mMonthImageViews.add(imageView55);
			mMonthImageViews.add(imageView56);
			mMonthImageViews.add(imageView57);
//---------------------------------------
			mDayLayouts.add(t61);
			mDayLayouts.add(t62);
			mDayLayouts.add(t63);
			mDayLayouts.add(t64);
			mDayLayouts.add(t65);
			mDayLayouts.add(t66);
			mDayLayouts.add(t67);
			
			mMonthTextViews.add(textView61);
			mMonthTextViews.add(textView62);
			mMonthTextViews.add(textView63);
			mMonthTextViews.add(textView64);
			mMonthTextViews.add(textView65);
			mMonthTextViews.add(textView66);
			mMonthTextViews.add(textView67);

			mMonthBgViews.add(bgView61);
			mMonthBgViews.add(bgView62);
			mMonthBgViews.add(bgView63);
			mMonthBgViews.add(bgView64);
			mMonthBgViews.add(bgView65);
			mMonthBgViews.add(bgView66);
			mMonthBgViews.add(bgView67);

			mMonthImageViews.add(imageView61);
			mMonthImageViews.add(imageView62);
			mMonthImageViews.add(imageView63);
			mMonthImageViews.add(imageView64);
			mMonthImageViews.add(imageView65);
			mMonthImageViews.add(imageView66);
			mMonthImageViews.add(imageView67);
		}
	}

}
