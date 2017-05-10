package zetrixweb.com.bloodnow.extra;

import android.app.Activity;

import java.util.ArrayList;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;


public class OptionsWindowHelperr {

    static IsoCountries isoCountries;
    static ArrayList<String> items;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String province,String country);
    }

    private OptionsWindowHelperr() {
    }

    public static CharacterPickerWindow builder(Activity activity, final OnOptionsSelectListener listener) {

        CharacterPickerWindow mOptions = new CharacterPickerWindow(activity);
        setPickerData(mOptions.getPickerView());
        mOptions.setSelectOptions(0, 0, 0);
        mOptions.setOnoptionsSelectListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                if (listener != null) {
                    String province = isoCountries.allCountries.get(option1).calling;
                    listener.onOptionsSelect(province,items.get(option1));
                }
            }

        });
        return mOptions;
    }

    public static void setPickerData(CharacterPickerView view) {
        isoCountries = new IsoCountries();
        items = new ArrayList<>();
        for (int i = 0; i < isoCountries.allCountries.size(); i++) {
            items.add(isoCountries.allCountries.get(i).name);
        }
        view.setPicker(items);
    }

}
