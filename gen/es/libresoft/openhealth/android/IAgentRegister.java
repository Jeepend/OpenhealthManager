/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/sancane/projects/openhealth/morfeo/openhealth/sources/AndroidOpenHealth/src/es/libresoft/openhealth/android/IAgentRegister.aidl
 */
package es.libresoft.openhealth.android;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
/**
 * Interface for register Aplications on to a remote service for
 * to get notifications about specific event of agents
 * (running in another process).
 */
public interface IAgentRegister extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements es.libresoft.openhealth.android.IAgentRegister
{
private static final java.lang.String DESCRIPTOR = "es.libresoft.openhealth.android.IAgentRegister";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IAgentRegister interface,
 * generating a proxy if needed.
 */
public static es.libresoft.openhealth.android.IAgentRegister asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof es.libresoft.openhealth.android.IAgentRegister))) {
return ((es.libresoft.openhealth.android.IAgentRegister)iin);
}
return new es.libresoft.openhealth.android.IAgentRegister.Stub.Proxy(obj);
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
case TRANSACTION_registerAgentCallback:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
es.libresoft.openhealth.android.IAgentCallbackService _arg1;
_arg1 = es.libresoft.openhealth.android.IAgentCallbackService.Stub.asInterface(data.readStrongBinder());
this.registerAgentCallback(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterCallback:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
es.libresoft.openhealth.android.IAgentCallbackService _arg1;
_arg1 = es.libresoft.openhealth.android.IAgentCallbackService.Stub.asInterface(data.readStrongBinder());
this.unregisterCallback(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements es.libresoft.openhealth.android.IAgentRegister
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
     * Call back to its clients when some event arrives on the agent identified by system_id.
     */
public void registerAgentCallback(java.lang.String system_id, es.libresoft.openhealth.android.IAgentCallbackService mc) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
_data.writeStrongBinder((((mc!=null))?(mc.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerAgentCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Remove a previously registered callback interface.
     */
public void unregisterCallback(java.lang.String system_id, es.libresoft.openhealth.android.IAgentCallbackService mc) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
_data.writeStrongBinder((((mc!=null))?(mc.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerAgentCallback = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterCallback = (IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
     * Call back to its clients when some event arrives on the agent identified by system_id.
     */
public void registerAgentCallback(java.lang.String system_id, es.libresoft.openhealth.android.IAgentCallbackService mc) throws android.os.RemoteException;
/**
     * Remove a previously registered callback interface.
     */
public void unregisterCallback(java.lang.String system_id, es.libresoft.openhealth.android.IAgentCallbackService mc) throws android.os.RemoteException;
}
