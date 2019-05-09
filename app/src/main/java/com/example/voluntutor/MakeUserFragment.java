package com.example.voluntutor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class MakeUserFragment extends Fragment {
    public static String id;
    public int[] values = new int[4];
    public String dayOfWeek;
    public String subject;
    /**
     * returns the path to the tutor object of the person
     * @return the path
     */
    public static String getID() {
        return id;
    }
    public View view;
    public static void setID(String s) { id = s; }
    //initializes the Settings Fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.make_user, container, false);
        makeSpinner(view);

        //make tutor parts invisible
        final TextView tv1 = view.findViewById(R.id.session_label);
        final View v1 = view.findViewById(R.id.spinnerDay);
        final View v2 = view.findViewById(R.id.linearLayout);
        final View v3 = view.findViewById(R.id.linearLayout2);
        final View v4 = view.findViewById(R.id.subjects_label);
        final View v5 = view.findViewById(R.id.chooseSubs);
        final View v6 = view.findViewById(R.id.proceed);

        tv1.setVisibility(View.GONE);
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        v3.setVisibility(View.GONE);
        v4.setVisibility(View.GONE);
        v5.setVisibility(View.GONE);
        v6.setVisibility(View.GONE);

        //check student or tutor is checked
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.student_or_tutor);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.student_option) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.isTutor), false);
                    editor.commit();
                    tv1.setVisibility(View.GONE);
                    v1.setVisibility(View.GONE);
                    v2.setVisibility(View.GONE);
                    v3.setVisibility(View.GONE);
                    v4.setVisibility(View.GONE);
                    v5.setVisibility(View.GONE);
                }
                else if(checkedId == R.id.tutor_option) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.isTutor), true);
                    editor.commit();
                    tv1.setVisibility(View.VISIBLE);
                    v1.setVisibility(View.VISIBLE);
                    v2.setVisibility(View.VISIBLE);
                    v3.setVisibility(View.VISIBLE);
                    v4.setVisibility(View.VISIBLE);
                    v5.setVisibility(View.VISIBLE);
                }
            }
        });

        //deal with making tutor or student object
        Button button = (Button) view.findViewById(R.id.make_user_obj);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "click");
                EditText txtName = (EditText) view.findViewById(R.id.enter_name);
                EditText txtSchool = (EditText) view.findViewById(R.id.enter_school);

                if (txtName.getText().toString().equals("") || txtSchool.getText().toString().equals("")) {
                    Log.d("null", "Null");
                    Toast.makeText(getContext(), "You did not enter a name and school. Please fill out both.", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_pref_name), 0);
                    boolean isTutor = sharedPref.getBoolean(getString(R.string.isTutor), false);
                    if (isTutor) {
                        Log.d("is tutor", "yes");
                        String name = txtName.getText().toString();
                        String school = txtSchool.getText().toString();
                        int startHr = values[0];
                        int startMin = values[1];
                        int endHr = values[2];
                        int endMin = values[3];

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.name), name);
                        editor.putString(getString(R.string.school), school);
                        editor.commit();

                        TimeSlot ts = new TimeSlot(dayOfWeek, startHr, startMin, endHr, endMin);
                        Tutor t = new Tutor(name, school);
                        t.addTimeSlots(ts);

                        t.addSubject(subject);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, 6);
                        Sessions s = new Sessions(Long.toString(calendar.getTime().getTime()), "MAMS",
                                45, "Alan", name, false);
                        t.addSession(s);

                        Log.d("tutor", t.toString());

                        FirebaseDatabase fb = FirebaseDatabase.getInstance();
                        DatabaseReference ref = fb.getReference("tutors");
                        DatabaseReference nRef = ref.push();
                        nRef.setValue(t);
                        id = nRef.getKey();

                        Log.d("tutor key", id);

                        HomeFragment nextFrag= new HomeFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, nextFrag)
                                .addToBackStack(null)
                                .commit();
                    }
                    else {
                        Log.d("is tutor", "no");
                        String name = txtName.getText().toString();
                        String school = txtSchool.getText().toString();


                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.name), name);
                        editor.putString(getString(R.string.school), school);
                        editor.commit();

                        Student s = new Student(name, school);

                        Calendar calendar = Calendar.getInstance();
                        Sessions sess = new Sessions(Long.toString(calendar.getTime().getTime()), "MAMS",
                                45, "Alan", name, false);
                        s.addSession(sess);

                        Log.d("student", s.toString());

                        FirebaseDatabase fb = FirebaseDatabase.getInstance();
                        DatabaseReference ref = fb.getReference("/students");
                        DatabaseReference nRef = ref.push();
                        nRef.setValue(s);
                        id = nRef.getKey();

                        Log.d("student key", id);

                        HomeFragment nextFrag= new HomeFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, nextFrag)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        return view;
    }

    private void makeSpinner(View v) {
        Spinner spinnerD = (Spinner) v.findViewById(R.id.spinnerDay);
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this.getContext(),
                R.array.days, android.R.layout.simple_spinner_item);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerD.setAdapter(adapt);

        spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                dayOfWeek = s;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerSH = (Spinner) v.findViewById(R.id.spinnerSH);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSH.setAdapter(adapter);

        spinnerSH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                values[0] = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerSM = (Spinner) v.findViewById(R.id.spinnerSM);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.minutes, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSM.setAdapter(adapter2);

        spinnerSM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                values[1] = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerEH = (Spinner) v.findViewById(R.id.spinnerEH);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.hours, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEH.setAdapter(adapter3);

        spinnerEH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                values[2] = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinnerEM = (Spinner) v.findViewById(R.id.spinnerEM);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.minutes, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEM.setAdapter(adapter4);

        spinnerEM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                values[3] = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner subs = (Spinner) v.findViewById(R.id.chooseSubs);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this.getContext(),
                R.array.subjects, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subs.setAdapter(adapter5);
        subs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = (String) parent.getItemAtPosition(position);
                subject = s;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
