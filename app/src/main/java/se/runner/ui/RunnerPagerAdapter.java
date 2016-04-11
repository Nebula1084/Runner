package se.runner.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.karim.MaterialTabs;
import se.runner.R;

public class RunnerPagerAdapter extends FragmentPagerAdapter implements MaterialTabs.CustomTabProvider {
    private Context context;
    private ArrayList<String> titles;
    private ArrayList<Fragment> fragments;
    private ArrayList<Drawable> drawables;

    public RunnerPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        drawables= new ArrayList<>();
    }

    public void setItem(String title, Fragment fragment, Drawable drawable) {
        titles.add(title);
        fragments.add(fragment);
        drawables.add(drawable);
    }

    public void removeItem(int position) {
        titles.remove(position);
        fragments.remove(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public View getCustomTabView(ViewGroup viewGroup, int i) {
        View view;
        Drawable drawable=drawables.get(i);
        if (drawable!=null){
            view = LayoutInflater.from(context).inflate(R.layout.main_tabs, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.tabs_face);
            imageView.setImageDrawable(drawable);
            TextView textView = (TextView) view.findViewById(R.id.tabs_text);
            textView.setText(titles.get(i));
            return view;
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.text_tabs, null);
            ((TextView)view).setText(titles.get(i));
            return view;
        }

    }
}
