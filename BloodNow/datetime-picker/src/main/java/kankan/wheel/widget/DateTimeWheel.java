package kankan.wheel.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import kankan.wheel.widget.adapters.DayArrayAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class DateTimeWheel extends LinearLayout {
	private Calendar calendar = Calendar.getInstance();

	public static ArrayList<Calendar> strings;
	private OnTimeChangedListener timeChangedListener = null;
	private DayArrayAdapter dayArrayAdapter;

	private final int daysCount = 12;
	
	public DateTimeWheel(Context context) {
		this(context, null);
	}

	public DateTimeWheel(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.date_time_wheel, this, true);
		
		final WheelView day = (WheelView) findViewById(R.id.day);
		NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 1, 31, "%02d");
		hourAdapter.setItemResource(R.layout.wheel_text_item);
		hourAdapter.setItemTextResource(R.id.text);
		day.setViewAdapter(hourAdapter);
		day.setCyclic(true);

		final WheelView yearView = (WheelView) findViewById(R.id.year);
		NumericWheelAdapter minAdapter = new NumericWheelAdapter(context, 1913, 2017, "");
		minAdapter.setItemResource(R.layout.wheel_text_item);
		minAdapter.setItemTextResource(R.id.text);
		yearView.setViewAdapter(minAdapter);
		yearView.setCyclic(true);

		yearView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				calendar.set(Calendar.YEAR, 1913+newValue+1);
				fireTimeChanged(calendar.getTimeInMillis());
			}
		});

		day.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				calendar.set(Calendar.DAY_OF_MONTH, newValue+oldValue);
				fireTimeChanged(calendar.getTimeInMillis());
			}
		});

		// set current time
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH));
		yearView.setCurrentItem(calendar.get(Calendar.YEAR)-13);

		DateTimeWheel.strings = new ArrayList<>();

		for (int i = 0; i < daysCount; i++) {
			/*int day1 = -daysCount / 2 + i;*/
			Calendar newCalendar = (Calendar) calendar.clone();
			newCalendar.roll(Calendar.MONTH, i+1);
			DateTimeWheel.strings.add(newCalendar);
		}

		final WheelView monthView = (WheelView) findViewById(R.id.month);

		class DateCompare implements Comparator<Calendar> {
			public int compare(Calendar one, Calendar two) {
				return one.compareTo(two);
			}
		}
		DateCompare dateCompare = new DateCompare();
		Collections.sort(strings,dateCompare);
		dayArrayAdapter = new DayArrayAdapter(context, calendar);
		monthView.setViewAdapter(dayArrayAdapter);
		//dayView.setCurrentItem(calendar.get(Calendar.DAY_OF_YEAR));
		monthView.setCyclic(true);
		monthView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				calendar.set(Calendar.MONTH,strings.get(newValue).get(Calendar.MONTH)+1);
				//calendar.set(Calendar.YEAR,strings.get(newValue).get(Calendar.YEAR)+1);
				//calendar.set(Calendar.DATE,strings.get(newValue).get(Calendar.DATE)+1);
				//calendar.setTime(dateTime(strings.get(newValue).getTime(),calendar.getTime()));
				fireTimeChanged(calendar.getTimeInMillis());
			}
		});
	}

	/*public Date dateTime(Date date, Date time) {
		return new Date(
				date.getYear(), date.getMonth(), date.getDay(),
				time.getHours(), time.getMinutes(), time.getSeconds()
		);
	}*/

	public void setCalenderTime(Calendar calenderTime)
	{
		calendar = calenderTime;
		final WheelView dayView = (WheelView) findViewById(R.id.day);
		dayView.setCurrentItem(calendar.get(Calendar.DAY_OF_YEAR)-1);

		final WheelView month = (WheelView) findViewById(R.id.month);
		month.setCurrentItem(calendar.get(Calendar.HOUR));

		final WheelView mins = (WheelView) findViewById(R.id.year);
		mins.setCurrentItem(calendar.get(Calendar.MINUTE));
	}

	private void fireTimeChanged(long timeInMillis) {
		if (timeChangedListener != null) {
			timeChangedListener.onTimeChanged(timeInMillis);
		}
	}
	
	public void setOnTimeChangedListener(OnTimeChangedListener timeChangedListener) {
		this.timeChangedListener = timeChangedListener;
	}

	public interface OnTimeChangedListener {
		void onTimeChanged(long time);
	}

}
