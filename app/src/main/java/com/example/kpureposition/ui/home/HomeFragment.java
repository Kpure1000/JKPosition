package com.example.kpureposition.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.util.Calendar;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.kpureposition.R;
import com.example.kpureposition.myUtil.Point;
import com.example.kpureposition.myUtil.WifiSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Scan here
 */
public class HomeFragment extends Fragment {

    //  Calculate:
    private Point selfPoint = new Point();

    //  UI:
    View root;

    /**
     * 输出用的
     */
    private EditText eX, eY;

    private TextView logView;

    private EditText axis_x1,axis_x2,axis_x3;
    private EditText axis_y1,axis_y2,axis_y3;

    //  Wifi
    private WifiManager wifiManager;
    private final int GET_SCAN_RESULT = 399;
    private final int MaxScanTimes = 3;
    private final int MaxScanNumbers = 10;
    private final int EffectiveLevel = -70;
    private boolean isScanStarted = false;
    private boolean isCalculated = false;

    // Method:

    /**
     * @param inflater           ?
     * @param container          ?
     * @param savedInstanceState state saved
     * @return root view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        if (root != null) return root;
        root = inflater.inflate(R.layout.fragment_home, container, false);

        //  UI init:
        eX = root.findViewById(R.id.axisX);
        eY = root.findViewById(R.id.axisY);
        logView = root.findViewById(R.id.logText);
        logView.setMovementMethod(ScrollingMovementMethod.getInstance());

        axis_x1 = root.findViewById(R.id.axis_x1);
        // TODO: 复制 5个

        //  WIFI init:
        wifiManager = (WifiManager) root.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        //  start scan button:
        Button scanButton = root.findViewById(R.id.scanButton);
        scanButton.setOnClickListener(startScanListener);
        Button calculateButton = root.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener((calculateListener));

        Toast.makeText(root.getContext(), "重新加载了555", Toast.LENGTH_LONG).show();

        return root;
    }

    /**
     * 控制台输出用的
     * @param textView
     * @param content
     */
    private void addLodText(TextView textView, String content) {
        if (!content.endsWith("\n")) content += "\n";
        textView.append(content);
        int offset = textView.getLayout().getLineTop(textView.getLineCount());
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }

    /**
     * 获取标准时间
     * @return
     */
    private String getTimeFormatStr() {
        Calendar cld = Calendar.getInstance();
        return String.format("%s/%s/%s %s:%s:%s",
                cld.get(Calendar.YEAR), cld.get(Calendar.MONTH), cld.get(Calendar.DATE),
                cld.get(Calendar.HOUR_OF_DAY), cld.get(Calendar.MINUTE), cld.get(Calendar.SECOND));
    }

    private void Calculate() {
        if (isScanStarted) return;
        isCalculated = true;
        // TODO: how to calculate
        // 用
        //  selfPoint.wifiLists;

        // 输出
        eX.setText("巴拉巴拉");
        eY.setText("巴拉巴拉");
        isCalculated = false;
    }

    // Listener or handler:

    /**
     * The handler to scan wifi and deal the results
     */
    @SuppressLint("HandlerLeak")
    private Handler scanHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_SCAN_RESULT) {
                // 扫
                wifiManager.startScan();
                // 结果
                List<ScanResult> scanResults = wifiManager.getScanResults();
                int resCount = 0;
                int index = 0;
                ArrayList<WifiSource> newWifiList = new ArrayList<>();
                //  加入自己的点
                selfPoint.wifiLists.add(newWifiList);
                addLodText(logView, "扫描结果：");
                // 遍历
                for (ScanResult item :
                        scanResults) {
                    if (resCount >= MaxScanNumbers) break;
                    // 输出
                    addLodText(logView, String.format("AP%s, SSID: %s, Level: %s", index, item.SSID, item.level));
                    if (item.level > EffectiveLevel) {
                        // 列表
                        newWifiList.add(new WifiSource(item.SSID, item.level));
                        resCount++;
                    }
                    index++;
                }
                addLodText(logView, String.format("共扫描到周边AP个数：%s, 有效个数(限%s个): %s\n" +
                        "--------------------------------------------------------\n", scanResults.size(), resCount, MaxScanNumbers));
            }
        }
    };

    /**
     * The Click listener to start scan
     */
    private View.OnClickListener startScanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isScanStarted || isCalculated) return;
            selfPoint.ClearLists();
            if (wifiManager != null) {
                String startScanStr = String.format("--------------------------------------------------------\n" +
                        "%s开始扫描: 自定义坐标 (%s,%s).", getTimeFormatStr(), selfPoint.x, selfPoint.y);
                addLodText(logView, startScanStr);
                Thread scanThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        isScanStarted = true;
                        int times = 0;
                        while (times < MaxScanTimes) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            scanHandler.sendEmptyMessage(GET_SCAN_RESULT);
                            // TODO: @see{scanHandler}
                            times++;
                        }
                        isScanStarted = false;
                    }
                });
                scanThread.start();
            }
        }
    };

    /**
     * 按计算按钮
     */
    private View.OnClickListener calculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isScanStarted) return;
            // TODO: call calculate
            Thread calculateThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Calculate();
                }
            });
            calculateThread.start();
        }
    };
}