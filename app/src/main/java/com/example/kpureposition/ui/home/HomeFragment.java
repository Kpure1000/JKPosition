package com.example.kpureposition.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.kpureposition.R;
import com.example.kpureposition.myUtil.Point;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //  Calculate:
    Point selfPoint = new Point();

    //  UI:

    private EditText eX, eY;
    private Button scanButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        textView.setText("");
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        //  UI init:
//        //  editText x, y:
        eX = (EditText) root.findViewById(R.id.axisX);
        eY = (EditText) root.findViewById(R.id.axisY);
        //  start scan button:
        scanButton = root.findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eX.getText().length() > 0 && eY.getText().length() > 0) {
                    selfPoint.x = Float.parseFloat(eX.getText().toString());
                    selfPoint.y = Float.parseFloat(eY.getText().toString());
                }

                Toast.makeText(root.getContext(), String.format("开始扫描,坐标(%s,%s).",
                        selfPoint.x, selfPoint.y), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}