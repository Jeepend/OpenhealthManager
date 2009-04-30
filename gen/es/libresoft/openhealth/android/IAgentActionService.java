/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/sancane/projects/openhealth/morfeo/openhealth/sources/AndroidOpenHealth/src/es/libresoft/openhealth/android/IAgentActionService.aidl
 */
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
/*
Copyright (C) 2008-2009  Santiago Carot Nemesio
email: scarot@libresoft.es

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in 
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
public interface IAgentActionService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements IAgentActionService
{
private static final java.lang.String DESCRIPTOR = "IAgentActionService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IAgentActionService interface,
 * generating a proxy if needed.
 */
public static IAgentActionService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof IAgentActionService))) {
return ((IAgentActionService)iin);
}
return new IAgentActionService.Stub.Proxy(obj);
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
case TRANSACTION_sendEvent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
this.sendEvent(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getService:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.getService(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setService:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setService(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements IAgentActionService
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
     * Send event to some agent
     */
public void sendEvent(java.lang.String system_id, int eventType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
_data.writeInt(eventType);
mRemote.transact(Stub.TRANSACTION_sendEvent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * invoke get Service in agent
     */
public void getService(java.lang.String system_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
mRemote.transact(Stub.TRANSACTION_getService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * invoke set Service in agent
     */
public void setService(java.lang.String system_id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(system_id);
mRemote.transact(Stub.TRANSACTION_setService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendEvent = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getService = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setService = (IBinder.FIRST_CALL_TRANSACTION + 2);
}
/**
     * Send event to some agent
     */
public void sendEvent(java.lang.String system_id, int eventType) throws android.os.RemoteException;
/**
     * invoke get Service in agent
     */
public void getService(java.lang.String system_id) throws android.os.RemoteException;
/**
     * invoke set Service in agent
     */
public void setService(java.lang.String system_id) throws android.os.RemoteException;
}
