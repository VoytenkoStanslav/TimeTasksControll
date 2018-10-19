package com.stanislav.admin.timetaskscontroll;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mTasksRecyclerView;
    private TasksAdapter mAdapter;

    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private String saveTaskTimeUid = " ";

    FirebaseUser currentFirebaseUser;

    private List<Task> mTasksForSave = new ArrayList<>();

    //private List<Task> mTasks;

    private LinearLayout lnMain;

    private boolean col[];

    private ArrayList<Boolean> run = new ArrayList<>();

    private Map<String, Boolean> hashMap = new HashMap<String, Boolean>();

    private Map<String, Boolean> hashMapPause = new HashMap<String, Boolean>();

    private int globalPosition = -10;

    SharedPreferences sPref;

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mAuth = FirebaseAuth.getInstance();

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        mTasksRecyclerView = (RecyclerView) findViewById(R.id.tasks_recycler_view);
        lnMain = (LinearLayout) findViewById(R.id.lnMain);
        lnMain.setVisibility(View.VISIBLE);
        initFirebase();
        updateUI();
        mTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private class TasksHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView tvItemNameTask;
        private TextView tvItemAllTimeTask;
        private TextView tvItemNowTimeTask;
        private TextView tvItemDateTask;
        private Chronometer mChronometerNow;
        private Chronometer mChronometerAllTime;


        private Button btnPlayTask;
        private Button btnStopTask;
        private Button btnPauseTask;
        private Button btnRepiteTask;

        private Task mTask;

        public TasksHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_task, parent, false));

            itemView.setOnClickListener(this);

            tvItemNameTask = (TextView) itemView.findViewById(R.id.tvItemNameTask);
            tvItemAllTimeTask = (TextView) itemView.findViewById(R.id.tvItemAllTimeTask);
            tvItemNowTimeTask = (TextView) itemView.findViewById(R.id.tvItemNowTimeTask);
            mChronometerNow = itemView.findViewById(R.id.chronometerNow);
            mChronometerNow.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long elapsedMillis = SystemClock.elapsedRealtime()
                            - mChronometerNow.getBase();
                }
            });

            mChronometerAllTime = itemView.findViewById(R.id.chronometerAllTime);
            mChronometerAllTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long elapsedMillis = SystemClock.elapsedRealtime()
                            - mChronometerAllTime.getBase();
                }
            });

            btnPlayTask = (Button) itemView.findViewById(R.id.btnPlayTask);
            btnPlayTask.setOnClickListener(this);

            btnStopTask = (Button) itemView.findViewById(R.id.btnStopTask);
            btnStopTask.setOnClickListener(this);

        }

        public void bind(Task task){
            mTask = task;
            tvItemNameTask.setText(mTask.getNameTask());
        }

        public void bindForTrue(Task task){
            mTask = task;

            btnPlayTask.setVisibility(View.GONE);
            btnStopTask.setVisibility(View.VISIBLE);

            tvItemNameTask.setText(mTask.getNameTask());
            sPref = getPreferences(MODE_PRIVATE);
            long time = sPref.getLong(mTask.getUid(), -25);
            long allTimeInFile = sPref.getLong(mTask.getUid() + "All", -25);
            if (time > 0) {
                Calendar c = Calendar.getInstance();
                long timeNow = c.getTimeInMillis();
                long timeReset = timeNow - time;
                mChronometerNow.setBase(SystemClock.elapsedRealtime()-timeReset);
                mChronometerNow.start();
                if (allTimeInFile > 0){
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime()-allTimeInFile - timeReset);
                    mChronometerAllTime.start();
                } else {
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime()-task.getAllTimeTask() - timeReset);
                    mChronometerAllTime.start();
                }
            } else {
                mChronometerNow.setBase(SystemClock.elapsedRealtime());
                mChronometerNow.start();
                if (allTimeInFile > 0){
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime()-allTimeInFile);
                    mChronometerAllTime.start();
                } else {
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime() - task.getAllTimeTask());
                    mChronometerAllTime.start();
                }
            }
        }

        public void bindForFalse(Task task){
            mTask = task;
            tvItemNameTask.setText(mTask.getNameTask());
            //long date = mTask.
            sPref = getPreferences(MODE_PRIVATE);
            //if (time == 0) {
            btnPlayTask.setVisibility(View.VISIBLE);
            btnStopTask.setVisibility(View.GONE);

            long allTimeInFile = sPref.getLong(mTask.getUid() + "All", -25);

            if (task.getUid().equals(saveTaskTimeUid)){
                long time = sPref.getLong(mTask.getUid(), -25);
                long newAllTime = 0;
                Calendar c = Calendar.getInstance();
                long timeNow = c.getTimeInMillis();
                long timeReset = timeNow - time;
                if(allTimeInFile>0){
                    newAllTime = allTimeInFile + timeReset;
                    saveTimeTaskInPref(mTask.getUid(), newAllTime, timeReset);
                    mChronometerNow.stop();
                    mChronometerNow.setBase(SystemClock.elapsedRealtime());
                    mChronometerAllTime.stop();
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime() - newAllTime);
                } else {
                    newAllTime = mTask.getAllTimeTask() + timeReset;
                    saveTimeTaskInPref(mTask.getUid(),newAllTime, timeReset);
                    mChronometerNow.stop();
                    mChronometerNow.setBase(SystemClock.elapsedRealtime());
                    mChronometerAllTime.stop();
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime() - newAllTime);
                }
                saveStopTask(mTask.getUid());
                saveTaskTimeUid = " ";
            } else {
                mChronometerNow.stop();
                mChronometerNow.setBase(SystemClock.elapsedRealtime());
                if (allTimeInFile > 0) {
                    mChronometerAllTime.stop();
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime() - allTimeInFile);
                } else {
                    mChronometerAllTime.stop();
                    mChronometerAllTime.setBase(SystemClock.elapsedRealtime() - task.getAllTimeTask());
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == btnPlayTask.getId()){
                hashMap.put(mTask.getUid(), true);
                String nameSaveTask = mTask.getUid();
                mAdapter.notifyItemChanged(getLayoutPosition());
                saveBeginTask(nameSaveTask);
            } else
                if (v.getId() == btnStopTask.getId()) {
                    hashMap.put(mTask.getUid(), false);
                    saveTaskTimeUid = mTask.getUid();
                    mAdapter.notifyItemChanged(getLayoutPosition());
            } else {
                    Intent intent = TasksActivity.newIntent(MainActivity.this, mTask.getUid());
                    startActivity(intent);
            }
        }
    }

    private class TasksAdapter extends RecyclerView.Adapter<TasksHolder>{

        List<Task> mTasks;

        public TasksAdapter(List<Task> tasks) {
            this.mTasks = tasks;
        }

        @Override
        public TasksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            return new TasksHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TasksHolder holder, int position) {

            Task task = mTasks.get(position);

            //for(int i = 0; i<col.length; i++) {
            if (hashMap.get(task.getUid()) == true) {
                holder.bindForTrue(task);
            } else if (hashMap.get(task.getUid()) == false) {
                holder.bindForFalse(task);
            } else {
                holder.bind(task);
            }
        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sPref = getPreferences(MODE_PRIVATE);
        for(int i = 0; i < mTasksForSave.size(); i++){
            Task task = mTasksForSave.get(i);
            long allTimeInFile = sPref.getLong(task.getUid() + "All", -25);
            long nowTimeInFile = sPref.getLong(task.getUid() + "Now", -25);
            if (allTimeInFile>0 && nowTimeInFile>0){
                MyTaskParams taskTimeParam = new MyTaskParams(task.getUid(), allTimeInFile, nowTimeInFile);
                AsyncSaveTask asyncSaveTask = new AsyncSaveTask();
                asyncSaveTask.execute(taskTimeParam);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putLong(task.getUid() + "All", -25);
                ed.putLong(task.getUid() + "Now", -25);
                ed.commit();
            }

        }
    }

    private void updateUI(){


        final List<Task> mTasks = new ArrayList<>();

        sPref = getPreferences(MODE_PRIVATE);

        /*CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);*/

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser !=null){
            Log.d("Us", "onComplete: good");
        } else {
            Log.d("Us", "onComplete: null");
        }

        mDatabaseReference.child(currentFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (mTasks.size() > 0) {
                            mTasks.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса User
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Task task = postSnapshot.getValue(Task.class);

                            SharedPreferences.Editor ed = sPref.edit();
                            ed.putLong(task.getUid() + "All", -25);
                            ed.putLong(task.getUid() + "Now", -25);
                            ed.commit();
                            mTasks.add(task);
                            mTasksForSave.add(task);
                            boolean resolution  = sPref.getBoolean(task.getUid()+"Bool", false);
                            hashMap.put(task.getUid(), resolution);
                        }
                        //публикуем данные в ListView
                        mAdapter = new TasksAdapter(mTasks);
                        mTasksRecyclerView.setAdapter(mAdapter);
                        lnMain.setVisibility(View.GONE);
                        //убираем View загрузки
                        //circular_progress.setVisibility(View.INVISIBLE);
                        //list_data.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    void saveBeginTask(String nameTask) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        String nameForBool = nameTask + "Bool";
        Calendar c = Calendar.getInstance();
        long d = c.getTimeInMillis();
        ed.putLong(nameTask, d);
        ed.putBoolean(nameForBool, true);
        ed.commit();
    }

    void saveStopTask(String nameTask) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        String nameForBool = nameTask + "Bool";
        String namePauseTime = nameTask + "pauseTime";
        ed.putLong(nameTask, 0);
        ed.putLong(namePauseTime, 0);
        ed.putBoolean(nameForBool, false);
        ed.commit();
    }

    void saveTimeTaskInPref(String nameTask, long allTime, long nowTime) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        String nameForAllTime = nameTask + "All";
        String nameForNowTime = nameTask + "Now";
        ed.putLong(nameForAllTime, allTime);
        ed.putLong(nameForNowTime, nowTime);
        ed.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_sign_out){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.out_title));
            builder.setMessage(getString(R.string.out_app_from_account));
            builder.setPositiveButton(getString(R.string.out), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                    startActivity(intent);

                }
            });

            builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initFirebase() {
        //инициализируем наше приложение для Firebase согласно параметрам в google-services.json
        // (google-services.json - файл, с настройками для firebase, кот. мы получили во время регистрации)
        FirebaseApp.initializeApp(this);
        //получаем точку входа для базы данных
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //получаем ссылку для работы с базой данных
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void saveTimeTask(String idTask, long allTime, long nowTime) {

        //Float sum = Float.parseFloat(editSumTask.getText().toString());
        //Long time = Long.parseLong(editTimeTask.getText().toString());

        if (currentFirebaseUser != null) {
            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(idTask)
                    .child("allTimeTask")
                    .setValue(allTime);

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(idTask)
                    .child("nowTimeTask")
                    .setValue(nowTime);
        } else {
            Log.d("Us", "onComplete: null");
        }
    }


    private static class MyTaskParams {
        String idTask;
        long allTime;
        long nowTime;

        MyTaskParams(String idTask, long allTime, long nowTime) {
            this.idTask = idTask;
            this.allTime = allTime;
            this.nowTime = nowTime;
        }
    }


    class AsyncSaveTask extends AsyncTask<MyTaskParams, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(MyTaskParams... params) {
            String idTask = params[0].idTask;
            long allTime = params[0].allTime;
            long nowTime = params[0].nowTime;
            saveTimeTask(idTask, allTime, nowTime);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }


    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.out_title));
        builder.setMessage(getString(R.string.out_app_from_app));
        builder.setPositiveButton(getString(R.string.out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onPause();
                finishAffinity();
                System.exit(0);

            }
        });

        builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }



}
