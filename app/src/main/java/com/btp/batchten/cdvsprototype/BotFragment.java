package com.btp.batchten.cdvsprototype;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;


import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;


/**
 * A simple {@link Fragment} subclass.
 */
public class BotFragment extends Fragment implements com.btp.batchten.cdvsprototype.FileWriter.IWritinngCompletionCallback,TextToSpeech.OnInitListener {

    public static Chat chat;
    public Bot bot;
    Button mbutton;
    ListView mlist;
    EditText mtext;
    View view;
    ChatMsgAdapter madapter;
    private ProgressDialog progress;
    private TextToSpeech tts;
    com.btp.batchten.cdvsprototype.FileWriter.IWritinngCompletionCallback mCallback;

    public static BotFragment newInstance(int sectionNumber) {
        BotFragment fragment = new BotFragment();
        Bundle args = new Bundle();
        args.putInt("bot", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_bot, container, false);
        setupView();
        return view;
    }

    private void setupView() {
        tts = new TextToSpeech(getContext(), this);
        mlist=(ListView) view.findViewById(R.id.list);
        mbutton=(Button) view.findViewById(R.id.send);
        mtext=(EditText) view.findViewById(R.id.edit_msg);
        final ArrayList<ChatMsg> msg_item=new ArrayList<ChatMsg>();
        madapter=new ChatMsgAdapter(getContext(),msg_item);
        mlist.setAdapter(madapter);

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=mtext.getText().toString();
                String response = chat.multisentenceRespond(mtext.getText().toString());
                if (TextUtils.isEmpty(message)) {
                    return;
                }
                sendMsg(message);
                mimicOtherMsg(response);
                mtext.setText("");
                mlist.setSelection(madapter.getCount() - 1);
            }
        });
        writingFile();
    }

    private void sendMsg(String msg){
        ChatMsg chatMsg=new ChatMsg(msg,true);
        madapter.add(chatMsg);

    }

    private void mimicOtherMsg(String msg){
        ChatMsg chatMsg=new ChatMsg(msg,false);
        madapter.add(chatMsg);
        speakOut(msg);
    }

    private void sendMsg() {
        ChatMsg chatMsg = new ChatMsg(null, true);
        madapter.add(chatMsg);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMsg chatMsg = new ChatMsg(null, false);
        madapter.add(chatMsg);

    }

    @Override
    public void onWritingComplete() {
        AIMLProcessor.extension = new PCAIMLProcessorExtension();
        bot = new Bot("MY_BOT", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        String[] args = null;
        progress.dismiss();
    }


    public void writingFile() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("loading bot files");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
       // progress.show();
        FileWriter writer = new FileWriter(getContext(),this);
        writer.execute();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed");
        }

    }

    private void speakOut(String  message) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null,message);
        } else{
            tts.speak(message,TextToSpeech.QUEUE_ADD,null);
        }

    }

}
