package br.com.correiam.checkmeta;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.correiam.checkmeta.dao.MetaDAO;
import br.com.correiam.checkmeta.dominio.Meta;


public class MetaActivity extends ActionBarActivity {

    private EditText etIdMeta;
    private EditText etName;
    private EditText etDescription;
    private EditText etState;
    private Button btnDueDate;
    private Button btnActualDate;
    private TextView tvState;
    private TextView tvActualDate;
    private AlertDialog delete_alert;
    static final int DUEDATE = 0;
    static final int ACTUALDATE = 1;
    private int year;
    private int month;
    private int day;


    private Meta oldMeta;
    private MetaDAO dao;
    int cur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta);

        etName = (EditText) findViewById(R.id.etNome);
        etDescription = (EditText) findViewById(R.id.etDescricao);
        btnDueDate = (Button) findViewById(R.id.btnDueDate);

        btnDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DUEDATE);
            }
        });
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(calendar.getTime());

            if(cur == DUEDATE){
                btnDueDate.setText(date);
            }
            else{
                btnActualDate.setText(date);
            }

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DUEDATE:
                cur = DUEDATE;
                return new DatePickerDialog(this,
                        mDateSetListener,
                        year, month, day);
            case ACTUALDATE:
                cur = ACTUALDATE;
                return new DatePickerDialog(this,
                        mDateSetListener,
                        year, month, day);
        }
        return null;
    }
}
