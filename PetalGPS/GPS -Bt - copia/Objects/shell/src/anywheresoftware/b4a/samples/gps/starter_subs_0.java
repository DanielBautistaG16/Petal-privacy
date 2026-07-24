package anywheresoftware.b4a.samples.gps;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.*;

public class starter_subs_0 {


public static RemoteObject  _admin_devicefound(RemoteObject _name,RemoteObject _macaddress) throws Exception{
try {
		Debug.PushSubsStack("admin_DeviceFound (starter) ","starter",2,starter.processBA,starter.mostCurrent,34);
if (RapidSub.canDelegate("admin_devicefound")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","admin_devicefound", _name, _macaddress);}
Debug.locals.put("Name", _name);
Debug.locals.put("MacAddress", _macaddress);
 BA.debugLineNum = 34;BA.debugLine="Private Sub admin_DeviceFound (Name As String, Mac";
Debug.ShouldStop(2);
 BA.debugLineNum = 35;BA.debugLine="Log($\"Device found: ${Name}\"$)";
Debug.ShouldStop(4);
starter.mostCurrent.__c.runVoidMethod ("LogImpl","22031617",(RemoteObject.concat(RemoteObject.createImmutable("Device found: "),starter.mostCurrent.__c.runMethod(true,"SmartStringFormatter",(Object)(BA.ObjectToString("")),(Object)((_name))),RemoteObject.createImmutable(""))),0);
 BA.debugLineNum = 36;BA.debugLine="If Name = \"HC-06\" Or Name = \"RIEGO2\" Or Name = \"Pe";
Debug.ShouldStop(8);
if (RemoteObject.solveBoolean("=",_name,BA.ObjectToString("HC-06")) || RemoteObject.solveBoolean("=",_name,BA.ObjectToString("RIEGO2")) || RemoteObject.solveBoolean("=",_name,BA.ObjectToString("Petal"))) { 
 BA.debugLineNum = 37;BA.debugLine="Log(\"Trying to connect...\")";
Debug.ShouldStop(16);
starter.mostCurrent.__c.runVoidMethod ("LogImpl","22031619",RemoteObject.createImmutable("Trying to connect..."),0);
 BA.debugLineNum = 38;BA.debugLine="admin.CancelDiscovery";
Debug.ShouldStop(32);
starter._admin.runVoidMethod ("CancelDiscovery");
 BA.debugLineNum = 39;BA.debugLine="serial.Connect(MacAddress)";
Debug.ShouldStop(64);
starter._serial.runVoidMethod ("Connect",starter.processBA,(Object)(_macaddress));
 };
 BA.debugLineNum = 41;BA.debugLine="End Sub";
Debug.ShouldStop(256);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _admin_discoveryfinished() throws Exception{
try {
		Debug.PushSubsStack("admin_DiscoveryFinished (starter) ","starter",2,starter.processBA,starter.mostCurrent,43);
if (RapidSub.canDelegate("admin_discoveryfinished")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","admin_discoveryfinished");}
 BA.debugLineNum = 43;BA.debugLine="Private Sub admin_DiscoveryFinished";
Debug.ShouldStop(1024);
 BA.debugLineNum = 44;BA.debugLine="connecting = False";
Debug.ShouldStop(2048);
starter._connecting = starter.mostCurrent.__c.getField(true,"False");
 BA.debugLineNum = 45;BA.debugLine="End Sub";
Debug.ShouldStop(4096);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _application_error(RemoteObject _error,RemoteObject _stacktrace) throws Exception{
try {
		Debug.PushSubsStack("Application_Error (starter) ","starter",2,starter.processBA,starter.mostCurrent,82);
if (RapidSub.canDelegate("application_error")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","application_error", _error, _stacktrace);}
Debug.locals.put("Error", _error);
Debug.locals.put("StackTrace", _stacktrace);
 BA.debugLineNum = 82;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
Debug.ShouldStop(131072);
 BA.debugLineNum = 83;BA.debugLine="Return True";
Debug.ShouldStop(262144);
if (true) return starter.mostCurrent.__c.getField(true,"True");
 BA.debugLineNum = 84;BA.debugLine="End Sub";
Debug.ShouldStop(524288);
return RemoteObject.createImmutable(false);
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _ast_newtext(RemoteObject _text) throws Exception{
try {
		Debug.PushSubsStack("ast_NewText (starter) ","starter",2,starter.processBA,starter.mostCurrent,66);
if (RapidSub.canDelegate("ast_newtext")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","ast_newtext", _text);}
Debug.locals.put("Text", _text);
 BA.debugLineNum = 66;BA.debugLine="Private Sub ast_NewText (Text As String)";
Debug.ShouldStop(2);
 BA.debugLineNum = 67;BA.debugLine="CallSub2(Main, \"MessageFromDevice\", Text)";
Debug.ShouldStop(4);
starter.mostCurrent.__c.runMethodAndSync(false,"CallSubNew2",starter.processBA,(Object)((starter.mostCurrent._main.getObject())),(Object)(BA.ObjectToString("MessageFromDevice")),(Object)((_text)));
 BA.debugLineNum = 68;BA.debugLine="End Sub";
Debug.ShouldStop(8);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _ast_terminated() throws Exception{
try {
		Debug.PushSubsStack("ast_Terminated (starter) ","starter",2,starter.processBA,starter.mostCurrent,70);
if (RapidSub.canDelegate("ast_terminated")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","ast_terminated");}
 BA.debugLineNum = 70;BA.debugLine="Private Sub ast_Terminated";
Debug.ShouldStop(32);
 BA.debugLineNum = 71;BA.debugLine="connected = False";
Debug.ShouldStop(64);
starter._connected = starter.mostCurrent.__c.getField(true,"False");
 BA.debugLineNum = 72;BA.debugLine="CallSub(Main, \"SetState\")";
Debug.ShouldStop(128);
starter.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",starter.processBA,(Object)((starter.mostCurrent._main.getObject())),(Object)(RemoteObject.createImmutable("SetState")));
 BA.debugLineNum = 73;BA.debugLine="End Sub";
Debug.ShouldStop(256);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _connect() throws Exception{
try {
		Debug.PushSubsStack("Connect (starter) ","starter",2,starter.processBA,starter.mostCurrent,20);
if (RapidSub.canDelegate("connect")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","connect");}
 BA.debugLineNum = 20;BA.debugLine="Public Sub Connect";
Debug.ShouldStop(524288);
 BA.debugLineNum = 21;BA.debugLine="admin.StartDiscovery";
Debug.ShouldStop(1048576);
starter._admin.runVoidMethod ("StartDiscovery");
 BA.debugLineNum = 22;BA.debugLine="connecting = True";
Debug.ShouldStop(2097152);
starter._connecting = starter.mostCurrent.__c.getField(true,"True");
 BA.debugLineNum = 24;BA.debugLine="CallSub(Main, \"SetState\")";
Debug.ShouldStop(8388608);
starter.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",starter.processBA,(Object)((starter.mostCurrent._main.getObject())),(Object)(RemoteObject.createImmutable("SetState")));
 BA.debugLineNum = 25;BA.debugLine="End Sub";
Debug.ShouldStop(16777216);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _disconnect() throws Exception{
try {
		Debug.PushSubsStack("disConnect (starter) ","starter",2,starter.processBA,starter.mostCurrent,27);
if (RapidSub.canDelegate("disconnect")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","disconnect");}
 BA.debugLineNum = 27;BA.debugLine="Public Sub disConnect";
Debug.ShouldStop(67108864);
 BA.debugLineNum = 28;BA.debugLine="admin.StartDiscovery";
Debug.ShouldStop(134217728);
starter._admin.runVoidMethod ("StartDiscovery");
 BA.debugLineNum = 29;BA.debugLine="connecting = False";
Debug.ShouldStop(268435456);
starter._connecting = starter.mostCurrent.__c.getField(true,"False");
 BA.debugLineNum = 31;BA.debugLine="CallSub(Main, \"SetState\")";
Debug.ShouldStop(1073741824);
starter.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",starter.processBA,(Object)((starter.mostCurrent._main.getObject())),(Object)(RemoteObject.createImmutable("SetState")));
 BA.debugLineNum = 32;BA.debugLine="End Sub";
Debug.ShouldStop(-2147483648);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Private serial As Serial";
starter._serial = RemoteObject.createNew ("anywheresoftware.b4a.objects.Serial");
 //BA.debugLineNum = 8;BA.debugLine="Private admin As BluetoothAdmin";
starter._admin = RemoteObject.createNew ("anywheresoftware.b4a.objects.Serial.BluetoothAdmin");
 //BA.debugLineNum = 9;BA.debugLine="Private ast As AsyncStreams";
starter._ast = RemoteObject.createNew ("anywheresoftware.b4a.randomaccessfile.AsyncStreams");
 //BA.debugLineNum = 10;BA.debugLine="Public connected As Boolean";
starter._connected = RemoteObject.createImmutable(false);
 //BA.debugLineNum = 11;BA.debugLine="Public connecting As Boolean";
starter._connecting = RemoteObject.createImmutable(false);
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _sendmessage(RemoteObject _msg) throws Exception{
try {
		Debug.PushSubsStack("SendMessage (starter) ","starter",2,starter.processBA,starter.mostCurrent,61);
if (RapidSub.canDelegate("sendmessage")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","sendmessage", _msg);}
Debug.locals.put("msg", _msg);
 BA.debugLineNum = 61;BA.debugLine="Public Sub SendMessage(msg() As Byte)";
Debug.ShouldStop(268435456);
 BA.debugLineNum = 63;BA.debugLine="Log(\"msg\")";
Debug.ShouldStop(1073741824);
starter.mostCurrent.__c.runVoidMethod ("LogImpl","22228226",RemoteObject.createImmutable("msg"),0);
 BA.debugLineNum = 64;BA.debugLine="End Sub";
Debug.ShouldStop(-2147483648);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _serial_connected(RemoteObject _success) throws Exception{
try {
		Debug.PushSubsStack("Serial_Connected (starter) ","starter",2,starter.processBA,starter.mostCurrent,47);
if (RapidSub.canDelegate("serial_connected")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","serial_connected", _success);}
Debug.locals.put("Success", _success);
 BA.debugLineNum = 47;BA.debugLine="Private Sub Serial_Connected (Success As Boolean)";
Debug.ShouldStop(16384);
 BA.debugLineNum = 48;BA.debugLine="If Success Then";
Debug.ShouldStop(32768);
if (_success.<Boolean>get().booleanValue()) { 
 BA.debugLineNum = 49;BA.debugLine="If ast.IsInitialized Then ast.Close";
Debug.ShouldStop(65536);
if (starter._ast.runMethod(true,"IsInitialized").<Boolean>get().booleanValue()) { 
starter._ast.runVoidMethod ("Close");};
 BA.debugLineNum = 51;BA.debugLine="Log(\"Connected\")";
Debug.ShouldStop(262144);
starter.mostCurrent.__c.runVoidMethod ("LogImpl","22162692",RemoteObject.createImmutable("Connected"),0);
 BA.debugLineNum = 52;BA.debugLine="connected = True";
Debug.ShouldStop(524288);
starter._connected = starter.mostCurrent.__c.getField(true,"True");
 }else {
 BA.debugLineNum = 55;BA.debugLine="Log(LastException)";
Debug.ShouldStop(4194304);
starter.mostCurrent.__c.runVoidMethod ("LogImpl","22162696",BA.ObjectToString(starter.mostCurrent.__c.runMethod(false,"LastException",starter.processBA)),0);
 };
 BA.debugLineNum = 57;BA.debugLine="connecting = False";
Debug.ShouldStop(16777216);
starter._connecting = starter.mostCurrent.__c.getField(true,"False");
 BA.debugLineNum = 58;BA.debugLine="CallSub(Main, \"SetState\")";
Debug.ShouldStop(33554432);
starter.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",starter.processBA,(Object)((starter.mostCurrent._main.getObject())),(Object)(RemoteObject.createImmutable("SetState")));
 BA.debugLineNum = 59;BA.debugLine="End Sub";
Debug.ShouldStop(67108864);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _service_create() throws Exception{
try {
		Debug.PushSubsStack("Service_Create (starter) ","starter",2,starter.processBA,starter.mostCurrent,14);
if (RapidSub.canDelegate("service_create")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","service_create");}
 BA.debugLineNum = 14;BA.debugLine="Sub Service_Create";
Debug.ShouldStop(8192);
 BA.debugLineNum = 15;BA.debugLine="serial.Initialize(\"serial\")";
Debug.ShouldStop(16384);
starter._serial.runVoidMethod ("Initialize",(Object)(RemoteObject.createImmutable("serial")));
 BA.debugLineNum = 16;BA.debugLine="admin.Initialize(\"admin\")";
Debug.ShouldStop(32768);
starter._admin.runVoidMethod ("Initialize",starter.processBA,(Object)(RemoteObject.createImmutable("admin")));
 BA.debugLineNum = 18;BA.debugLine="End Sub";
Debug.ShouldStop(131072);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _service_destroy() throws Exception{
try {
		Debug.PushSubsStack("Service_Destroy (starter) ","starter",2,starter.processBA,starter.mostCurrent,86);
if (RapidSub.canDelegate("service_destroy")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","service_destroy");}
 BA.debugLineNum = 86;BA.debugLine="Sub Service_Destroy";
Debug.ShouldStop(2097152);
 BA.debugLineNum = 88;BA.debugLine="End Sub";
Debug.ShouldStop(8388608);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _service_start(RemoteObject _startingintent) throws Exception{
try {
		Debug.PushSubsStack("Service_Start (starter) ","starter",2,starter.processBA,starter.mostCurrent,77);
if (RapidSub.canDelegate("service_start")) { return anywheresoftware.b4a.samples.gps.starter.remoteMe.runUserSub(false, "starter","service_start", _startingintent);}
Debug.locals.put("StartingIntent", _startingintent);
 BA.debugLineNum = 77;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
Debug.ShouldStop(4096);
 BA.debugLineNum = 79;BA.debugLine="End Sub";
Debug.ShouldStop(16384);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}