package com.cyy.filemanager.test;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by study on 16/12/23.
 */

public class MusicService extends Binder {

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return super.onTransact(code, data, reply, flags);
    }

    public void start(){


    }

    public void stop(){

    }
}
