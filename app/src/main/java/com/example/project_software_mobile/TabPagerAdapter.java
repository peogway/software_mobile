package com.example.project_software_mobile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabPagerAdapter extends FragmentStateAdapter {
    private String cityName;

    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity, String cityName) {
        super(fragmentActivity);
        this.cityName = cityName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return FragmentBasicInfor.newInstance(cityName);
            case 1:
                return FragmentComparison.newInstance(cityName);
            case 2:

                return FragmentQuiz.newInstance(cityName);

            case 3:
                return FragmentDataVisualization.newInstance(cityName);

            default:
                return FragmentBasicInfor.newInstance(cityName);
        }


    }

    @Override
    public int getItemCount() {
        return 4;
    }
    @Override
    public long getItemId(int position) {
        // Return a unique identifier for each fragment
        return position;
    }
}