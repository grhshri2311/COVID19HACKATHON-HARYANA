package com.gprs.haryana;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelfAssess extends AppCompatActivity implements TextToSpeech.OnInitListener {

    ArrayList<String> answer;
    private Assistant watsonAssistant;
    private Response<SessionResponse> watsonAssistantSession;
    private TextToSpeech tts;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Boolean sound = false;
    CustomSelfAssessAdapter customSelfAssessAdapter;
    ListView listView;
    ArrayList<String> arrayList, arrayList1, question;
    ArrayList<Integer> toggle;
    private int status = 1;

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Language not supported", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Init failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void createServices() {
        if (checkInternetConnection()) {
            watsonAssistant = new Assistant("2019-02-28", new IamAuthenticator("S6C-4uOqeJyJzNaRBGP2PEp7PSJuNZ9C_OciE5JO3KoS"));
            watsonAssistant.setServiceUrl("https://api.eu-gb.assistant.watson.cloud.ibm.com/instances/fff0d44c-bc02-4bdc-965e-b83674194106");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_self_assess2);
        pref = getApplicationContext().getSharedPreferences("selfassesslanguage", 0); // 0 - for private mode
        editor = pref.edit();
        tts = new TextToSpeech(this, this);
        tts.setSpeechRate(1f);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.list);
        arrayList = new ArrayList<>();
        arrayList1 = new ArrayList<>();
        question = new ArrayList<>();
        toggle = new ArrayList<>();
        answer = new ArrayList<>();
        final ImageButton audio = findViewById(R.id.sound);
        audio.setTag("Off");
        pref = getApplicationContext().getSharedPreferences("language", 0);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio.getTag().equals("Off")) {
                    if (pref.getString("lang", "").equals("en")) {
                        audio.setTag("On");
                        sound = true;
                        audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));
                    }
                } else {
                    tts.stop();
                    sound = false;
                    audio.setTag("Off");
                    audio.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_black_24dp));
                }
            }
        });
        customSelfAssessAdapter = new CustomSelfAssessAdapter(this, toggle, arrayList1);

        listView.setAdapter(customSelfAssessAdapter);


        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.add(arrayList.get(arrayList.size() - 1));
                answer.add("yes");
                toggle.add(1);
                arrayList1.add(getString(R.string.yes));
                customSelfAssessAdapter.notifyDataSetChanged();
                listView.setSelection(arrayList1.size() - 1);
                sendMessage("yes", false);

            }
        });

        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question.add(arrayList.get(arrayList.size() - 1));
                answer.add(("no"));
                arrayList1.add(getString(R.string.no));
                toggle.add(1);
                customSelfAssessAdapter.notifyDataSetChanged();
                listView.setSelection(arrayList1.size() - 1);
                sendMessage("no", false);
            }
        });


        createServices();
        sendMessage("", true);
        findViewById(R.id.bottom).setVisibility(View.INVISIBLE);

    }

    private void sendMessage(String mes, boolean init) {

        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.bottom).setVisibility(View.INVISIBLE);
        if (init)
            new SelfAssess.send().execute(mes.trim(), "initial");
        else {
            new SelfAssess.send().execute(mes.trim(), "notinitial");
        }

    }


    private class receive extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(final String... params) {

            final String[] firstTranslation = new String[1];

            switch (params[0]) {
                case "If you are having difficulty breathing or experiencing other severe symptoms, call 011-23978046 or 1075":
                    firstTranslation[0] = getResources().getString(R.string.selfassess1);
                    break;
                case "Are you experiencing any of the following symptoms?\n" +
                        "1) severe difficulty breathing (for example, struggling for each breath, speaking in single words)\n" +
                        "2) severe chest pain\n" +
                        "3) having a very hard time waking up\n" +
                        "4) feeling confused\n" +
                        "5) lost consciousness":
                    firstTranslation[0] = getResources().getString(R.string.selfassess2);
                    break;
                case "Self-assessment result\n" +
                        "Please call 1075, +91-11-23978046 or email at ncov2019@gmail.com or go directly to your nearest emergency department.":
                    firstTranslation[0] = getResources().getString(R.string.selfassess3);
                    break;
                case "Are you experiencing any of the following symptoms (or a combination of these symptoms)?\n" +
                        "1) muscle aches\n" +
                        "2) fatigue\n" +
                        "3) headache\n" +
                        "4) sore throat\n" +
                        "5) runny nose\n" +
                        "Symptoms in young children may also be non-specific (for example, lethargy, poor feeding).":
                    firstTranslation[0] = getResources().getString(R.string.selfassess4);
                    break;
                case "Have you travelled outside of India in the last 14 days?":
                    firstTranslation[0] = getResources().getString(R.string.selfassess5);
                    break;
                case "Practice Opens in a new windowsocial distancing in order to decrease risk of COVID-19 transmission.\n" +
                        "\n" +
                        "You should also Opens in a new windowself-isolate if you:\n" +
                        "\n" +
                        "* are over 70\n" +
                        "* are immunocompromised (for example if you have HIV/AIDS, are receiving \n" +
                        "* immunosuppression therapy or treatment for cancer or have had a transplant)\n" +
                        "* have returned to India from travel in the last 14 days\n" +
                        "* have come into contact with someone with respiratory symptoms":
                    firstTranslation[0] = getResources().getString(R.string.selfassess6);
                    break;
                case "You should:\n" +
                        "\n" +
                        "* Only leave your home or see other people for essential reasons, and where possible seek services over the phone or online or ask for help from friends, family or neighbours. Learn more about Opens in a new windowself-isolating.\n" +
                        "* Continue to monitor your health after your symptoms started. Learn more about Opens in a new windowself-monitoring.\n" +
                        "* Not call your primary care provider.":
                    firstTranslation[0] = getResources().getString(R.string.selfassess7);
                    break;
                case "Contact either:\n" +
                        "\n" +
                        "your primary care provider (for example, family doctor) for a virtual assessment\n" +
                        "Please call at Toll free: 1075 or +91-11-23978046 and to speak with a COVID-19 Emergency Helpline\n" +
                        "You should:\n" +
                        "\n" +
                        "Only leave your home or see other people for essential reasons, and where possible seek services over the phone or online or ask for help from friends, family or neighbours. Learn more about Opens in a new windowself-isolating.":
                    firstTranslation[0] = getResources().getString(R.string.selfassess8);
                    break;
                case "Are you experiencing any of the following symptoms (or a combination of these symptoms)?\n" +
                        "1)fever\n" +
                        "2)new cough\n" +
                        "3)difficulty breathing (for example, struggling for each breath, cannot hold breath for more than 10 seconds)":
                    firstTranslation[0] = getResources().getString(R.string.selfassess9);
                    break;
                case "Self-assessment result:\n" +
                        "It is unlikely that you have COVID-19.":
                    firstTranslation[0] = getResources().getString(R.string.selfassess10);
                    break;
                case "Does someone you are in close contact with have COVID-19 (for example, someone in your household or workplace)?":
                    firstTranslation[0] = getResources().getString(R.string.selfassess11);
                    break;
                case "Self-assessment result\n" +
                        "Please seek clinical assessment for COVID-19 over the phone.":
                    firstTranslation[0] = getResources().getString(R.string.selfassess12);
                    break;
                case "Are you in close contact with a person who is sick with respiratory symptoms (for example, fever, cough or difficulty breathing) who recently travelled outside of India?":
                    firstTranslation[0] = getResources().getString(R.string.selfassess13);
                    break;
                case "Self-assessment result\n" +
                        "It is unlikely that you have COVID-19 but you should self-isolate at home until you are symptom-free.":
                    firstTranslation[0] = getResources().getString(R.string.selfassess14);
                    break;
                default:
                    firstTranslation[0] = params[0];
            }

            if (firstTranslation[0].contains("directly to your nearest emergency department"))
                firstTranslation[0] = getResources().getString(R.string.selfassess3);

            pref = getApplicationContext().getSharedPreferences("user", 0); // 0 - for private mode


            runOnUiThread(new Runnable() {
                public void run() {
                    arrayList1.add(firstTranslation[0]);
                    arrayList.add(params[0]);
                    toggle.add(0);
                    progressBar.setVisibility(View.GONE);
                    findViewById(R.id.bottom).setVisibility(View.VISIBLE);
                    customSelfAssessAdapter.notifyDataSetChanged();
                    listView.setSelection(arrayList.size() - 1);
                    try {
                        if (arrayList.get(arrayList.size() - 1).contains("result") || arrayList.get(arrayList.size() - 2).contains("result")) {
                            findViewById(R.id.bottom).setVisibility(View.INVISIBLE);
                            if (arrayList.get(arrayList.size() - 1).contains("It is unlikely that you have COVID-19") || arrayList.get(arrayList.size() - 2).contains("It is unlikely that you have COVID-19"))
                                status = 0;
                            if (arrayList.get(arrayList.size() - 1).contains("result")) {
                                question.add("Result");
                                answer.add(arrayList.get(arrayList.size() - 1));
                            } else if (arrayList.get(arrayList.size() - 2).contains("result")) {
                                answer.add(arrayList.get(arrayList.size() - 2));
                                question.add("Result");
                            }
                            pref = getSharedPreferences("user", 0);
                            if (!pref.getString("user", "").equals("")) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {

                                            UserRegistrationHelper userRegistrationHelper = dataSnapshot.getValue(UserRegistrationHelper.class);
                                            UserSelfAssessHelper userSelfAssessHelper = new UserSelfAssessHelper(userRegistrationHelper.getFname(), userRegistrationHelper.getPhone(), userRegistrationHelper.getEmail(), userRegistrationHelper.getRole(), userRegistrationHelper.getLat(), userRegistrationHelper.getLon(), question, answer, status);
                                            FirebaseDatabase.getInstance().getReference().child("Assess").child(userRegistrationHelper.getPhone()).setValue(userSelfAssessHelper);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SelfAssess.this, "Tested Locally", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
            });
            speakOut(firstTranslation[0]);

            return "Did translate";
        }
    }

    private void speakOut(String speech) {


        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {


            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
            }

            @Override
            public void onError(String s) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

        String text = speech;
        if (sound) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "Dummy String");
            }
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private class send extends AsyncTask<String, Void, String> {

        private boolean initialRequest = false;


        @Override
        protected String doInBackground(String... params) {


            if (params[1].equals("initial"))
                initialRequest = true;


            final String inputmessage = params[0];
            if (!this.initialRequest) {
                Message inputMessage = new Message();
                inputMessage.setMessage(params[0]);
                inputMessage.setId("1");

            } else {
                Message inputMessage = new Message();
                inputMessage.setMessage(inputmessage);
                inputMessage.setId("100");
                this.initialRequest = false;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });


            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        if (watsonAssistantSession == null) {
                            ServiceCall<SessionResponse> call = watsonAssistant.createSession(new CreateSessionOptions.Builder().assistantId("a44cdda7-3764-47d0-a54e-688c4f2b013a").build());
                            watsonAssistantSession = call.execute();
                        }

                        MessageInput input = new MessageInput.Builder()
                                .text(inputmessage)
                                .build();
                        MessageOptions options = new MessageOptions.Builder()
                                .assistantId("a44cdda7-3764-47d0-a54e-688c4f2b013a")
                                .input(input)
                                .sessionId(watsonAssistantSession.getResult().getSessionId())
                                .build();
                        Response<MessageResponse> response = watsonAssistant.message(options).execute();

                        if (response != null &&
                                response.getResult().getOutput() != null &&
                                !response.getResult().getOutput().getGeneric().isEmpty()) {

                            List<RuntimeResponseGeneric> responses = response.getResult().getOutput().getGeneric();

                            for (RuntimeResponseGeneric r : responses) {
                                final Message outMessage;
                                switch (r.responseType()) {
                                    case "text":
                                        new SelfAssess.receive().execute(r.text());
                                        break;
                                    default:
                                        Log.e("Error", "Unhandled message type");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();


            return "Did translate";
        }
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(this, " No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }

    }
}