package com.stanislav.admin.timetaskscontroll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

/**
 * Created by Admin on 03.10.2018.
 */

public class ModifyTaskActivity extends AppCompatActivity{

    private static final String EXTRA_TASK_ID =
            "com.stanislav.admin.timetaskscontroll.task_id";
    private static final String EXTRA_USER_ID =
            "com.stanislav.admin.timetaskscontroll.user_id";
    private static final String EXTRA_TASK_NAME = "task_name";
    private static final String EXTRA_TASK_FULL_NAME = "task_full_name";
    private static final String EXTRA_TASK_ANY = "task_any";
    private static final String EXTRA_TASK_CHB = "task_chb";
    private static final String EXTRA_TASK_SUM = "task_sum";
    private static final String EXTRA_TASK_PLAN_TIME = "task_plan_time";
    private static final String EXTRA_TASK_COUNT_SUM = "task_count_sum";
    private static final String EXTRA_TASK_ALL_TIME = "task_all_time";
    private static final String EXTRA_TASK_SPIN_POS = "task_spin_pos";
    private static final String EXTRA_TASK_DATE_CREATED = "task_date_created";


    private LinearLayout lnForMiniTasks;
    private EditText editNameTask;
    private EditText editFullNameTask;
    private EditText editAnyTask;
    private EditText editSumTask;
    private EditText editTimeTask;
    private TextView tvPlanTime;
    private TextView tvTimeIncomeHour;
    private CheckBox checkBoxForCount;
    private Spinner spinnerForTimeTask;
    private TextView tvPaymentIncome;



    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    String taskId;
    String userId;
    private String taskName;
    private String taskFullName;
    private String taskAny;
    private boolean taskChb;
    private float taskSum;
    private long taskPlanTime;
    private String taskCountSum;
    private long allTime;
    private int spinPos;
    private long dateCreatedTask;

    private Float incomePaymentDay;
    private Float incomePaymentHour;
    private Float incomePaymentMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initFirebase();

        taskId = (String) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        taskName = (String) getIntent().getSerializableExtra(EXTRA_TASK_NAME);
        taskFullName = (String) getIntent().getSerializableExtra(EXTRA_TASK_FULL_NAME);
        taskAny = (String) getIntent().getSerializableExtra(EXTRA_TASK_ANY);
        taskChb = (boolean) getIntent().getSerializableExtra(EXTRA_TASK_CHB);
        taskSum = (float) getIntent().getSerializableExtra(EXTRA_TASK_SUM);
        taskPlanTime = (long) getIntent().getSerializableExtra(EXTRA_TASK_PLAN_TIME);
        taskCountSum = (String) getIntent().getSerializableExtra(EXTRA_TASK_COUNT_SUM);
        userId = (String) getIntent().getSerializableExtra(EXTRA_USER_ID);
        allTime = (long) getIntent().getSerializableExtra(EXTRA_TASK_ALL_TIME);
        spinPos = (int) getIntent().getSerializableExtra(EXTRA_TASK_SPIN_POS);
        dateCreatedTask = (long) getIntent().getSerializableExtra(EXTRA_TASK_DATE_CREATED);

        lnForMiniTasks = (LinearLayout) findViewById(R.id.lnForMiniTasks) ;

        editSumTask = (EditText) findViewById(R.id.editSumTask);
        editSumTask.setText(String.valueOf(taskSum));

        spinnerForTimeTask = (Spinner) findViewById(R.id.spinnerForTimeTask);
        spinnerForTimeTask.setSelection(spinPos);

        editTimeTask = (EditText) findViewById(R.id.editTimeTask);
        editTimeTask.setText(String.valueOf(taskPlanTime));

        editNameTask = (EditText) findViewById(R.id.editNameTask);
        editNameTask.setText(taskName.toString());

        editFullNameTask = (EditText) findViewById(R.id.editFullNameTask);
        editFullNameTask.setText(taskFullName.toString());

        editAnyTask = (EditText) findViewById(R.id.editAnyText);
        editAnyTask.setText(taskAny.toString());

        tvTimeIncomeHour = (TextView) findViewById(R.id.tvTimeIncomeHour);

        tvPaymentIncome = (TextView) findViewById(R.id.tvPaymentIncom);
        tvPaymentIncome.setText(taskCountSum);

        tvPlanTime = (TextView) findViewById(R.id.tvPlanTime);

