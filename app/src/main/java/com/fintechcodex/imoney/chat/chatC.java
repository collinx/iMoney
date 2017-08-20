package com.fintechcodex.imoney.chat;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.fintechcodex.imoney.LocalDatabase;
import com.fintechcodex.imoney.Message;
import com.fintechcodex.imoney.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIButton;

public class chatC extends BaseActivity implements AIButton.AIButtonListener {

    public static DatabaseHelper dbHelper;
    private ChatAdapter chatArrayAdapter;
    private ListView listView;
    public EditText chatText;
    private Button buttonSend;
    private AIDataService aiDataService;
    private ImageView clearView;
    private ImageView addMobile;
    public static final String TAG = chatC.class.getName();

    private AIButton aiButton;

    private Gson gson = GsonFactory.getGson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_c);
        dbHelper= new DatabaseHelper(this);

        TTS.init(getApplicationContext());
        initService( new LanguageConfig("en", "a11ea1d839e3446d84e402cb97cdadfb"));
        aiButton = (AIButton) findViewById(R.id.micButtonHome);
        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatAdapter(this, R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });



        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));

        aiButton.initialize(config);
        aiButton.setResultsListener(this);
        List<ChatMessage> msg = dbHelper.getMessage();
        if(msg!=null){
            for(int i=0;i<msg.size();i++){
                chatArrayAdapter.add(msg.get(i));
            }
        }
    }
    private void initService(final LanguageConfig selectedLanguage) {
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                lang,
                AIConfiguration.RecognitionEngine.System);


        aiDataService = new AIDataService(this, config);
    }
    @Override
    protected void onStart() {
        super.onStart();

        checkAudioRecordPermission();
    }
    public boolean sendChatMessage() {
        if(!TextUtils.isEmpty(chatText.getText())){
            sendRequest();
            dbHelper.addMessage(2,chatText.getText().toString());
            chatArrayAdapter.add(new ChatMessage(2, chatText.getText().toString()));
            chatText.setText("");

        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();

        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service
        aiButton.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // use this method to reinit connection to recognition service
        aiButton.resume();
    }
    private void sendRequest() {

        if (TextUtils.isEmpty(chatText.getText()) ) {
            return;
        }

        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];


                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);
                RequestExtras requestExtras = null;

                try {
                    return aiDataService.request(request, requestExtras);
                } catch (final AIServiceException e) {
                    aiError = new AIError(e);
                    return null;
                }
            }



            @Override
            protected void onPostExecute(final AIResponse response) {
                if (response != null) {
                    onResultText(response);
                } else {
                    onError(aiError);
                }
            }
        };

        task.execute(chatText.getText().toString());
    }



    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "onResult");
                chatArrayAdapter.add(new ChatMessage(2,response.getResult().getResolvedQuery()));
                chatArrayAdapter.add(new ChatMessage(1, response.getResult().getFulfillment().getSpeech()));
                dbHelper.addMessage(2,response.getResult().getResolvedQuery());
                dbHelper.addMessage(1,response.getResult().getFulfillment().getSpeech());
                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);
                TTS.speak(speech);

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }


            }

        });
    }

    public void onResultText(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Log.d(TAG, "onResultText");
                chatArrayAdapter.add(new ChatMessage(1, response.getResult().getFulfillment().getSpeech()));
                dbHelper.addMessage(1,response.getResult().getFulfillment().getSpeech());
                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());
                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);
                TTS.speak(speech);

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }


                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {

                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }

                    LocalDatabase data = new LocalDatabase(getApplicationContext());
                    if(response.getResult().getMetadata().getIntentName().equals("debit") && params.get("payee")!=null && params.get("amount")!=null && params.get("reason")!=null ){
                        Log.v("TAG",""+new Date().getTime()+ params.get("payee").getAsString()+params.get("reason").getAsString()+params.get("amount").getAsFloat());
                        data.saveDebit(new Message(new Date().getTime(),null,params.get("payee").getAsString(),params.get("reason").getAsString(),params.get("amount").getAsFloat()));
                    }else if(response.getResult().getMetadata().getIntentName().equals("debit")  && params.get("payee")!=null && params.get("amount")!=null  && params.get("reason")!=null ){
                        data.saveCredit(new Message(new Date().getTime(),null,params.get("payee").getAsString(),params.get("reason").getAsString(),params.get("amount").getAsFloat()));
                        Log.v("TAG",""+new Date().getTime()+ params.get("payee").getAsString()+params.get("reason").getAsString()+params.get("amount").getAsFloat());
                    }else if(response.getResult().getMetadata().getIntentName().equals("due")  && params.get("time")!=null && params.get("amount")!=null && params.get("reason")!=null  ){
                        data.saveDue(new Message(new Date().getTime(),null,params.get("time").getAsString(),params.get("reason").getAsString(),params.get("amount").getAsFloat()));
                        Log.v("TAG",""+new Date().getTime()+ params.get("time").getAsString()+params.get("reason").toString()+params.get("amount").getAsFloat());
                    }
                }



            }

        });
    }


    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");

            }
        });
    }



    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");

            }
        });
    }
}
