package com.chengtech.chengtechmt.adapter.business;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.ImageAddAdapter;
import com.chengtech.chengtechmt.entity.bridge.Bridge;
import com.chengtech.chengtechmt.entity.patrol.BridgeRecord;
import com.chengtech.chengtechmt.entity.patrol.CulvertOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.TunnelOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.TunnelRecord;
import com.chengtech.chengtechmt.entity.tunnel.Tunnel;
import com.chengtech.chengtechmt.entity.tunnel.TunnelRecords;
import com.chengtech.chengtechmt.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.chengtech.chengtechmt.R.array.bridgeRecord;

/**
 * 隧道新增记录adapter
 * 作者: LiuFuYingWang on 2017/5/24 14:27.
 */

public class TunnelOftenCheckAddAdapter extends RecyclerView.Adapter {

    private int HEAD_TYPE = 0;
    private int NORMAL_TYPE = 1;
    private int FOOT_TYPE = 2;
    private TunnelOftenCheck tunnelOftenCheck;
    private Context mContext;
    private ImageAddAdapter imageAdapter;
    private ArrayList<String> picturePaths;


    public TunnelOftenCheckAddAdapter(Context context, TunnelOftenCheck tunnelOftenCheck) {
        this.tunnelOftenCheck = tunnelOftenCheck;
        this.mContext = context;
    }
    public TunnelOftenCheckAddAdapter(Context context, TunnelOftenCheck tunnelOftenCheck,List<String> filePaths) {
        this.tunnelOftenCheck = tunnelOftenCheck;
        this.mContext = context;
        this.picturePaths = (ArrayList<String>) filePaths;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tun_often_add_head = LayoutInflater.from(parent.getContext()).inflate(R.layout.tunnel_often_add_head, parent, false);
        ViewHolder1 viewHolder1 = new ViewHolder1(tun_often_add_head);

        View tun_often_add_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.tunnel_often_add_item, parent, false);
        ViewHolder2 viewHolder2 = new ViewHolder2(tun_often_add_item);

        View tun_often_add_foot = LayoutInflater.from(parent.getContext()).inflate(R.layout.bridge_often_add_foot, parent, false);
        ViewHolder3 viewHolder3 = new ViewHolder3(tun_often_add_foot);
        if (viewType == HEAD_TYPE)
            return viewHolder1;

        if (viewType == FOOT_TYPE)
            return viewHolder3;
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1) {
            final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.secondDept.setText((tunnelOftenCheck.secondDeptName == null ? "" : tunnelOftenCheck.secondDeptName));
            viewHolder1.thirdDept.setText((tunnelOftenCheck.thirdDeptName == null ? "" : tunnelOftenCheck.thirdDeptName));
            viewHolder1.head.setText(tunnelOftenCheck.head == null ? "" : tunnelOftenCheck.head);
            viewHolder1.head.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tunnelOftenCheck.head = s.toString();
                }
            });
            viewHolder1.supervisor.setText(tunnelOftenCheck.supervisor == null ? "" : tunnelOftenCheck.supervisor);
            viewHolder1.supervisor.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tunnelOftenCheck.supervisor = s.toString();
                }
            });
            viewHolder1.record.setText(tunnelOftenCheck.record == null ? "" : tunnelOftenCheck.record);
            viewHolder1.record.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tunnelOftenCheck.record = s.toString();
                }
            });
            viewHolder1.overallEvaluation.setText(tunnelOftenCheck.overallEvaluation == null ? "" : tunnelOftenCheck.overallEvaluation);
            viewHolder1.overallEvaluation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    tunnelOftenCheck.overallEvaluation = s.toString();
                }
            });
            viewHolder1.checkDate.setText(tunnelOftenCheck.patrolDate.substring(0, 10));
