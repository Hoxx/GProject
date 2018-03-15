package com.hxx.gprojrct;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxx.gprojrct.view.PointProgress;


/**
 * Created by Android on 2018/3/15.
 */

public class ProgressFragment extends Fragment {

    private PointProgress point_progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.progress_fragment, container, false);
        point_progress = root.findViewById(R.id.point_progress);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        point_progress.setProgressColor(Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK);
        point_progress.start(5000, "");
    }

}
