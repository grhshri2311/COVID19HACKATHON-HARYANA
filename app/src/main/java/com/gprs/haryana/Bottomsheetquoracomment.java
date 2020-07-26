package com.gprs.haryana;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Bottomsheetquoracomment extends BottomSheetDialogFragment {

    String phone, question;
    QuoraHelper quoraHelper;
    ArrayList<String> name, comment;
    ListView list;
    EditText input;
    CustomQuoraCommentAdapter c;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.bottomsheet_quora_comment, container, false);

        phone = this.getArguments().getString("user");
        question = this.getArguments().getString("question");
        name = new ArrayList<>();
        comment = new ArrayList<>();
        list = view.findViewById(R.id.commentlist);
        input = view.findViewById(R.id.comment);

        c = new CustomQuoraCommentAdapter(getActivity(), name, comment);
        list.setAdapter(c);

        final SharedPreferences pref = getActivity().getSharedPreferences("user", 0); // 0 - for private mode

        FirebaseDatabase.getInstance().getReference().child("Quora").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        QuoraHelper q = d.getValue(QuoraHelper.class);
                        if (q != null && q.getPhone().equals(phone) && question.equals(q.getTitle())) {
                            quoraHelper = q;
                            if (quoraHelper.getUser() != null) {
                                name.addAll(quoraHelper.getUser());
                                comment.addAll(quoraHelper.getAnswer());
                            }
                            c.notifyDataSetChanged();
                            input.setEnabled(true);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!input.getText().toString().isEmpty()) {
                        if (quoraHelper.getUser() == null) {
                            quoraHelper.setUser(new ArrayList<String>());
                            quoraHelper.setAnswer(new ArrayList<String>());
                        }
                        quoraHelper.getUser().add(pref.getString("user", ""));
                        quoraHelper.getAnswer().add(input.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Quora").child(quoraHelper.getTime()).setValue(quoraHelper);
                        c.notifyDataSetChanged();
                        dismiss();
                        Toast.makeText(getContext(), "Comment Submitted", Toast.LENGTH_SHORT).show();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        final String currentDateTime = dateFormat.format(new Date()); // Find todays date
                        FirebaseDatabase.getInstance().getReference().child("Notification").child(quoraHelper.getPhone()).child("Quora").child(currentDateTime).setValue(quoraHelper.getName() + " comment your question!");
                        input.setText("");
                    }
                    return true;
                }
                return false;
            }
        });


        return view;
    }

}