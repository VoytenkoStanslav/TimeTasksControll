package com.stanislav.admin.timetaskscontroll;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TaskFragment extends Fragment{


    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;

    private TextView tvFrForNameTask;
    private TextView tvFrForFullNameTask;
    private TextView tvForFrAnyTask;
    private TextView tvFrSumTask;
    private TextView tvFrTimeTask;
    private TextView tvFrCountSum;
    private TextView tvFrAllTimePlay;
    private TextView tvFrLastTimePlay;
    private TextView tvFrForCountSum;
    private TextView tvFrForTimeTask;
    private TextView tvFrForSumTask;
    private TextView tvFrForRealCountSum;
    private TextView tvFrRealCountSum;
    private TextView tvFrDateTask;

    String userId;
    String taskId;

    DataIdFragment idTaskFragment;

    AlertDialog.Builder ad;

    private static final String ARG_TASK_ID = "task_id";
    private static final String ARG_TASK_NAME = "task_name";
    private static final String ARG_TASK_FULL_NAME = "task_full_name";
    private static final String ARG_TASK_ANY = "task_any";
    private static final String ARG_TASK_CHB = "task_chb";
    private static final String ARG_TASK_SUM = "task_sum";
    private static final String ARG_TASK_PLAN_TIME = "task_plan_time";
    private static final String ARG_TASK_COUNT_SUM = "task_count_sum";
    private static final String ARG_USER_ID = "task_user_id";
    private static final String ARG_TASK_ALL_TIME = "task_all_time";
    private static final String ARG_TASK_LAST_TIME = "task_last_time";
    private static final String ARG_TASK_SPIN_POS = "task_spin_pos";
    private static final String ARG_TASK_BEFORE_UID = "task_before_uid";
    private static final String ARG_TASK_DATE_CREATED = "task_date_created";


    private String taskName;
    private String taskFullName;
    private String taskAny;
    private boolean taskChb;
    private float taskSum;
    private long taskPlanTime;
    private String taskCountSum;
    private long allTime;
    private long lastTime;
    private int spinPos;
    private String beforeTaskUid;
    private long dateCreatedTask;

    private float realSum;

    private Float incomePaymentDay;
    private Float incomePaymentHour;
    private Float incomePaymentMinute;

    private Float planIncomePaymentDay;
    private Float planIncomePaymentHour;
    private Float planIncomePaymentMinute;

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");


    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            idTaskFragment = (DataIdFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    private void sendData(String taskId)
    {
        idTaskFragment.getTaskId(taskId);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFirebase();

    }


    public static TaskFragment newInstance(String taskId, String taskName, String taskFullName,
                                           String taskAny, boolean taskChb, float taskSum,
                                           long taskPlanTime, String taskCountSum, String userId,
                                           long allTime, long lastTime, int spinPos, String beforeTaskUid, long dateCreatedTask) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        args.putSerializable(ARG_TASK_NAME, taskName);
        args.putSerializable(ARG_TASK_FULL_NAME, taskFullName);
        args.putSerializable(ARG_TASK_ANY, taskAny);
        args.putSerializable(ARG_TASK_CHB, taskChb);
        args.putSerializable(ARG_TASK_SUM, taskSum);
        args.putSerializable(ARG_TASK_PLAN_TIME, taskPlanTime);
        args.putSerializable(ARG_TASK_COUNT_SUM, taskCountSum);
        args.putSerializable(ARG_USER_ID, userId);
        args.putSerializable(ARG_TASK_ALL_TIME, allTime);
        args.putSerializable(ARG_TASK_LAST_TIME, lastTime);
        args.putSerializable(ARG_TASK_SPIN_POS, spinPos);
        args.putSerializable(ARG_TASK_BEFORE_UID, beforeTaskUid);
        args.putSerializable(ARG_TASK_DATE_CREATED, dateCreatedTask);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        taskId = (String) getArguments().getSerializable(ARG_TASK_ID);
        taskName = (String) getArguments().getSerializable(ARG_TASK_NAME);
        taskFullName = (String) getArguments().getSerializable(ARG_TASK_FULL_NAME);
        taskAny = (String) getArguments().getSerializable(ARG_TASK_ANY);
        taskChb = (boolean) getArguments().getSerializable(ARG_TASK_CHB);
        taskSum = (float) getArguments().getSerializable(ARG_TASK_SUM);
        taskPlanTime = (long) getArguments().getSerializable(ARG_TASK_PLAN_TIME);
        taskCountSum = (String) getArguments().getSerializable(ARG_TASK_COUNT_SUM);
        userId = (String) getArguments().getSerializable(ARG_USER_ID);
        allTime = (long) getArguments().getSerializable(ARG_TASK_ALL_TIME);
        lastTime = (long) getArguments().getSerializable(ARG_TASK_LAST_TIME);
        spinPos = (int) getArguments().getSerializable(ARG_TASK_SPIN_POS);
        beforeTaskUid = (String) getArguments().getSerializable(ARG_TASK_BEFORE_UID);
        dateCreatedTask = (long) getArguments().getSerializable(ARG_TASK_DATE_CREATED);

        int d = Math.round(allTime/(86400 * 1000));
        int h = Math.round((allTime-(86400*1000*d))/(3600*1000));
        int m = Math.round((allTime-(86400*1000*d)-(3600*1000*h))/(60*1000));
        int s = Math.round((allTime-(86400*1000*d)-(3600*1000*h)-(60*1000*m))/(1000));

        int hs = Math.round(lastTime/(3600*1000));
        int ms = Math.round((lastTime-(3600*1000*hs))/(60*1000));
        int ss = Math.round((lastTime-(3600*1000*hs)-(60*1000*ms))/(1000));

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        tvFrForNameTask = (TextView) view.findViewById(R.id.tvFrForNameTask);
        tvFrForNameTask.setText(taskName);

        tvFrForFullNameTask = (TextView) view.findViewById(R.id.tvFrForFullNameTask);
        if(taskFullName.isEmpty()){
            tvFrForFullNameTask.setText(getString(R.string.not_text));
        } else {
            tvFrForFullNameTask.setText(taskFullName);
        }

        tvForFrAnyTask = (TextView) view.findViewById(R.id.tvForFrAnyTask);
        if (taskAny.isEmpty()){
            tvForFrAnyTask.setText(getString(R.string.not_text));
        } else {
            tvForFrAnyTask.setText(taskAny);
        }

        tvFrDateTask = (TextView) view.findViewById(R.id.tvFrDateTask);
        tvFrDateTask.setText(sdf.format(new Date(dateCreatedTask)));

        tvFrSumTask = (TextView) view.findViewById(R.id.tvFrSumTask);
        tvFrSumTask.setText(new DecimalFormat("#0.00").format(taskSum));

        tvFrRealCountSum = (TextView) view.findViewById(R.id.tvFrRealCountSum);

        tvFrAllTimePlay = (TextView) view.findViewById(R.id.tvFrAllTimePlay);

        tvFrTimeTask = (TextView) view.findViewById(R.id.tvFrTimeTask);

        tvFrLastTimePlay = (TextView) view.findViewById(R.id.tvFrLastTimePlay);

        tvFrCountSum = (TextView) view.findViewById(R.id.tvFrCountSum);

        int timeDay = (int) Math.ceil(allTime/(86400*1000));
        if (timeDay<1){
            timeDay = 1;
        }
        int timeHour = (int) Math.ceil(allTime/(3600*1000));
        if (timeHour<1){
            timeHour = 1;
        }
        int timeMinute = (int) Math.ceil(allTime/(60*1000));
        if (timeMinute<1){
            timeMinute = 1;
        }

        String allTimeText = String.valueOf(d) + " " + getString(R.string.tf_day) + " "
                + String.valueOf(h) + " " + getString(R.string.tf_hour) + " "
                + String.valueOf(m) + " " + getString(R.string.tf_minute) + " "
                + String.valueOf(s) + " " + getString(R.string.tf_second);

        String lastTimeText = String.valueOf(hs) + " " + getString(R.string.tf_hour) + " "
                + String.valueOf(ms) + " " + getString(R.string.tf_minute) + " "
                + String.valueOf(ss) + " " + getString(R.string.tf_second);

        if(spinPos == 0) {
            tvFrTimeTask.setText(String.valueOf(taskPlanTime) + " " + getString(R.string.day_2));
            incomePaymentDay = taskSum / timeDay;
            incomePaymentHour = taskSum / timeHour;
            planIncomePaymentDay = taskSum / taskPlanTime;
            planIncomePaymentHour = taskSum / (taskPlanTime * 24);
            tvFrRealCountSum.setText(new DecimalFormat("#0.00").format(incomePaymentHour)
                    + " " + getString(R.string.hour) + " / "
                    + new DecimalFormat("#0.00").format(incomePaymentDay)
                    + " " + getString(R.string.day));
            if (taskPlanTime != 0) {
                tvFrCountSum.setText(new DecimalFormat("#0.00").format(planIncomePaymentHour)
                        + " " + getString(R.string.hour) + " / "
                        + new DecimalFormat("#0.00").format(planIncomePaymentDay)
                        + " " + getString(R.string.day));
            } else {
                tvFrCountSum.setText(getString(R.string.payment_income));
            }
            tvFrAllTimePlay.setText(allTimeText);
            tvFrLastTimePlay.setText(lastTimeText);
        } else if (spinPos == 1){
            tvFrTimeTask.setText(String.valueOf(taskPlanTime) + " " + getString(R.string.hour_2));
            incomePaymentHour = taskSum / timeHour;
            tvFrRealCountSum.setText(new DecimalFormat("#0.00").format(incomePaymentHour) + " " + getString(R.string.hour));
            planIncomePaymentHour = taskSum / taskPlanTime;
            if (taskPlanTime != 0) {
                tvFrCountSum.setText(new DecimalFormat("#0.00").format(planIncomePaymentHour) + " " + getString(R.string.hour));
            } else {
                tvFrCountSum.setText(getString(R.string.payment_income));
            }
            tvFrAllTimePlay.setText(allTimeText);
            tvFrLastTimePlay.setText(lastTimeText);
        } else if (spinPos == 2){
            tvFrTimeTask.setText(String.valueOf(taskPlanTime) + " " + getString(R.string.minute_2));
            incomePaymentMinute = taskSum / timeMinute;
            tvFrRealCountSum.setText(new DecimalFormat("#0.00").format(incomePaymentMinute) + " " + getString(R.string.minute));
            planIncomePaymentMinute = taskSum / taskPlanTime;
            if (taskPlanTime != 0) {
                tvFrCountSum.setText(new DecimalFormat("#0.00").format(planIncomePaymentMinute) + " " + getString(R.string.minute));
            } else {
                tvFrCountSum.setText(getString(R.string.payment_income));
            }
            tvFrAllTimePlay.setText(allTimeText);
            tvFrLastTimePlay.setText(lastTimeText);
        }

        tvFrForRealCountSum = (TextView) view.findViewById(R.id.tvFrForRealCountSum);

        tvFrForCountSum = (TextView) view.findViewById(R.id.tvFrForCountSum);

        tvFrForSumTask = (TextView) view.findViewById(R.id.tvFrForSumTask);

        tvFrForTimeTask = (TextView) view.findViewById(R.id.tvFrForTimeTask);

        if(taskChb){
            tvFrForCountSum.setVisibility(View.VISIBLE);
            tvFrForSumTask.setVisibility(View.VISIBLE);
            tvFrForTimeTask.setVisibility(View.VISIBLE);
            tvFrCountSum.setVisibility(View.VISIBLE);
            tvFrSumTask.setVisibility(View.VISIBLE);
            tvFrTimeTask.setVisibility(View.VISIBLE);
            tvFrRealCountSum.setVisibility(View.VISIBLE);
            tvFrForRealCountSum.setVisibility(View.VISIBLE);
        } else {
            tvFrForCountSum.setVisibility(View.GONE);
            tvFrForSumTask.setVisibility(View.GONE);
            tvFrForTimeTask.setVisibility(View.GONE);
            tvFrCountSum.setVisibility(View.GONE);
            tvFrSumTask.setVisibility(View.GONE);
            tvFrTimeTask.setVisibility(View.GONE);
            tvFrRealCountSum.setVisibility(View.GONE);
            tvFrForRealCountSum.setVisibility(View.GONE);
        }

        ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(getString(R.string.dialog_title));  // заголовок
        ad.setMessage(taskName); // сообщение
        ad.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                idTaskFragment.getBeforeTaskId(beforeTaskUid, taskId);
            }
        });
        ad.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_activity_task, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_modify:
                Intent intent = ModifyTaskActivity.newIntent(getActivity(), taskId, taskName,
                        taskFullName, taskAny, taskChb, taskSum, taskPlanTime, taskCountSum,
                        userId, allTime, spinPos, dateCreatedTask);
                startActivity(intent);
                break;
            case android.R.id.home:
                sendData(taskId);
                break;
            case R.id.menu_remove:
                ad.show();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(getActivity());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


}
