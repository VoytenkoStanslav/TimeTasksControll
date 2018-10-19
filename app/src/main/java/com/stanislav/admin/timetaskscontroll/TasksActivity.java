package com.stanislav.admin.timetaskscontroll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity implements DataIdFragment{

    private static final String EXTRA_TASK_ID =
            "com.stanislav.admin.timetaskscontroll.task_id";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ViewPager mViewPager;
    private List<Task> mTasks = new ArrayList<>();

    private String taskId;
    String taskIdFr;

    boolean page = false;

    private Float incomePaymentDay;
    private Float incomePaymentHour;
    private Float incomePaymentMinute;

    FirebaseUser currentFirebaseUser;

    String beforeDeleteTask;

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initFirebase();
        getTasks();

        taskId = (String) getIntent().getSerializableExtra(EXTRA_TASK_ID);

        mViewPager = (ViewPager) findViewById(R.id.fragment_container_task);

    }

    public static Intent newIntent(Context packageContext, String taskId) {
        Intent intent = new Intent(packageContext, TasksActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    private void getTasks(){

        /*CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);*/

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser !=null){
            Log.e("Us", "onComplete: good");
        } else {
            Log.e("Us", "onComplete: null");
        }


        mDatabaseReference.child(currentFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mTasks.size() > 0) {
                            mTasks.clear();
                        }
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Task task = postSnapshot.getValue(Task.class);
                            mTasks.add(task);
                            if (mTasks.size()>2){

                            }
                        }
                        setAdapter(taskId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }



    private void setAdapter(String taskId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter((new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Task task = mTasks.get(position);
                if (position > 0){
                    Task beforeTask = mTasks.get(position-1);
                    beforeDeleteTask = beforeTask.getUid();
                } else {
                    beforeDeleteTask = " ";
                }

                String countSum = getString(R.string.not_text);
                float sum = task.getSum();
                long time = task.getTimeTask();
                int pos = task.getSpinPos();
                if (task.isCheckSumTask()) {
                    if (pos == 0) {
                        incomePaymentDay = sum / time;
                        incomePaymentHour = sum / (time * 24);
                        countSum = new DecimalFormat("#0.00").format(incomePaymentHour)
                                + " " + getString(R.string.hour) + " / "
                                + new DecimalFormat("#0.00").format(incomePaymentDay)
                                + " " + getString(R.string.day);
                    } else if (pos == 1) {
                        incomePaymentHour = sum / time;
                        countSum = new DecimalFormat("#0.00").format(incomePaymentHour) + " " + getString(R.string.hour);
                    } else {
                        incomePaymentMinute = sum / time;
                        countSum = new DecimalFormat("#0.00").format(incomePaymentMinute) + " " + getString(R.string.minute);
                    }
                }
                sPref = getPreferences(MODE_PRIVATE);
                long lastTime = sPref.getLong(task.getUid() + "Now", -25);
                long allTime = sPref.getLong(task.getUid() + "All", -25);
                if(lastTime<=0){
                    lastTime = task.getNowTimeTask();
                }
                if(allTime<=0){
                    allTime = task.getAllTimeTask();
                }
                return TaskFragment.newInstance(task.getUid(), task.getNameTask(), task.getFullNameTask(),
                        task.getAnyText(), task.isCheckSumTask(), task.getSum(), task.getTimeTask(), countSum,
                        String.valueOf(currentFirebaseUser.getUid()), allTime, lastTime, task.getSpinPos(), beforeDeleteTask, task.getDateCreateTask());
            }

            @Override
            public int getCount() {
                return mTasks.size();
            }
        }));

        for (int i = 0; i < mTasks.size(); i++) {
            if (mTasks.get(i).getUid().equals(taskId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }


    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    @Override
    public void getTaskId(String taskId) {
        super.finish();
    }

    @Override
    public void getBeforeTaskId(String beforeTaskId, String taskId) {
        this.taskId = beforeTaskId;
        mDatabaseReference.child(currentFirebaseUser.getUid())
                .child(taskId)
                .removeValue();
    }


}
