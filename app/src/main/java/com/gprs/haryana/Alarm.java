package com.gprs.haryana;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.abs;

public class Alarm extends AppCompatActivity {

    AlertDialog alert;
    FloatingActionButton floatingActionButton;
    ArrayList<String> name, time;
    ArrayList<Boolean> onoff, repeat, deletea;
    String extStorageDirectory = Environment.getExternalStorageDirectory()
            .toString();

    long finalHrs = 0, finalSeconds = 0;
    Date date1 = null, date2;


    alarmAdapter alarmAdapter;
    File f = new File(extStorageDirectory + "/COVI19RELIEF/alarm/obj.dat");

    SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");

    String curtime = df.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm);

        if (!f.exists()) {
            try {

                File folder1 = new File(extStorageDirectory, "COVI19RELIEF");// Name of the folder you want to keep your file in the local storage.
                folder1.mkdir(); //creating the folder
                File folder2 = new File(folder1, "alarm");// Name of the folder you want to keep your file in the local storage.
                folder2.mkdir(); //creating the folder
                File file = new File(folder2, "obj.dat");
                file.createNewFile(); // creating the file inside the folder
            } catch (Exception e) {

            }
        }


        name = new ArrayList<>();
        time = new ArrayList<>();
        onoff = new ArrayList<>();
        deletea = new ArrayList<>();
        repeat = new ArrayList<>();


        loadMap();
        ListView listView = findViewById(R.id.alarmlist);

        alarmAdapter = new alarmAdapter(name, repeat, deletea, time, onoff, this);
        listView.setAdapter(alarmAdapter);

        TextView textView = findViewById(R.id.empty);
        listView.setEmptyView(textView);


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addalarm();
            }
        });

    }


    public String AlarmTime(TimePicker timePicker) {

        Integer alarmHours = timePicker.getCurrentHour();
        Integer alarmMinutes = timePicker.getCurrentMinute();
        String stringAlarmMinutes;

        if (alarmMinutes < 10) {
            stringAlarmMinutes = "0";
            stringAlarmMinutes = stringAlarmMinutes.concat(alarmMinutes.toString());
        } else {
            stringAlarmMinutes = alarmMinutes.toString();
        }
        String stringAlarmTime;


        if (alarmHours > 12) {
            alarmHours = alarmHours - 12;
            String stringAlarmHours = "";
            if (alarmHours < 10) {
                stringAlarmHours = "0";
                stringAlarmHours = stringAlarmHours.concat(alarmHours.toString());
            } else
                stringAlarmHours = alarmHours.toString();

            stringAlarmTime = stringAlarmHours.concat(":").concat(stringAlarmMinutes).concat(" PM");
        } else if (alarmHours == 12) {
            String stringAlarmHours = "";
            stringAlarmHours = alarmHours.toString();
            stringAlarmTime = stringAlarmHours.concat(":").concat(stringAlarmMinutes).concat(" PM");
        } else {
            String stringAlarmHours = "";
            if (alarmHours < 10) {
                stringAlarmHours = "0";
                stringAlarmHours = stringAlarmHours.concat(alarmHours.toString());
            } else
                stringAlarmHours = alarmHours.toString();
            stringAlarmTime = stringAlarmHours.concat(":").concat(stringAlarmMinutes).concat(" AM");
        }
        return stringAlarmTime;
    }

    private void saveMap() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(name);
            objectOutputStream.writeObject(time);
            objectOutputStream.writeObject(repeat);
            objectOutputStream.writeObject(deletea);
            objectOutputStream.writeObject(onoff);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadMap() {
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            name = (ArrayList<String>) objectInputStream.readObject();
            time = (ArrayList<String>) objectInputStream.readObject();
            repeat = (ArrayList<Boolean>) objectInputStream.readObject();
            deletea = (ArrayList<Boolean>) objectInputStream.readObject();
            onoff = (ArrayList<Boolean>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addalarm() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Alarm.this, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        builder.setCancelable(true);


        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.alarm_item, null, true);


        final EditText name1;
        final TimePicker timePicker;
        name1 = view.findViewById(R.id.alarmname);
        timePicker = view.findViewById(R.id.timePicker);
        TextView textView = view.findViewById(R.id.remaining);
        try {
            curtime = df.format(new Date());

            date1 = df.parse(curtime);


            date2 = df.parse(curtime.substring(0, 11) + AlarmTime(timePicker));

            long minutes = 0;

            if (date1.after(date2)) {

                long ltime = date2.getTime() + 24 * 60 * 60 * 1000;
                String newdate = df.format(new Date(ltime));
                date2 = df.parse(newdate);
                minutes = abs(((date1.getTime() / 1000) - (date2.getTime() / 1000)) / 60);

            } else {

                minutes = abs(((date2.getTime() / 1000) - (date1.getTime() / 1000)) / 60);

            }

            finalHrs = (minutes / 60);

            finalSeconds = minutes % 60;


        } catch (ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        textView.setText("Alarm in" + finalHrs + " hours " + finalSeconds + "minutes");

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view1, int hourOfDay, int minute) {
                TextView textView = view.findViewById(R.id.remaining);

                try {
                    curtime = df.format(new Date());
                    date1 = df.parse(curtime);


                    date2 = df.parse(curtime.substring(0, 11) + AlarmTime(timePicker));

                    long minutes = 0;

                    if (date1.after(date2)) {

                        long ltime = date2.getTime() + 24 * 60 * 60 * 1000;
                        String newdate = df.format(new Date(ltime));
                        date2 = df.parse(newdate);
                        minutes = abs(((date1.getTime() / 1000) - (date2.getTime() / 1000)) / 60);

                    } else {

                        minutes = abs(((date2.getTime() / 1000) - (date1.getTime() / 1000)) / 60);

                    }

                    finalHrs = (minutes / 60);

                    finalSeconds = minutes % 60;


                } catch (ParseException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                }

                textView.setText("Alarm in" + finalHrs + " hours " + finalSeconds + "minutes");
            }
        });

        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curtime = df.format(new Date());
                Switch repeat1 = view.findViewById(R.id.switch2);
                Switch del1 = view.findViewById(R.id.switch3);

                if (!name1.getText().toString().isEmpty()) {
                    curtime = df.format(new Date());
                    name.add(name1.getText().toString());
                    time.add(AlarmTime(timePicker));
                    onoff.add(true);
                    repeat1 = view.findViewById(R.id.switch2);
                    Switch del = view.findViewById(R.id.switch3);

                    repeat.add(repeat1.isChecked());
                    deletea.add(del.isChecked());
                    saveMap();
                    alarmAdapter.notifyDataSetChanged();


                    try {

                        date1 = df.parse(curtime);


                        date2 = df.parse(curtime.substring(0, 11) + time.get(time.size() - 1));
                        long minutes = 0;

                        if (date1.after(date2)) {

                            long ltime = date2.getTime() + 24 * 60 * 60 * 1000;
                            String newdate = df.format(new Date(ltime));
                            date2 = df.parse(newdate);
                            minutes = abs(((date1.getTime() / 1000) - (date2.getTime() / 1000)) / 60);

                        } else {

                            minutes = abs(((date2.getTime() / 1000) - (date1.getTime() / 1000)) / 60);

                        }

                        finalHrs = (minutes / 60);

                        finalSeconds = minutes % 60;


                    } catch (ParseException e) {

                        // TODO Auto-generated catch block

                        e.printStackTrace();

                    }

                    Toast.makeText(Alarm.this, "Alarm will go off in " + finalHrs + " hours and " + finalSeconds + " minutes.", Toast.LENGTH_LONG).show();
                    alert.hide();
                } else {
                    Toast.makeText(Alarm.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.hide();
            }
        });
        alert = builder.create();
        alert.setView(view);
        alert.show();

    }

}