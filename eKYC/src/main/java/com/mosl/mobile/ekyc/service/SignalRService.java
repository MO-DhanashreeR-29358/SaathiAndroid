package com.mosl.mobile.ekyc.service;

/**
 * Created by pramod on 13-10-2016.
 */

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mosl.mobile.ekyc.common.MOSLCommon;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import microsoft.aspnet.signalr.client.*;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler3;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler4;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.CRM;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.DOCUMENT_VERIFIED_STATUS_APPROVED;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.PP;


public class SignalRService extends Service{
    private String roomId;
    private String userName;
    public final static String MY_ACTION = "SIGNALR_SERVICE_ACTION";
    public final static String UI_UPDATE_ACTION = "UI_ACTION";
    public final static String SERVER_DISCONNECT_STATUS_ACTION = "SERVER_DISCONNECT_STATUS_ACTION";
    String TAG="SignalRService";
    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients

    private CountDownTimer timer;

    public SignalRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //System.setProperty("http.keepAlive", "false");
        mHandler = new Handler(Looper.getMainLooper());

        timer = new CountDownTimer(50000, 1000) {

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                notifyStartTimeoutTimerToUI(time,false);
                Log.d(TAG,"Timeout Seconds remaining: " + time);
            }

            public void onFinish() {
                disconnectConnection();
                cancel();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        //startSignalR(userId,roomId);
        return result;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        //mHubConnection.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        String roomId = intent.getStringExtra("roomId");
        String userName = intent.getStringExtra("userName");
        String requestFrom = intent.getStringExtra("requestFrom");
        String OldRoomID = intent.getStringExtra("OldRoomID");

        //if(requestFrom.equals("PARTNER")) {
            startSignalRPartner(userName, roomId, OldRoomID);
        //}
        //else{
        //    startSignalRDirect(userName, roomId, OldRoomID);
        //}
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    /**
     * method for clients (activities)
     */
    public void sendMessage(String message) {
        mHubProxy.invoke("sendMessage", roomId, userName, message).done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"sendMessage SENT!");
            }
        });
    }
    public void startCameraForMobileUser(String videoClientId) {
        mHubProxy.invoke("startCameraForMobileUser", roomId, videoClientId).done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"sendVideoClientInfo SENT!");
            }
        });
    }
    //for direct user
    public void notifyNewUser() {
        mHubProxy.invoke("notifyNewUser",roomId,"New User Arrived</br>").done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"notifyNewUser SENT!");
            }
        });
    }

    //to be called when new document is uploaded
    public void notifyNewUploadClient(String docName) {

        String msg = docName+" uploaded successfully.";
        mHubProxy.invoke("notifyNewUploadClient",roomId,msg).done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"notifyNewUploadedClient SENT!");
            }
        });
    }
    //to be called when new document is deleted
    public void notifyDocDeleted(String docName) {
        String msg = docName+" deleted successfully.";
        mHubProxy.invoke("notifyNewUploadClient",roomId,msg).done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"notifyDocDeleted SENT!");
            }
        });
    }
    //to be called to close old verifier if connected
    public void sendOldRoomId(String oldRoomId) {
        if(MOSLCommon.isBlank(oldRoomId)){
            return;
        }

        String msg = "Client connected to another verifier, please stop verification.";
        mHubProxy.invoke("notifyNewUploadClient",oldRoomId,msg).done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"notifyDocDeleted SENT!");
            }
        });
    }

    /**
     * used to send disconnect message to server for branch/partner
     *//*
    public void sendDisconnectMessage() {
        String msg = userName+ "Disconnected";
        mHubProxy.invoke("sendDisconnectMessage",roomId,msg).done(new Action<Void>() {

            @Override
            public void run(Void obj) throws Exception {
                Log.d(TAG,"sendDisconnectMessage SENT!");
            }
        });
    }*/

    public void disconnectConnection(){
        if(mHubConnection!=null) {
            mHubConnection.disconnect();
            notifyServerDisconnectToUI();
        }
    }
    /*private void startSignalRDirect(final String userName, String roomId, String oldRoomID) {
        this.userName = userName;
        this.roomId = roomId;

        // Create a new console logger
        Logger logger = new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                Log.d(TAG,(message));
            }
        };

        // Connect to the server
        String qs = "RoomID="+roomId+"&_="+new Date().getTime();
        mHubConnection = new HubConnection(WebServiceHandler.SIGNALR_URL, qs, true, logger);

        // Create the hub proxy
        mHubProxy = mHubConnection.createHubProxy("notifyHub");
        mHubProxy.on("addMessage",
                new SubscriptionHandler2<String,String>() {
                    @Override
                    public void run(String from, String message) {
                        //broadcast message returned back to sender. So check if the message received is actually sent by me
                        //do not display if
                        if(!(userName+": ").equals(from)) {
                            message = from + message;
                            updateUI(null, null, message, from, false, true);
                        }
                        releaseTimeoutTimer();
                    }
                }
                , String.class,String.class);
        mHubProxy.on("recieveApproveRejectNotification",
                new SubscriptionHandler3<String,String,String>() {
                    @Override
                    *//**
                     * responsetype=A,R
                     * uploadedproof=PAN, CQ etc
                     *//*
                    public void run(String responsetype, String rejectreason, String uploadedproof) {
                        boolean isVerifiedMsg=false;
                        boolean showChatMsg=false;
                        if(responsetype.equals("VerificationDone")){
                            isVerifiedMsg=true;
                            showChatMsg=true;
                            stopSelf();

                            if(mHubConnection!=null) {
                                mHubConnection.disconnect();
                            }
                        }
                        else{
                            releaseTimeoutTimer();
                        }
                        updateUI(responsetype,uploadedproof,rejectreason,null,isVerifiedMsg,showChatMsg);
                    }
                }
                , String.class,String.class,String.class);

        mHubProxy.on("sendConnectionState",
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(final String msg) {
                        updateUI(null,null,msg,null,false,true);
                        if(msg.equalsIgnoreCase("Partner disconnected")){
                            startTimeoutTimer();
                        }
                        else{
                            releaseTimeoutTimer();
                        }
                    }
                }
                , String.class);

        // Subscribe to the error event
        mHubConnection.error(new ErrorCallback() {

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

        // Subscribe to the connected event
        mHubConnection.connected(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG,"CONNECTED");
                notifyServerConnectToUI();
            }
        });

        // Subscribe to the closed event
        mHubConnection.closed(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG,"DISCONNECTED");
            }
        });

        SignalRFuture<Void> future  =mHubConnection.start(new ServerSentEventsTransport(mHubConnection.getLogger()));
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        notifyNewUser();
        sendOldRoomId(oldRoomID);
    }*/

    private void startSignalRPartner(final String userName, String roomId, String oldRoomID) {
        this.userName = userName;
        this.roomId = roomId;

        // Create a new console logger
        Logger logger = new Logger() {

            @Override
            public void log(String message, LogLevel level) {
                Log.d(TAG,(message));
            }
        };

        // Connect to the server
        String qs = "RoomID="+roomId+"&_="+new Date().getTime();
        mHubConnection = new HubConnection(WebServiceHandler.SIGNALR_URL, qs, true, logger);

        // Create the hub proxy
        mHubProxy = mHubConnection.createHubProxy("myChatHub");


        mHubProxy.subscribe(new Object() {
            public void addMessage(String from, String message) {

                //broadcast message returned back to sender. So check if the message received is actually sent by me
                //do not display if
                if(!(userName+": ").equals(from)) {
                    Log.d(TAG, from + message);
                    message = from + message;
                    updateUI(null, null, message, from, false, true);
                }
                releaseTimeoutTimer();
            }

            public void recieveApproveRejectNotification(String notificationID, String aprRejectMsg, String docName, String displaytext) {
                Log.d(TAG,"Docname" + ": " + docName);
                //Log.d(TAG,"Docname" + ": " + uploadedproof);
                if(notificationID.equals(DOCUMENT_VERIFIED_STATUS_APPROVED+"")){
                    //aprRejectMsg=displaytext+" "+aprRejectMsg;
                    aprRejectMsg="Document has been approved";
                }
                updateUI(notificationID,docName,aprRejectMsg,null,false,false);
                releaseTimeoutTimer();
            }

            public void DisconnectMessage(String message) {
                Log.d(TAG,message);
                updateUI(null,null,message,null,true,true);
                //sendDisconnectMessage();
                disconnectConnection();
                stopSelf();
                releaseTimeoutTimer();
            }
            public void sendConnectionState(String message) {
                Log.d(TAG,message);

                updateUI(null,null,message,null,false,true);
                if(message.equalsIgnoreCase("Partner disconnected")){
                    startTimeoutTimer();
                }
                else{
                    releaseTimeoutTimer();
                }
            }
        });


        // Subscribe to the error event
        mHubConnection.error(new ErrorCallback() {

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });

        // Subscribe to the connected event
        mHubConnection.connected(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG,"CONNECTED");
                notifyServerConnectToUI();
            }
        });

        // Subscribe to the closed event
        mHubConnection.closed(new Runnable() {

            @Override
            public void run() {
                Log.d(TAG,"DISCONNECTED");
            }
        });

        SignalRFuture<Void> future  =mHubConnection.start(new ServerSentEventsTransport(mHubConnection.getLogger()));
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        notifyNewUser();
        sendOldRoomId(oldRoomID);

    }

    /**
     *
     * @param notificationID
     * @param docName
     * @param message
     * @param from
     * @param isVerifiedMsg
     * @param showChatMsg: flag to display msg in chat section
     */
    public void updateUI(String notificationID,String docName,String message,String from,boolean isVerifiedMsg,boolean showChatMsg){
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("notificationID", notificationID);
        intent.putExtra("docName", docName);
        intent.putExtra("message", message);
        intent.putExtra("from", from);
        intent.putExtra("isVerifiedMsg", isVerifiedMsg);
        intent.putExtra("showChatMsg", showChatMsg);
        sendBroadcast(intent);
    }

    public void notifyServerDisconnectToUI(){
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("isServerDisconnected", true);
        sendBroadcast(intent);
    }
    public void notifyServerConnectToUI(){
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("isServerConnected", true);
        sendBroadcast(intent);
    }

    /**
     *
     * @param time
     * @param reset: clear timeout time when true
     */
    public void notifyStartTimeoutTimerToUI(long time,boolean reset){
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("serverTimeoutCall", true);
        intent.putExtra("remainTime", time);
        intent.putExtra("reset", reset);
        sendBroadcast(intent);
    }

    /**
     *
     */
    private void startTimeoutTimer(){
        timer.start();
    }
    private void releaseTimeoutTimer(){
        if(timer!=null) {
            timer.cancel();
            notifyStartTimeoutTimerToUI(0,true);
        }
    }
}