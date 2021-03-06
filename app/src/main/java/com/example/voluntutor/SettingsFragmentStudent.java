package com.example.voluntutor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class provides the tools accessed by the Settings Page fragment
 * and also allows for it to be displayed in accordance with the
 * commands recieved from the bottom navigation bar
 */
public class SettingsFragmentStudent extends Fragment {
    /**
     * Instantiates the UI view of a particular fragment
     * @param inflater inputted (Inflater) object which inflates views in a particular fragment
     * @param container inputted (ViewGroup) object which, when non-null, consists of
     * the parent view attached to a particular fragment
     * @param savedInstanceState inputted (Bundle) object which, when not-null, constructs a
     * particular fragment from a previously saved state
     * @return the View for the UI of a particular fragment, or NULL
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.settingsfragmentstudent, container, false);

        //sets the hints with current name and school
        setHints(view);

        //controls onClick for changing the student's name
        Button buttonName = view.findViewById(R.id.name_change_student_confirm);
        buttonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase fb = FirebaseDatabase.getInstance();
                DatabaseReference ref = fb.getReference("students");
                DatabaseReference nameRef = ref.child(MakeUserFragment.getID()).getRef().child("name").getRef();

                EditText et = view.findViewById(R.id.change_name_student_field);
                String newName = et.getText().toString();

                nameRef.setValue(newName);
            }
        });

        //controls onClick for changing the student's school
        Button buttonSchool = view.findViewById(R.id.school_change_student_confirm);
        buttonSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase fb = FirebaseDatabase.getInstance();
                DatabaseReference ref = fb.getReference("students");
                DatabaseReference nameRef = ref.child(MakeUserFragment.getID()).getRef().child("school").getRef();

                EditText et = view.findViewById(R.id.change_school_student_field);
                String newSchool = et.getText().toString();

                nameRef.setValue(newSchool);
            }
        });

        return view;
    }

    /**
     * This method shows the hints in the edit texts
     * @param v the view of the page
     */
    private void setHints(View v) {
        final EditText changeName = v.findViewById(R.id.change_name_student_field);
        final EditText changeSchool = v.findViewById(R.id.change_school_student_field);

        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference dr = fb.getReference("students");

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            /**
             * This method changes the name and school of a user if data is changed.
             * @param dataSnapshot Latest instance of Tutor data from Firebase
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(MakeUserFragment.getID())) {
                        Tutor t = ds.getValue(Tutor.class);
                        changeName.setHint("Current name: " + t.getName());
                        changeSchool.setHint("Current school: " + t.getSchool());
                    }
                }
            }

            /**
             * This method is called if the onDataChange method cannot be executed for any reason
             * @param databaseError error produced by the onDataChange method not being able to run
             */
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
