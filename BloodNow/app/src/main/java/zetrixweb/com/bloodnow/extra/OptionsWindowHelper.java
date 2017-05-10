package zetrixweb.com.bloodnow.extra;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;


public class OptionsWindowHelper {

    public static List<String> options1Items = null;
    public static List<String> options2Items = null;
    public static List<String> options3Items = null;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String province, String city, String area);
    }

    private OptionsWindowHelper() {
    }

    public static CharacterPickerWindow builder(Activity activity, final OnOptionsSelectListener listener) {

        CharacterPickerWindow mOptions = new CharacterPickerWindow(activity);

        setPickerData(mOptions.getPickerView());

        mOptions.setSelectOptions(0, 0, 0);

        mOptions.setOnoptionsSelectListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                if (listener != null) {
                    String province = options1Items.get(option1);
                    String city = options2Items.get(option2);
                    String area = options3Items.get(option3);
                    listener.onOptionsSelect(province, city, area);
                }
            }
        });
        return mOptions;
    }

    public static void setPickerData(CharacterPickerView view) {
        if (options1Items == null) {
            options1Items = new ArrayList<>();
            options2Items = new ArrayList<>();
            options3Items = new ArrayList<>();

            for (int i = 1; i <= 31; i++) {
                options1Items.add(String.valueOf(i));
            }

            /*for (int i = 0; i < 12; i++) {
                *//*Calendar newCalendar = Calendar.getInstance();
                newCalendar.roll(Calendar.MONTH, i+1);*//*
                options2Items.add()
            }*/

            options2Items.add("January");
            options2Items.add("February");
            options2Items.add("March");
            options2Items.add("April");
            options2Items.add("May");
            options2Items.add("June");
            options2Items.add("July");
            options2Items.add("August");
            options2Items.add("September");
            options2Items.add("Octomber");
            options2Items.add("November");
            options2Items.add("December");


            for (int i = 1931; i <= 2017; i++) {
                options3Items.add(String.valueOf(i));
            }
        }

        view.setPicker(options1Items, options2Items, options3Items);
    }

}
