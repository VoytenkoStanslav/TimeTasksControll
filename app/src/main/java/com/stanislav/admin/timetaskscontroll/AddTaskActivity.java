package com.stanislav.admin.timetaskscontroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AddTaskActivity extends AppCompatActivity {

    private Button btnAddNewMiniTask;
    private LinearLayout lnForMiniTasks;
    private EditText editNameTask;
    private EditText editFullNameTask;
    private EditText editAnyTask;
    private EditText editMiniTaskOne;
    private EditText editMiniTaskTwo;
    private EditText editSumTask;
    private EditText editTimeTask;
    private CheckBox checkBoxForCount;
    private TextView tvTimeIncomeHour;
    private TextView tvPlanTime;
    private TextView tvPaymentIncome;
    private Spinner spinnerForTimeTask;
    private List<View> allEds;

    private Button btnAddTask;

    private Float incomePaymentDay;
    private Float incomePaymentHour;
    private Float incomePaymentMinute;

    int spinPos = 1;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;



    private int countTask = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initFirebase();


        allEds = new ArrayList<View>();

        lnForMiniTasks = (LinearLayout) findViewById(R.id.lnForMiniTasks) ;

        editNameTask = (EditText) findViewById(R.id.editNameTask);
        editFullNameTask = (EditText) findViewById(R.id.editFullNameTask);
        editAnyTask = (EditText) findViewById(R.id.editAnyText);
        tvTimeIncomeHour = (TextView) findViewById(R.id.tvTimeIncomeHour);
        tvPaymentIncome = (TextView) findViewById(R.id.tvPaymentIncom);
        editSumTask = (EditText) findViewById(R.id.editSumTask);
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
        editTimeTask = (EditText) findViewById(R.id.editTimeTask);
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
        tvPlanTime = (TextView) findViewById(R.id.tvPlanTime);
        spinnerForTimeTask = (Spinner) findViewById(R.id.spinnerForTimeTask);
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
        checkBoxForCount = (CheckBox) findViewById(R.id.checkBoxForCount);
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
                protectedTask();
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
        //инициализируем наше приложение для Firebase согласно параметрам в google-services.json
        // (google-services.json - файл, с настройками для firebase, кот. мы получили во время регистрации)
        FirebaseApp.initializeApp(this);
        //получаем точку входа для базы данных
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //получаем ссылку для работы с базой данных
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void createTaskCheck() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser !=null) {
        } else {
        }

        String nameTask = editNameTask.getText().toString();


        if (!nameTask.isEmpty()){

            float sum = Float.parseFloat(editSumTask.getText().toString());

            long time = Long.parseLong(editTimeTask.getText().toString());

            Calendar c = Calendar.getInstance();
            long dateCreated = c.getTimeInMillis();

            Task task = new Task(UUID.randomUUID().toString(),
                    editNameTask.getText().toString(),
                    editFullNameTask.getText().toString(),
                    editAnyTask.getText().toString(),
                    checkBoxForCount.isChecked(), sum, time, spinnerForTimeTask.getSelectedItemPosition(), dateCreated);

            mDatabaseReference.child(currentFirebaseUser.getUid()).child(task.getUid()).setValue(task);
            finish();

        } else {
            Toast.makeText(this, getString(R.string.not_edit_name_task), Toast.LENGTH_SHORT).show();
        }

    }

    private void createTaskNotCheck() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser !=null) {
        } else {
        }

        String nameTask = editNameTask.getText().toString();


        if (!nameTask.isEmpty()){

            float sum = 0;

            long time = 0;

            Calendar c = Calendar.getInstance();
            long dateCreated = c.getTimeInMillis();

            Task task = new Task(UUID.randomUUID().toString(),
                    editNameTask.getText().toString(),
                    editFullNameTask.getText().toString(),
                    editAnyTask.getText().toString(),
                    checkBoxForCount.isChecked(), sum, time, spinnerForTimeTask.getSelectedItemPosition(), dateCreated);

            mDatabaseReference.child(currentFirebaseUser.getUid()).child(task.getUid()).setValue(task);
            finish();

        } else {
            Toast.makeText(this, getString(R.string.not_edit_name_task), Toast.LENGTH_SHORT).show();
        }

    }

    private void protectedTask(){

        if (checkBoxForCount.isChecked()) {
            if (!editSumTask.getText().toString().isEmpty()) {
                if (!editTimeTask.getText().toString().isEmpty()) {
                    if (Integer.valueOf(editTimeTask.getText().toString()) != 0
                            && Float.valueOf(editSumTask.getText().toString()) != 0) {
                        createTaskCheck();
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
            createTaskNotCheck();
        }
    }
}