        if(taskChb){
            editSumTask.setVisibility(View.VISIBLE);
            editTimeTask.setVisibility(View.VISIBLE);
            tvTimeIncomeHour.setVisibility(View.VISIBLE);
            tvPlanTime.setVisibility(View.VISIBLE);
            tvPaymentIncome.setVisibility(View.VISIBLE);
            spinnerForTimeTask.setVisibility(View.VISIBLE);
        } else {
            editSumTask.setVisibility(View.GONE);
            editTimeTask.setVisibility(View.GONE);
            tvTimeIncomeHour.setVisibility(View.GONE);
            tvPlanTime.setVisibility(View.GONE);
            tvPaymentIncome.setVisibility(View.GONE);
            spinnerForTimeTask.setVisibility(View.GONE);
        }

        checkBoxForCount = (CheckBox) findViewById(R.id.checkBoxForCount);
        checkBoxForCount.setChecked(taskChb);
        checkBoxForCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editSumTask.setVisibility(View.VISIBLE);
                    editTimeTask.setVisibility(View.VISIBLE);
                    tvTimeIncomeHour.setVisibility(View.VISIBLE);
                    tvPlanTime.setVisibility(View.VISIBLE);
                    tvPaymentIncome.setVisibility(View.VISIBLE);
                    spinnerForTimeTask.setVisibility(View.VISIBLE);
                } else {
                    editSumTask.setVisibility(View.GONE);
                    editTimeTask.setVisibility(View.GONE);
                    tvTimeIncomeHour.setVisibility(View.GONE);
                    tvPlanTime.setVisibility(View.GONE);
                    tvPaymentIncome.setVisibility(View.GONE);
                    spinnerForTimeTask.setVisibility(View.GONE);
                }
            }
        });

        spinnerForTimeTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!editSumTask.getText().toString().isEmpty() && !editTimeTask.getText().toString().isEmpty()
                        && Float.valueOf(editTimeTask.getText().toString()) != 0) {
                    float sum = Float.valueOf(editSumTask.getText().toString());
                    int time = Integer.valueOf(editTimeTask.getText().toString());
                    if (position == 0) {
                        incomePaymentDay = sum / time;
                        incomePaymentHour = sum / (time * 24);
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentHour)
                                + " " + getString(R.string.hour) + " / "
                                + new DecimalFormat("#0.00").format(incomePaymentDay)
                                + " " + getString(R.string.day));
                    } else if (position == 1) {
                        incomePaymentHour = sum / time;
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentHour) + " " + getString(R.string.hour));
                    } else {
                        incomePaymentMinute = sum / time;
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentMinute) + " " + getString(R.string.minute));
                    }
                } else {
                    tvPaymentIncome.setText(getString(R.string.payment_income));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editSumTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editTimeTask.getText().toString().isEmpty() && !s.toString().isEmpty()
                        && Float.valueOf(editTimeTask.getText().toString()) != 0){
                    int timePlan = Integer.valueOf(editTimeTask.getText().toString());
                    float sum;
                    if(s.toString().isEmpty()){
                        sum = 0;
                    } else {
                        sum = Float.valueOf(s.toString());
                    }

                    if (spinnerForTimeTask.getSelectedItemPosition() == 0) {
                        incomePaymentDay = sum / timePlan;
                        incomePaymentHour = sum / (timePlan * 24);
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentHour)
                                + " " + getString(R.string.hour) + " / "
                                + new DecimalFormat("#0.00").format(incomePaymentDay)
                                + " " + getString(R.string.day));
                    } else if (spinnerForTimeTask.getSelectedItemPosition() == 1) {
                        incomePaymentHour = sum / timePlan;
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentHour) + " " + getString(R.string.hour));
                    } else {
                        incomePaymentMinute = sum / timePlan;
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentMinute) + " " + getString(R.string.minute));
                    }
                } else {
                    tvPaymentIncome.setText(getString(R.string.payment_income));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                int p = str.indexOf(".");
                if (p != -1) {
                    String tmpStr = str.substring(p);
                    if (tmpStr.length() == 4) {
                        s.delete(s.length()-1, s.length());
                    }
                }
            }
        });

        editTimeTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!editSumTask.getText().toString().isEmpty() && !s.toString().isEmpty()
                        && Float.valueOf(s.toString()) != 0){
                    float sumPlan = Float.valueOf(editSumTask.getText().toString());
                    int time = Integer.valueOf(s.toString());
                    if (spinnerForTimeTask.getSelectedItemPosition() == 0) {
                        incomePaymentDay = sumPlan / time;
                        incomePaymentHour = sumPlan / (time * 24);
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentHour)
                                + " " + getString(R.string.hour) + " / "
                                + new DecimalFormat("#0.00").format(incomePaymentDay)
                                + " " + getString(R.string.day));
                    } else if (spinnerForTimeTask.getSelectedItemPosition() == 1) {
                        incomePaymentHour = sumPlan / time;
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentHour) + " " + getString(R.string.hour));
                    } else {
                        incomePaymentMinute = sumPlan / time;
                        tvPaymentIncome.setText(new DecimalFormat("#0.00").format(incomePaymentMinute) + " " + getString(R.string.minute));
                    }
                } else {
                    tvPaymentIncome.setText(getString(R.string.payment_income));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public static Intent newIntent(Context packageContext, String taskId, String taskName, String taskFullName,
                                   String taskAny, boolean taskChb, float taskSum, long taskPlanTime,
                                   String taskCountSum, String userId, long allTime, int spinPos, long dateCreatedTask) {
        Intent intent = new Intent(packageContext, ModifyTaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        intent.putExtra(EXTRA_TASK_NAME, taskName);
        intent.putExtra(EXTRA_TASK_FULL_NAME, taskFullName);
        intent.putExtra(EXTRA_TASK_ANY, taskAny);
        intent.putExtra(EXTRA_TASK_CHB, taskChb);
        intent.putExtra(EXTRA_TASK_SUM, taskSum);
        intent.putExtra(EXTRA_TASK_PLAN_TIME, taskPlanTime);
        intent.putExtra(EXTRA_TASK_COUNT_SUM, taskCountSum);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TASK_ALL_TIME, allTime);
        intent.putExtra(EXTRA_TASK_SPIN_POS, spinPos);
        intent.putExtra(EXTRA_TASK_DATE_CREATED, dateCreatedTask);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ok:
                protectedModifyTask();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }


    private void modifyTaskCheck() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser !=null) {
        } else {
        }

        String nameTask = editNameTask.getText().toString();


        if (!nameTask.isEmpty()){

            float sum = Float.parseFloat(editSumTask.getText().toString());

            long time = Long.parseLong(editTimeTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("nameTask")
                    .setValue(editNameTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("anyText")
                    .setValue(editAnyTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("checkSumTask")
                    .setValue(checkBoxForCount.isChecked());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("dateCreateTask")
                    .setValue(dateCreatedTask);

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("fullNameTask")
                    .setValue(editFullNameTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("spinPos")
                    .setValue(spinnerForTimeTask.getSelectedItemPosition());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("sum")
                    .setValue(sum);

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("timeTask")
                    .setValue(time);

            finish();
        } else {
            Toast.makeText(this, getString(R.string.not_edit_name_task), Toast.LENGTH_SHORT).show();
        }

    }

    private void modifyTaskNotCheck() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser !=null) {
        } else {
        }

        String nameTask = editNameTask.getText().toString();


        if (!nameTask.isEmpty()){

            float sum = 0;

            long time = 0;

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("nameTask")
                    .setValue(editNameTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("anyText")
                    .setValue(editAnyTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("checkSumTask")
                    .setValue(checkBoxForCount.isChecked());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("dateCreateTask")
                    .setValue(dateCreatedTask);

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("fullNameTask")
                    .setValue(editFullNameTask.getText().toString());

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("spinPos")
                    .setValue(spinPos);

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("sum")
                    .setValue(sum);

            mDatabaseReference.child(currentFirebaseUser.getUid())
                    .child(taskId)
                    .child("timeTask")
                    .setValue(time);
            finish();

        } else {
            Toast.makeText(this, getString(R.string.not_edit_name_task), Toast.LENGTH_SHORT).show();
        }

    }

    private void protectedModifyTask(){

        if (checkBoxForCount.isChecked()) {
            if (!editSumTask.getText().toString().isEmpty()) {
                if (!editTimeTask.getText().toString().isEmpty()) {
                    if (Integer.valueOf(editTimeTask.getText().toString()) != 0
                            && Float.valueOf(editSumTask.getText().toString()) != 0) {
                        modifyTaskCheck();
                    } else {
                        Toast.makeText(this, getString(R.string.sum_time_not_null), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.not_edit_time_is_chek), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.not_edit_sum_is_chek), Toast.LENGTH_SHORT).show();
            }
        } else {
            modifyTaskNotCheck();
        }
    }

}
