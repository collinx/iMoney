package com.fintechcodex.imoney;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SpeechRecog implements RecognitionListener {
    public ToggleButton button;
    String LOG_TAG = "SpeechRecog";
    Context context;
    AudioManager am;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView partResults, total;
    TableLayout table;

    float price = -1;
    Button remove;
    ScrollView scroll;
    Pattern quan = Pattern.compile("\\d+([.]\\d+)?");


    ArrayList<String> units = new ArrayList<String>();
    ArrayList<String> utters = new ArrayList<String>();
    private Intent recognizerIntent;
    private SpeechRecognizer speech = null;
    private StringBuffer suggestion;

    public SpeechRecog(Context context, TextView part_results, ToggleButton button, AudioManager audioManager, TextView t1, TextView t2, TextView t3, TextView t4,ScrollView scroll) {

        this.am = audioManager;
        this.context = context;
        this.button = button;
        this.tv1 = t1;
        this.tv2 = t2;
        this.tv3 = t3;
        this.tv4 = t4;
        this.partResults = part_results;

        this.total = total;
        this.suggestion = suggestion;
        this.scroll = scroll;

        startSpeech();
    }

    public void startSpeech() {

        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "en-us");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-us");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 20000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 20000);


        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    }

    public SpeechRecognizer getSpeechRe() {
        return speech;
    }

    public Intent getSpeechIn() {
        return recognizerIntent;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
    }

    @Override
    public void onRmsChanged(float rmsdB) {//used for visualization
        //    Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        if (rmsdB > 0) {
            tv1.setScaleY((float) (0.06 * rmsdB + 0.1 * (Math.random() % 6)));
            tv2.setScaleY((float) (1 - (0.04 * rmsdB + 0.1 * (Math.random() % 6))));
            tv3.setScaleY((float) (0.04 * rmsdB + 0.1 * (Math.random() % 6)));
            tv4.setScaleY((float) (1 - (0.04 * rmsdB + 0.1 * (Math.random() % 6))));
        } else {
            tv1.setScaleY(0.1f);
            tv2.setScaleY(0.1f);
            tv3.setScaleY(0.1f);
            tv4.setScaleY(0.1f);
        }
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");


    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
        Log.d(LOG_TAG, "FAILED " + errorMessage);



    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        float[] confidence = results.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
        partResults.setText(matches.get(0));
        String text = "";

        for (int i = 0; i < matches.size(); i++) {

            text += matches.get(i) + ",";


        }
        Log.v("OnResult", text);

        for (String temp : text.split(",")) {

            if (temp.split(" ").length == 1 && utters.size() > 0) {

                temp = utters.get(utters.size() - 1).split(",")[0] + " " + temp;
                Log.v("TAG", temp + utters.size());
            }
            if (temp.split(" ").length == 2 && utters.size() > 1) {
                temp = utters.get(utters.size() - 2).split(",")[0] + " " + temp;
                Log.v("TAG", temp);
            }

           // process_complete(temp.trim());
        }
        utters.add(text);

        if (button.isChecked()) {
            speech.destroy();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startSpeech();
            speech.startListening(recognizerIntent);
        }

    }

  /*  public void process_complete(String complete_input) {
        String orignalText = complete_input;
        int end_of_number; //end of first number found
        int start_of_number;
        String input = complete_input.replace("डेड ", "1.5 ");
        input = input.replace("डेढ़ ", "1.5 ");
        input = input.replace("ढाई ", "2.5 ");
        input = input.replace("ढ़ाई ", "2.5 ");
        input = input.replace("पाव ", "0.25 ");
        input = input.replace("पांव ", "0.25 ");
        input = input.replace("सवा ", "1.25 ");
        input = input.replace("सौ ", "100 ");
        input = input.replace("सो ", "100 ");
        input = input.replace("आधा ", "0.5 ");
        input = input.replace("एक ", "1 ");
        input = input.replace("दो ", "2 ");
        input = input.replace("तीन ", "3 ");
        input = input.replace("चार ", "4 ");
        input = input.replace("पांच ", "5 ");
        input = input.replace("छह ", "6 ");
        input = input.replace("साथ ", "7 ");
        input = input.replace("सात ", "7 ");
        input = input.replace("आठ ", "8 ");
        input = input.replace(":00 ", " ");

        Log.v("process_complete", input);
        String unitStr = "";
        float quantity = -1;
        String itemName = "";
        price = -1;
        int offset = 0;

        Matcher m1 = quan.matcher(input);  //m1 replaces 2 numbers appearing adjacent with a single number like ढ़ाई सौ = 2.5 100 = 250
        float temp = 1f;
        int temp_start, temp_end;
        int count;
        String[] separated = input.split(" ");


        if (m1.find()) {
            temp_start = m1.start();
            temp_end = m1.end();
            temp = Float.parseFloat(m1.group());
            count = m1.groupCount();

            while (m1.find()) {
                try {
                    if (m1.start() + offset == temp_end + 1) {
                        Log.v("inWhile_input", input);
                        input = input.replace(input.substring(temp_start, m1.end() + offset), Float.toString(temp * Float.parseFloat(m1.group())));
                        Log.v("inWhile", input.substring(temp_start, m1.end() + offset) + "---->" + Float.toString(temp * Float.parseFloat(m1.group())));
                        offset += Float.toString(temp * Float.parseFloat(m1.group())).length() - input.substring(temp_start, m1.end() + offset).length();

                    }
                    temp = Float.parseFloat(m1.group());
                    temp_start = m1.start() + offset;
                    temp_end = m1.end() + offset;
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }
        }
        Log.v("No 2 cont. number", input);
        Matcher matcher_quan = quan.matcher(input.trim());

        try {
            if (matcher_quan.find()) {
                quantity = Float.parseFloat(matcher_quan.group());
                start_of_number = matcher_quan.start();
                end_of_number = matcher_quan.end();
                if (matcher_quan.find()) {
                    if (input.endsWith("वाली")) {
                        //  unitStr = matcher_quan.group() + " वाली";
                        unitStr = "यूनिट";
                        price = Float.parseFloat(matcher_quan.group()) * quantity;
                        itemName = input.substring(end_of_number + 1, matcher_quan.start() - 1);
                        Log.v("Ends with Wali", "true");
                    } else {
                        //  unitStr = Float.toString(quantity) + " वाली";
                        quantity = Float.parseFloat(matcher_quan.group());
                        itemName = input.substring(matcher_quan.end() + 1);
                        Log.v("Ends with Wali", "false");
                        Log.v("TAG", "" + itemName);
                    }
                } else {
                    unitStr = input.substring(end_of_number + 1).split("\\s")[0];
                    if (start_of_number == 0) {
                        itemName = input.substring(end_of_number + 1).split(unitStr)[1].substring(1);
                        Log.v("itemName", itemName);
                    } else
                        itemName = input.substring(0, start_of_number).trim();
                }
            }
        } catch (Exception e) {
            Log.v("String Parser", "Illegal String");
        }

        LocalItemModel localItemModel = db.searchItem(itemName);
        if ((units.contains(unitStr) || unitStr.contains("वाली")) && localItemModel != null) {
            float curQuant = quantity;
            if (price == -1) {
                if (unitStr.contentEquals("ग्राम")) {
                    Log.v("String Parser", "Inside if = true");
                    price = db.searchItem(itemName).price * quantity * 0.001f;
                    curQuant = (float) (.001 * curQuant);

                } else {
                    price = db.searchItem(itemName).price * quantity;
                }
            }
            price = 0.01f * Math.round(100 * price);
            curQuant = 0.001f * Math.round(1000 * curQuant);
            LoginActivity.itemList.add(localItemModel);//Not used yet
            LoginActivity.quanList.add(curQuant);
            LoginActivity.unitList.add(unitStr);
            TableRow row = (TableRow) LayoutInflater.from(context).inflate(R.layout.row_layout, null);


            final TextView itemNameRow = (TextView) row.findViewById(R.id.item_name_row);
            TextView itemQuantityRow = (TextView) row.findViewById(R.id.item_quantity_row);
            TextView itemPriceRow = (TextView) row.findViewById(R.id.item_price_row);
            ImageView itemRemoveRow = (ImageView) row.findViewById(R.id.item_remove_row);

            itemRemoveRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = itemNameRow.getText().toString();
                    Log.v("TAG", name);
                    for (int i = 0; i < LoginActivity.itemList.size(); i++) {
                        if (name.equals(LoginActivity.itemList.get(i).itemName)) {
                            LoginActivity.itemList.remove(i);
                            LoginActivity.quanList.remove(i);
                            LoginActivity.unitList.remove(i);
                            break;
                        }
                    }
                    table.removeView((View) itemNameRow.getParent());
                }
            });
            Log.v("String Parser", "-" + unitStr + "-");


            itemQuantityRow.setText("" + quantity + " " + unitStr);
            itemNameRow.setText(itemName);
            itemPriceRow.setText("" + price);
            itemNameRow.setTextColor(0xFF000000);
            itemPriceRow.setTextColor(0xFF000000);
            itemQuantityRow.setTextColor(0xFF000000);

            table.addView(row);
            scroll.post(new Runnable() {
                @Override
                public void run() {
                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
            total.setText("" + (Float.valueOf(String.valueOf(total.getText())) + price));
        } else if (unitStr != "") {
            suggestion.delete(0, suggestion.length());
            suggestion.append(itemName);

        }

    }*/

    @Override
    public void onPartialResults(Bundle partialResults) {//blue wala textview
        Log.i(LOG_TAG, "onPartialResults");
        ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String partial_text;
        partial_text = "";
        partResults.setText(matches.get(0));
        for (String result : matches) {
            partial_text += matches + " ";

        }
        Log.v("OnPartial", partial_text);

    }


    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(LOG_TAG, "onEvent");
    }

    public String getErrorText(int errorCode) {
        String message;

        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                if (button.isChecked()) {
                    speech.destroy();
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startSpeech();
                    speech.startListening(recognizerIntent);
                }

                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                if (button.isChecked()) {
                    speech.destroy();
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startSpeech();
                    speech.startListening(recognizerIntent);
                }
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "Listening...";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                if (button.isChecked()) {
                    speech.destroy();
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startSpeech();
                    speech.startListening(recognizerIntent);
                }
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }

        return message;
    }
}
