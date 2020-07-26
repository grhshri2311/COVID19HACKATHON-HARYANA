package com.gprs.haryana;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomQuoraHomeItemAdapter extends ArrayAdapter {


    ArrayList<QuoraHelper> arrayList;
    HashMap<Integer, byte[]> hashMapimage;
    ArrayList<Boolean> likemap;
    private Activity context;
    ImageView imageView1;
    RelativeLayout linearLayout;
    Boolean sound = false;

    public CustomQuoraHomeItemAdapter(Activity context, ArrayList<QuoraHelper> arrayList) {
        super(context, R.layout.quora_home_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        likemap = new ArrayList<>();
        hashMapimage = new HashMap<>();
    }


    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;
        likemap.add(false);
        rowView = inflater.inflate(R.layout.quora_home_item, null, true);
        //this code gets references to objects in the listview_row.xml file
        TextView qtime = rowView.findViewById(R.id.date);
        TextView qname = rowView.findViewById(R.id.people_name);
        TextView qtitle = rowView.findViewById(R.id.question);
        TextView qmessage = rowView.findViewById(R.id.quoramessage);
        final ImageView like, comment;
        like = rowView.findViewById(R.id.like);
        comment = rowView.findViewById(R.id.comment);
        TextView likecount = rowView.findViewById(R.id.likecount);
        TextView commentcount = rowView.findViewById(R.id.commentcount);
        final SharedPreferences pref = context.getSharedPreferences("user", 0); // 0 - for private mode

        if (arrayList.get(position).getUserlike() != null) {
            for (int i = 0; i < arrayList.get(position).getUserlike().size(); i++) {
                if (arrayList.get(position).getUserlike().get(i).equals(pref.getString("user", ""))) {
                    like.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                    likemap.set(position, true);
                    break;
                }
            }
            likecount.setText(String.valueOf(arrayList.get(position).getUserlike().size()));
        }

        if (arrayList.get(position).getAnswer() != null) {
            commentcount.setText(String.valueOf(arrayList.get(position).getAnswer().size()));
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!likemap.get(position)) {
                    likemap.set(position, true);
                    like.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                    if (arrayList.get(position).getUserlike() == null)
                        arrayList.get(position).setUserlike(new ArrayList<String>());
                    arrayList.get(position).getUserlike().add(pref.getString("user", ""));
                    FirebaseDatabase.getInstance().getReference().child("Quora").child(arrayList.get(position).getTime()).setValue(arrayList.get(position));
                }
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("question", arrayList.get(position).getTitle());
                bundle.putString("user", arrayList.get(position).getPhone());

                BottomSheetDialogFragment f = new Bottomsheetquoracomment();
                f.setArguments(bundle);
                f.show(((FragmentActivity) context).getSupportFragmentManager(), "Dialog");
            }
        });

        qtime.setText(arrayList.get(position).getTime().substring(0, 10));
        qname.setText(arrayList.get(position).name + ", " + arrayList.get(position).getRole());
        qtitle.setText(arrayList.get(position).getTitle());
        qmessage.setText(arrayList.get(position).getMessage());


        linearLayout = rowView.findViewById(R.id.media);

        imageView1 = new ImageView(getContext());
        imageView1.setAdjustViewBounds(true);
        imageView1.setPadding(10, 10, 10, 10);
        imageView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        if (arrayList.get(position).isImage()) {
            linearLayout.addView(imageView1);
        }

        if (arrayList.get(position).isImage()) {
            if (hashMapimage.get(position) == null) {

                if (FirebaseStorage.getInstance().getReference().child("Quora").child(arrayList.get(position).getUri()) != null) {
                    FirebaseStorage.getInstance().getReference().child("Quora").child(arrayList.get(position).getUri()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            hashMapimage.put(position, bytes);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView1.setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
                }

            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(hashMapimage.get(position), 0, hashMapimage.get(position).length);
                imageView1.setImageBitmap(bitmap);

            }
        }

        final VideoView videoView = rowView.findViewById(R.id.vedio);
        final ImageView imageView = rowView.findViewById(R.id.play);
        if (arrayList.get(position).isVedio()) {


            final String extStorageDirectory = Environment.getExternalStorageDirectory()
                    .toString();
            File f = new File(extStorageDirectory + "/COVI19RELIEF/vedios/" + arrayList.get(position).getUri() + ".mp4");
            if (!f.exists()) {


                MediaController mediaController = new MediaController(rowView.getContext());
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);


                if (FirebaseStorage.getInstance().getReference().child("Quora").child(arrayList.get(position).getUri()) != null) {


                    FirebaseStorage.getInstance().getReference().child("Quora").child(arrayList.get(position).getUri()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            try {

                                File folder1 = new File(extStorageDirectory, "COVI19RELIEF");// Name of the folder you want to keep your file in the local storage.
                                folder1.mkdir(); //creating the folder
                                File folder2 = new File(folder1, "vedios");// Name of the folder you want to keep your file in the local storage.
                                folder2.mkdir(); //creating the folder
                                FileOutputStream fileOutputStream = new FileOutputStream(extStorageDirectory + "/COVI19RELIEF/vedios/" + arrayList.get(position).getUri() + ".mp4");
                                fileOutputStream.write(bytes);
                                start(position, videoView, imageView);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }

            } else {
                if (f.exists()) {
                    start(position, videoView, imageView);
                }

            }

        } else {
            linearLayout.removeView(videoView);
            linearLayout.removeView(imageView);
        }


        return rowView;


    }

    private void start(final int position, final VideoView videoView, final ImageView imageView) {

        final String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        videoView.setVideoPath(extStorageDirectory + "/COVI19RELIEF/vedios/" + arrayList.get(position).getUri() + ".mp4");

        imageView.setVisibility(View.INVISIBLE);
        videoView.start();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.start();
                    videoView.pause();
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    videoView.start();
                    imageView.setVisibility(View.INVISIBLE);
                }

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageView.setVisibility(View.VISIBLE);
                videoView.pause();
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                videoView.pause();
                imageView.setVisibility(View.VISIBLE);
            }
        }, 500);


    }
}
