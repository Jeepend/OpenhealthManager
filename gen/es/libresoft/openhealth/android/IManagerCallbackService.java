/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/sancane/projects/openhealth/morfeo/openhealth/sources/AndroidOpenHealth/src/es/libresoft/openhealth/android/IManagerCallbackService.aidl
 */
package es.libresoft.openhealth.android;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
/**
 * Manager notification interface, all clients registered with the remote service, will be
 * notified when next events occurs.
 * Note that this is a one-way interface so the server does not block waiting for the client.
 */
public interface IManagerCallbackService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.IManagerCallbackService
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.IManagerCallbackService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IManagerCallbackService interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.IManagerCallbackService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.IManagerCallbackService))) {
return ((es.libresoft.openhealth.android.IManagerCallbackService)iin);
}
return new es.libresoft.openhealth.android.IManagerCallbackService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_agentConnection:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.agentConnection(_arg0);
return true;
}
case TRANSACTION_agentDisconnection:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.agentDisconnection(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements es.libresoft.openhealth.android.IManagerCallbackService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Called when agent connect with the manager.
     */
public void agentConnection(java.lang.String system_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
mRemote.transact(Stub.TRANSACTION_agentConnection, _data, null, IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
     * Called when agent releases the association with the manager.
     */
public void agentDisconnection(java.lang.String system_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
mRemote.transact(Stub.TRANSACTION_agentDisconnection, _data, null, IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_agentConnection = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_agentDisconnection = (IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
     * Called when agent connect with the manager.
     */
public void agentConnection(java.lang.String system_id) throws android.os.RemoteException;
/**
     * Called when agent releases the association with the manager.
     */
public void agentDisconnection(java.lang.String system_id) throws android.os.RemoteException;
}
