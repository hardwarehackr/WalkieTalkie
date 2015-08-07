package com.piccoli.hello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;


public class InitiateHelloActivity extends ActionBarActivity {

    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    AudioInpu audioCaptureThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //disconnects the existing wifi connection
        //WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //wifi.disconnect();

        //mManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
        //mChannel = mManager.initialize(this, getMainLooper(), null);
        //mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        //turns on wifi-direct programatically
        /*try {
            Class<?> wifiManager = Class
                    .forName("android.net.wifi.p2p.WifiP2pManager");

            Method method = wifiManager
                    .getMethod(
                            "enableP2p",
                            new Class[] { android.net.wifi.p2p.WifiP2pManager.Channel.class });

            method.invoke(mManager, mChannel);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        mIntentFilter = new IntentFilter();
        String me = "you";
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //setupWIFI();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_initiate_hello);

    }

    public void startAudioCapture(View view)
    {
        audioCaptureThread = new AudioInpu();
        Button startButton = (Button) findViewById(R.id.btnStart);
        startButton.setText("Hang up now");
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopCall(v);
            }
        });

        //Intent intentToStartMic = new Intent(this, HelloRequestService.class);

       // startService(intentToStartMic);
        audioCaptureThread.start();
    }

    public void stopCall(View view)
    {
        audioCaptureThread.Stop();
        Button startButton = (Button) findViewById(R.id.btnStart);

        startButton.setText("Start transmission");
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAudioCapture(v);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    /* /Discovers peers for use with wifi direct */
    public void setupWIFI()
    {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener()
        {
            //notifies you that the discovery process succeeded.
            @Override public void onSuccess()
            {
                //update the listbox of peers.
                TextView tvs = (TextView) findViewById(R.id.textViewStatus);
                tvs.setText("P2P Wifi discovery succeeded.");

            }
            @Override public void onFailure(int reasonCode)
            {
                TextView tvs = (TextView) findViewById(R.id.textViewStatus);
                tvs.setText("P2P Wifi discovery init failed");
            }
        }
        );
    }

    public void standby()
    {
        //listen for requests on socket
        //set up connection if request is received.
    }

    public void instigateCall()
    {
        //send request packet
        //wait for response
    }

    public void answerCall() {
        //listen for calls

    }

    public void duringCall(){
        //set up socket
        //while socket is still open
            //read from socket
            //play audio
            //read from microphone
            //send on socket.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initiate_hello, menu);
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
}