//            final int year = Integer.parseInt(tunnelOftenCheck.patrolDate.substring(0, 4));
//            final int month = Integer.parseInt(tunnelOftenCheck.patrolDate.substring(5, 7));
//            final int day = Integer.parseInt(tunnelOftenCheck.patrolDate.substring(8, 10));
            viewHolder1.checkDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String monthStr = String.format("%02d", monthOfYear + 1);
                            viewHolder1.checkDate.setText(year + "-" + monthStr + "-" + dayOfMonth);
                            tunnelOftenCheck.patrolDate = year + "-" + monthStr + "-" + dayOfMonth;
                        }
                    }, Integer.parseInt(tunnelOftenCheck.patrolDate.substring(0, 4)),
                            (Integer.parseInt(tunnelOftenCheck.patrolDate.substring(5, 7)) - 1),
                            Integer.parseInt(tunnelOftenCheck.patrolDate.substring(8, 10)));
                    dialog.show();
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, tunnelOftenCheck.listWeather);
            viewHolder1.weather.setAdapter(adapter);
            Integer weatherPos = tunnelOftenCheck.savePosition.get("天气");
            if (weatherPos != null)
                viewHolder1.weather.setSelection(weatherPos, true);
            viewHolder1.weather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String content = tunnelOftenCheck.listWeather.get(position);
                    tunnelOftenCheck.weather = content;
                    tunnelOftenCheck.savePosition.put("天气", position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final List<String> data = new ArrayList<>();
            data.add("");
            for (int i = 0; i < tunnelOftenCheck.listTunnel.size(); i++) {
                Tunnel tunnel = tunnelOftenCheck.listTunnel.get(i);
                data.add(tunnel.code + "---" + tunnel.name + "---" + tunnel.stake);
            }
            ArrayAdapter<String> tunnelCodeAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, data);
            viewHolder1.tunnelCode.setAdapter(tunnelCodeAdapter);
            Integer tunNo = tunnelOftenCheck.savePosition.get("隧道编码");
            if (tunNo != null)
                viewHolder1.tunnelCode.setSelection(tunNo, true);
            viewHolder1.tunnelCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String content = data.get(position);
                    String routeCode = "";
                    if (position != 0) {
                        routeCode = tunnelOftenCheck.listTunnel.get(position - 1).routeCode;
                    }
                    viewHolder1.tunnelName.setText("");
                    String[] split = content.split("---");
                    if (split != null && split.length == 3) {
                        viewHolder1.tunnelName.setText(split[1]);
                        tunnelOftenCheck.tunnelCode = split[0];
                    }
                    viewHolder1.routeCode.setText(routeCode);
                    tunnelOftenCheck.savePosition.put("隧道编码", position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            if (tunnelOftenCheck != null && tunnelOftenCheck.listTunnelRecords != null && tunnelOftenCheck.listTunnelRecords.size() > 0) {
                List<TunnelRecord> tunnelRecords = tunnelOftenCheck.listTunnelRecords;
                final TunnelRecord tunnelRecord = tunnelRecords.get(position - 1);
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        tunnelRecord.mileageMark = s.toString();
                    }
                };
                addTextWatcher(viewHolder2.mileageMark,textWatcher,tunnelRecord.mileageMark);

                TextWatcher checkContent = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        tunnelRecord.checkContents = s.toString();
                    }
                };
                addTextWatcher(viewHolder2.checkContents,checkContent,tunnelRecord.checkContents);

                TextWatcher stateDiscription = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        tunnelRecord.stateDiscription = s.toString();
                    }
                };
                addTextWatcher(viewHolder2.stateDiscription,stateDiscription,tunnelRecord.stateDiscription);

                TextWatcher judgeResult = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        tunnelRecord.judgeResult = s.toString();
                    }
                };
                addTextWatcher(viewHolder2.judgeResult,judgeResult,tunnelRecord.judgeResult);

                final List<String> data = new ArrayList<>();
                data.add("");
                data.addAll(tunnelOftenCheck.listProjectName);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, data);
                viewHolder2.projectName.setAdapter(adapter);
                if (tunnelOftenCheck.savePosition.get("项目名称"+tunnelRecord.projectName ) != null)
                    viewHolder2.projectName.setSelection(tunnelOftenCheck.savePosition.get("项目名称"+tunnelRecord.projectName  ));
                viewHolder2.projectName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        tunnelRecord.projectName = data.get(position);
                        tunnelOftenCheck.savePosition.put("项目名称"+tunnelRecord.projectName  , position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

        } else if (holder instanceof ViewHolder3) {
            ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            RecyclerView recyclerView =  viewHolder3.recyclerView;
            recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
            imageAdapter = new ImageAddAdapter(mContext,picturePaths);
            recyclerView.setAdapter(imageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return 2+(tunnelOftenCheck.listTunnelRecords==null?0:tunnelOftenCheck.listTunnelRecords.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEAD_TYPE;
        if (position==getItemCount()-1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView secondDept, thirdDept, routeCode, tunnelName,
                checkDate;

        public EditText head, record, supervisor, overallEvaluation;
        public Spinner tunnelCode, weather;

        public ViewHolder1(View itemView) {
            super(itemView);

            secondDept = (TextView) itemView.findViewById(R.id.secondDept);
            thirdDept = (TextView) itemView.findViewById(R.id.thirdDept);
            routeCode = (TextView) itemView.findViewById(R.id.routeCode);
            head = (EditText) itemView.findViewById(R.id.head);
            record = (EditText) itemView.findViewById(R.id.record);
            supervisor = (EditText) itemView.findViewById(R.id.supervisor);
            overallEvaluation = (EditText) itemView.findViewById(R.id.overallEvaluation);
            checkDate = (TextView) itemView.findViewById(R.id.checkDate);
            tunnelName = (TextView) itemView.findViewById(R.id.tunnelName);
            tunnelCode = (Spinner) itemView.findViewById(R.id.tunnelCode);
            weather = (Spinner) itemView.findViewById(R.id.weather);

        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        public EditText mileageMark, checkContents, stateDiscription, judgeResult;
        public Spinner projectName;


        public ViewHolder2(View itemView) {
            super(itemView);
            projectName = (Spinner) itemView.findViewById(R.id.projectName);
            mileageMark = (EditText) itemView.findViewById(R.id.mileageMark);
            checkContents = (EditText) itemView.findViewById(R.id.checkContents);
            stateDiscription = (EditText) itemView.findViewById(R.id.stateDiscription);
            judgeResult = (EditText) itemView.findViewById(R.id.judgeResult);
        }
    }

    private class ViewHolder3 extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;

        public ViewHolder3(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

        }
    }
    public ImageAddAdapter getImageAdapter() {
        return imageAdapter;
    }

    public void addTextWatcher(EditText target, TextWatcher textWatcher, String result) {
        if (target.getTag() instanceof TextWatcher) {
            target.removeTextChangedListener((TextWatcher) target.getTag());
        }
        target.setText(result);
        target.addTextChangedListener(textWatcher);
        target.setTag(textWatcher);
    }
}
