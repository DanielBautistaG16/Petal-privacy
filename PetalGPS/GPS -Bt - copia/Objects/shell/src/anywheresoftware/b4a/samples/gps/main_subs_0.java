package anywheresoftware.b4a.samples.gps;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.*;

public class main_subs_0 {


public static RemoteObject  _activity_create(RemoteObject _firsttime) throws Exception{
try {
		Debug.PushSubsStack("Activity_Create (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,37);
if (RapidSub.canDelegate("activity_create")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","activity_create", _firsttime);}
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 37;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(16);
 BA.debugLineNum = 38;BA.debugLine="If FirstTime Then";
Debug.ShouldStop(32);
if (_firsttime.<Boolean>get().booleanValue()) { 
 BA.debugLineNum = 39;BA.debugLine="GPS1.Initialize(\"GPS\")";
Debug.ShouldStop(64);
main._gps1.runVoidMethod ("Initialize",(Object)(RemoteObject.createImmutable("GPS")));
 BA.debugLineNum = 40;BA.debugLine="Log(\"GPS\")";
Debug.ShouldStop(128);
main.mostCurrent.__c.runVoidMethod ("LogImpl","2131075",RemoteObject.createImmutable("GPS"),0);
 };
 BA.debugLineNum = 44;BA.debugLine="Activity.LoadLayout(\"1\")";
Debug.ShouldStop(2048);
main.mostCurrent._activity.runMethodAndSync(false,"LoadLayout",(Object)(RemoteObject.createImmutable("1")),main.mostCurrent.activityBA);
 BA.debugLineNum = 45;BA.debugLine="Activity.LoadLayout(\"2\")";
Debug.ShouldStop(4096);
main.mostCurrent._activity.runMethodAndSync(false,"LoadLayout",(Object)(RemoteObject.createImmutable("2")),main.mostCurrent.activityBA);
 BA.debugLineNum = 48;BA.debugLine="audioStreamer.Initialize(\"audioStreamer\", 44100,";
Debug.ShouldStop(32768);
main._audiostreamer.runVoidMethod ("Initialize",main.processBA,(Object)(BA.ObjectToString("audioStreamer")),(Object)(BA.numberCast(int.class, 44100)),(Object)(main.mostCurrent.__c.getField(true,"True")),(Object)(BA.numberCast(int.class, 16)),(Object)(BA.numberCast(int.class, 1)));
 BA.debugLineNum = 53;BA.debugLine="recButton.Initialize(\"recButton\")";
Debug.ShouldStop(1048576);
main.mostCurrent._recbutton.runVoidMethod ("Initialize",main.mostCurrent.activityBA,(Object)(RemoteObject.createImmutable("recButton")));
 BA.debugLineNum = 54;BA.debugLine="stopButton.Initialize(\"stopButton\")";
Debug.ShouldStop(2097152);
main.mostCurrent._stopbutton.runVoidMethod ("Initialize",main.mostCurrent.activityBA,(Object)(RemoteObject.createImmutable("stopButton")));
 BA.debugLineNum = 55;BA.debugLine="filterLabel.Initialize(\"filterLabel\")";
Debug.ShouldStop(4194304);
main.mostCurrent._filterlabel.runVoidMethod ("Initialize",main.mostCurrent.activityBA,(Object)(RemoteObject.createImmutable("filterLabel")));
 BA.debugLineNum = 57;BA.debugLine="recButton.Text = \"Grabar\"";
Debug.ShouldStop(16777216);
main.mostCurrent._recbutton.runMethod(true,"setText",BA.ObjectToCharSequence("Grabar"));
 BA.debugLineNum = 58;BA.debugLine="stopButton.Text = \"Detener\"";
Debug.ShouldStop(33554432);
main.mostCurrent._stopbutton.runMethod(true,"setText",BA.ObjectToCharSequence("Detener"));
 BA.debugLineNum = 59;BA.debugLine="filterLabel.Text = \"Preparado para grabar audio\"";
Debug.ShouldStop(67108864);
main.mostCurrent._filterlabel.runMethod(true,"setText",BA.ObjectToCharSequence("Preparado para grabar audio"));
 BA.debugLineNum = 60;BA.debugLine="End Sub";
Debug.ShouldStop(134217728);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _activity_pause(RemoteObject _userclosed) throws Exception{
try {
		Debug.PushSubsStack("Activity_Pause (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,72);
if (RapidSub.canDelegate("activity_pause")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","activity_pause", _userclosed);}
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 72;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(128);
 BA.debugLineNum = 73;BA.debugLine="GPS1.Stop";
Debug.ShouldStop(256);
main._gps1.runVoidMethod ("Stop");
 BA.debugLineNum = 74;BA.debugLine="End Sub";
Debug.ShouldStop(512);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _activity_resume() throws Exception{
try {
		Debug.PushSubsStack("Activity_Resume (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,62);
if (RapidSub.canDelegate("activity_resume")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","activity_resume");}
 BA.debugLineNum = 62;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(536870912);
 BA.debugLineNum = 63;BA.debugLine="If GPS1.GPSEnabled = False Then";
Debug.ShouldStop(1073741824);
if (RemoteObject.solveBoolean("=",main._gps1.runMethod(true,"getGPSEnabled"),main.mostCurrent.__c.getField(true,"False"))) { 
 BA.debugLineNum = 64;BA.debugLine="ToastMessageShow(\"Please enable the GPS device.\"";
Debug.ShouldStop(-2147483648);
main.mostCurrent.__c.runVoidMethod ("ToastMessageShow",(Object)(BA.ObjectToCharSequence("Please enable the GPS device.")),(Object)(main.mostCurrent.__c.getField(true,"True")));
 BA.debugLineNum = 65;BA.debugLine="StartActivity(GPS1.LocationSettingsIntent) 'Will";
Debug.ShouldStop(1);
main.mostCurrent.__c.runVoidMethod ("StartActivity",main.processBA,(Object)((main._gps1.runMethod(false,"getLocationSettingsIntent"))));
 }else {
 BA.debugLineNum = 67;BA.debugLine="GPS1.Start(0, 0) 'Listen to GPS with no filters.";
Debug.ShouldStop(4);
main._gps1.runVoidMethodAndSync ("Start",main.processBA,(Object)(BA.numberCast(long.class, 0)),(Object)(BA.numberCast(float.class, 0)));
 };
 BA.debugLineNum = 70;BA.debugLine="End Sub";
Debug.ShouldStop(32);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _audiostreamer_recordbuffer(RemoteObject _buffer) throws Exception{
try {
		Debug.PushSubsStack("audioStreamer_RecordBuffer (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,208);
if (RapidSub.canDelegate("audiostreamer_recordbuffer")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","audiostreamer_recordbuffer", _buffer);}
Debug.locals.put("Buffer", _buffer);
 BA.debugLineNum = 208;BA.debugLine="Sub audioStreamer_RecordBuffer (Buffer() As Byte)";
Debug.ShouldStop(32768);
 BA.debugLineNum = 215;BA.debugLine="End Sub";
Debug.ShouldStop(4194304);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static void  _b4xswitch1_valuechanged(RemoteObject _value) throws Exception{
try {
		Debug.PushSubsStack("B4XSwitch1_ValueChanged (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,174);
if (RapidSub.canDelegate("b4xswitch1_valuechanged")) { anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","b4xswitch1_valuechanged", _value); return;}
ResumableSub_B4XSwitch1_ValueChanged rsub = new ResumableSub_B4XSwitch1_ValueChanged(null,_value);
rsub.resume(null, null);
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static class ResumableSub_B4XSwitch1_ValueChanged extends BA.ResumableSub {
public ResumableSub_B4XSwitch1_ValueChanged(anywheresoftware.b4a.samples.gps.main parent,RemoteObject _value) {
this.parent = parent;
this._value = _value;
}
java.util.LinkedHashMap<String, Object> rsLocals = new java.util.LinkedHashMap<String, Object>();
anywheresoftware.b4a.samples.gps.main parent;
RemoteObject _value;
RemoteObject _permission = RemoteObject.createImmutable("");
RemoteObject _result = RemoteObject.createImmutable(false);

@Override
public void resume(BA ba, RemoteObject result) throws Exception{
try {
		Debug.PushSubsStack("B4XSwitch1_ValueChanged (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,174);
Debug.locals = rsLocals;Debug.currentSubFrame.locals = rsLocals;

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
Debug.locals.put("Value", _value);
 BA.debugLineNum = 175;BA.debugLine="If Value=True Then";
Debug.ShouldStop(16384);
if (true) break;

case 1:
//if
this.state = 12;
if (RemoteObject.solveBoolean("=",_value,parent.mostCurrent.__c.getField(true,"True"))) { 
this.state = 3;
}else {
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
 BA.debugLineNum = 176;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_ACCESS_COARSE_L";
Debug.ShouldStop(32768);
parent.mostCurrent._rp.runVoidMethod ("CheckAndRequest",main.processBA,(Object)(parent.mostCurrent._rp.getField(true,"PERMISSION_ACCESS_COARSE_LOCATION")));
 BA.debugLineNum = 177;BA.debugLine="Log(\"Esperando\")";
Debug.ShouldStop(65536);
parent.mostCurrent.__c.runVoidMethod ("LogImpl","2917507",RemoteObject.createImmutable("Esperando"),0);
 BA.debugLineNum = 178;BA.debugLine="Wait For Activity_PermissionResult (Permission A";
Debug.ShouldStop(131072);
parent.mostCurrent.__c.runVoidMethod ("WaitFor","activity_permissionresult", main.processBA, anywheresoftware.b4a.pc.PCResumableSub.createDebugResumeSub(this, "main", "b4xswitch1_valuechanged"), null);
this.state = 13;
return;
case 13:
//C
this.state = 4;
_permission = (RemoteObject) result.getArrayElement(true,RemoteObject.createImmutable(0));Debug.locals.put("Permission", _permission);
_result = (RemoteObject) result.getArrayElement(true,RemoteObject.createImmutable(1));Debug.locals.put("Result", _result);
;
 BA.debugLineNum = 179;BA.debugLine="If Result Then";
Debug.ShouldStop(262144);
if (true) break;

case 4:
//if
this.state = 9;
if (_result.<Boolean>get().booleanValue()) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 BA.debugLineNum = 180;BA.debugLine="CallSub(Starter, \"Connect\")";
Debug.ShouldStop(524288);
parent.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",main.processBA,(Object)((parent.mostCurrent._starter.getObject())),(Object)(RemoteObject.createImmutable("Connect")));
 if (true) break;

case 8:
//C
this.state = 9;
 BA.debugLineNum = 182;BA.debugLine="ToastMessageShow(\"No permission\", True)";
Debug.ShouldStop(2097152);
parent.mostCurrent.__c.runVoidMethod ("ToastMessageShow",(Object)(BA.ObjectToCharSequence("No permission")),(Object)(parent.mostCurrent.__c.getField(true,"True")));
 if (true) break;

case 9:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
 BA.debugLineNum = 185;BA.debugLine="Starter.connected=False";
Debug.ShouldStop(16777216);
parent.mostCurrent._starter._connected /*RemoteObject*/  = parent.mostCurrent.__c.getField(true,"False");
 BA.debugLineNum = 186;BA.debugLine="Log(\"desconectado\")";
Debug.ShouldStop(33554432);
parent.mostCurrent.__c.runVoidMethod ("LogImpl","2917516",RemoteObject.createImmutable("desconectado"),0);
 BA.debugLineNum = 187;BA.debugLine="SetDesconectar";
Debug.ShouldStop(67108864);
_setdesconectar();
 BA.debugLineNum = 188;BA.debugLine="CallSub(Starter, \"disConnect\")";
Debug.ShouldStop(134217728);
parent.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",main.processBA,(Object)((parent.mostCurrent._starter.getObject())),(Object)(RemoteObject.createImmutable("disConnect")));
 if (true) break;

case 12:
//C
this.state = -1;
;
 BA.debugLineNum = 191;BA.debugLine="End Sub";
Debug.ShouldStop(1073741824);
if (true) break;

            }
        }
    }
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}
public static void  _activity_permissionresult(RemoteObject _permission,RemoteObject _result) throws Exception{
}
public static RemoteObject  _btnchat_click() throws Exception{
try {
		Debug.PushSubsStack("BtnChat_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,218);
if (RapidSub.canDelegate("btnchat_click")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","btnchat_click");}
RemoteObject _p = RemoteObject.declareNull("anywheresoftware.b4a.phone.Phone.PhoneIntents");
 BA.debugLineNum = 218;BA.debugLine="Private Sub BtnChat_Click";
Debug.ShouldStop(33554432);
 BA.debugLineNum = 219;BA.debugLine="Dim p As PhoneIntents";
Debug.ShouldStop(67108864);
_p = RemoteObject.createNew ("anywheresoftware.b4a.phone.Phone.PhoneIntents");Debug.locals.put("p", _p);
 BA.debugLineNum = 220;BA.debugLine="StartActivity(p.OpenBrowser(\"https://chatgpt.com/";
Debug.ShouldStop(134217728);
main.mostCurrent.__c.runVoidMethod ("StartActivity",main.processBA,(Object)((_p.runMethod(false,"OpenBrowser",(Object)(RemoteObject.createImmutable("https://chatgpt.com/share/678fb915-d3d0-8006-ac60-aeb54a0efa2e"))))));
 BA.debugLineNum = 222;BA.debugLine="End Sub";
Debug.ShouldStop(536870912);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static void  _btnconnect_click() throws Exception{
try {
		Debug.PushSubsStack("btnConnect_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,161);
if (RapidSub.canDelegate("btnconnect_click")) { anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","btnconnect_click"); return;}
ResumableSub_btnConnect_Click rsub = new ResumableSub_btnConnect_Click(null);
rsub.resume(null, null);
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static class ResumableSub_btnConnect_Click extends BA.ResumableSub {
public ResumableSub_btnConnect_Click(anywheresoftware.b4a.samples.gps.main parent) {
this.parent = parent;
}
java.util.LinkedHashMap<String, Object> rsLocals = new java.util.LinkedHashMap<String, Object>();
anywheresoftware.b4a.samples.gps.main parent;
RemoteObject _permission = RemoteObject.createImmutable("");
RemoteObject _result = RemoteObject.createImmutable(false);

@Override
public void resume(BA ba, RemoteObject result) throws Exception{
try {
		Debug.PushSubsStack("btnConnect_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,161);
Debug.locals = rsLocals;Debug.currentSubFrame.locals = rsLocals;

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 BA.debugLineNum = 162;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_ACCESS_COARSE_LO";
Debug.ShouldStop(2);
parent.mostCurrent._rp.runVoidMethod ("CheckAndRequest",main.processBA,(Object)(parent.mostCurrent._rp.getField(true,"PERMISSION_ACCESS_COARSE_LOCATION")));
 BA.debugLineNum = 163;BA.debugLine="Log(\"Esperando\")";
Debug.ShouldStop(4);
parent.mostCurrent.__c.runVoidMethod ("LogImpl","2851970",RemoteObject.createImmutable("Esperando"),0);
 BA.debugLineNum = 164;BA.debugLine="Wait For Activity_PermissionResult (Permission As";
Debug.ShouldStop(8);
parent.mostCurrent.__c.runVoidMethod ("WaitFor","activity_permissionresult", main.processBA, anywheresoftware.b4a.pc.PCResumableSub.createDebugResumeSub(this, "main", "btnconnect_click"), null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_permission = (RemoteObject) result.getArrayElement(true,RemoteObject.createImmutable(0));Debug.locals.put("Permission", _permission);
_result = (RemoteObject) result.getArrayElement(true,RemoteObject.createImmutable(1));Debug.locals.put("Result", _result);
;
 BA.debugLineNum = 165;BA.debugLine="If Result Then";
Debug.ShouldStop(16);
if (true) break;

case 1:
//if
this.state = 6;
if (_result.<Boolean>get().booleanValue()) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 BA.debugLineNum = 166;BA.debugLine="CallSub(Starter, \"Connect\")";
Debug.ShouldStop(32);
parent.mostCurrent.__c.runMethodAndSync(false,"CallSubNew",main.processBA,(Object)((parent.mostCurrent._starter.getObject())),(Object)(RemoteObject.createImmutable("Connect")));
 if (true) break;

case 5:
//C
this.state = 6;
 BA.debugLineNum = 169;BA.debugLine="ToastMessageShow(\"No permission\", True)";
Debug.ShouldStop(256);
parent.mostCurrent.__c.runVoidMethod ("ToastMessageShow",(Object)(BA.ObjectToCharSequence("No permission")),(Object)(parent.mostCurrent.__c.getField(true,"True")));
 if (true) break;

case 6:
//C
this.state = -1;
;
 BA.debugLineNum = 171;BA.debugLine="End Sub";
Debug.ShouldStop(1024);
if (true) break;

            }
        }
    }
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}
public static RemoteObject  _btnlogo_click() throws Exception{
try {
		Debug.PushSubsStack("BtnLogo_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,224);
if (RapidSub.canDelegate("btnlogo_click")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","btnlogo_click");}
RemoteObject _t = RemoteObject.declareNull("anywheresoftware.b4a.phone.Phone.PhoneIntents");
 BA.debugLineNum = 224;BA.debugLine="Private Sub BtnLogo_Click";
Debug.ShouldStop(-2147483648);
 BA.debugLineNum = 225;BA.debugLine="Dim t As PhoneIntents";
Debug.ShouldStop(1);
_t = RemoteObject.createNew ("anywheresoftware.b4a.phone.Phone.PhoneIntents");Debug.locals.put("t", _t);
 BA.debugLineNum = 226;BA.debugLine="StartActivity(t.OpenBrowser(\"https://ridepetal.co";
Debug.ShouldStop(2);
main.mostCurrent.__c.runVoidMethod ("StartActivity",main.processBA,(Object)((_t.runMethod(false,"OpenBrowser",(Object)(RemoteObject.createImmutable("https://ridepetal.com/"))))));
 BA.debugLineNum = 228;BA.debugLine="End Sub";
Debug.ShouldStop(8);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 18;BA.debugLine="Dim lblLon As Label";
main.mostCurrent._lbllon = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 19;BA.debugLine="Dim lblLat As Label";
main.mostCurrent._lbllat = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 20;BA.debugLine="Dim lblSpeed As Label";
main.mostCurrent._lblspeed = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 21;BA.debugLine="Dim lblSatellites As Label";
main.mostCurrent._lblsatellites = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 23;BA.debugLine="Dim btnConnect As Button";
main.mostCurrent._btnconnect = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 24;BA.debugLine="Dim progressBar1,progressBar2 As ProgressBar";
main.mostCurrent._progressbar1 = RemoteObject.createNew ("anywheresoftware.b4a.objects.ProgressBarWrapper");
main.mostCurrent._progressbar2 = RemoteObject.createNew ("anywheresoftware.b4a.objects.ProgressBarWrapper");
 //BA.debugLineNum = 25;BA.debugLine="Dim lblStatus As Label";
main.mostCurrent._lblstatus = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 26;BA.debugLine="Dim lblMessage As Label";
main.mostCurrent._lblmessage = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 27;BA.debugLine="Private rp As RuntimePermissions";
main.mostCurrent._rp = RemoteObject.createNew ("anywheresoftware.b4a.objects.RuntimePermissions");
 //BA.debugLineNum = 28;BA.debugLine="Private B4XImageView2 As B4XImageView";
main.mostCurrent._b4ximageview2 = RemoteObject.createNew ("anywheresoftware.b4a.samples.gps.b4ximageview");
 //BA.debugLineNum = 29;BA.debugLine="Private b4xSwitch1 As B4XSwitch";
main.mostCurrent._b4xswitch1 = RemoteObject.createNew ("anywheresoftware.b4a.samples.gps.b4xswitch");
 //BA.debugLineNum = 31;BA.debugLine="Private recButton As Button";
main.mostCurrent._recbutton = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 32;BA.debugLine="Private stopButton As Button";
main.mostCurrent._stopbutton = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 33;BA.debugLine="Private filterLabel As Label";
main.mostCurrent._filterlabel = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _gps_gnssstatus(RemoteObject _satellites) throws Exception{
try {
		Debug.PushSubsStack("GPS_GnssStatus (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,88);
if (RapidSub.canDelegate("gps_gnssstatus")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","gps_gnssstatus", _satellites);}
int _i = 0;
RemoteObject _satellite = RemoteObject.declareNull("anywheresoftware.b4a.gps.GpsSatelliteWrapper");
Debug.locals.put("Satellites", _satellites);
 BA.debugLineNum = 88;BA.debugLine="Sub GPS_GnssStatus (Satellites As List)";
Debug.ShouldStop(8388608);
 BA.debugLineNum = 89;BA.debugLine="lblSatellites.Text = \"Satellites:\" & CRLF";
Debug.ShouldStop(16777216);
main.mostCurrent._lblsatellites.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("Satellites:"),main.mostCurrent.__c.getField(true,"CRLF"))));
 BA.debugLineNum = 90;BA.debugLine="For i = 0 To Satellites.Size - 1";
Debug.ShouldStop(33554432);
{
final int step2 = 1;
final int limit2 = RemoteObject.solve(new RemoteObject[] {_satellites.runMethod(true,"getSize"),RemoteObject.createImmutable(1)}, "-",1, 1).<Integer>get().intValue();
_i = 0 ;
for (;(step2 > 0 && _i <= limit2) || (step2 < 0 && _i >= limit2) ;_i = ((int)(0 + _i + step2))  ) {
Debug.locals.put("i", _i);
 BA.debugLineNum = 91;BA.debugLine="Dim Satellite As GPSSatellite";
Debug.ShouldStop(67108864);
_satellite = RemoteObject.createNew ("anywheresoftware.b4a.gps.GpsSatelliteWrapper");Debug.locals.put("Satellite", _satellite);
 BA.debugLineNum = 92;BA.debugLine="Satellite = Satellites.Get(i)";
Debug.ShouldStop(134217728);
_satellite = RemoteObject.declareNull("anywheresoftware.b4a.AbsObjectWrapper").runMethod(false, "ConvertToWrapper", RemoteObject.createNew("anywheresoftware.b4a.gps.GpsSatelliteWrapper"), _satellites.runMethod(false,"Get",(Object)(BA.numberCast(int.class, _i))));Debug.locals.put("Satellite", _satellite);
 BA.debugLineNum = 93;BA.debugLine="lblSatellites.Text = lblSatellites.Text & CRLF &";
Debug.ShouldStop(268435456);
main.mostCurrent._lblsatellites.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(main.mostCurrent._lblsatellites.runMethod(true,"getText"),main.mostCurrent.__c.getField(true,"CRLF"),_satellite.runMethod(true,"getPrn"),RemoteObject.createImmutable(" "),_satellite.runMethod(true,"getSnr"),RemoteObject.createImmutable(" "),_satellite.runMethod(true,"getUsedInFix"),RemoteObject.createImmutable(" "),_satellite.runMethod(true,"getAzimuth"),RemoteObject.createImmutable(" "),_satellite.runMethod(true,"getElevation"))));
 }
}Debug.locals.put("i", _i);
;
 BA.debugLineNum = 97;BA.debugLine="End Sub";
Debug.ShouldStop(1);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _gps_locationchanged(RemoteObject _location1) throws Exception{
try {
		Debug.PushSubsStack("GPS_LocationChanged (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,76);
if (RapidSub.canDelegate("gps_locationchanged")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","gps_locationchanged", _location1);}
Debug.locals.put("Location1", _location1);
 BA.debugLineNum = 76;BA.debugLine="Sub GPS_LocationChanged (Location1 As Location)";
Debug.ShouldStop(2048);
 BA.debugLineNum = 77;BA.debugLine="lblLat.Text = \"Lat = \" & Location1.ConvertToMinut";
Debug.ShouldStop(4096);
main.mostCurrent._lbllat.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("Lat = "),_location1.runMethod(true,"ConvertToMinutes",(Object)(_location1.runMethod(true,"getLatitude"))))));
 BA.debugLineNum = 78;BA.debugLine="lblLon.Text = \"Lon = \" & Location1.ConvertToMinut";
Debug.ShouldStop(8192);
main.mostCurrent._lbllon.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("Lon = "),_location1.runMethod(true,"ConvertToMinutes",(Object)(_location1.runMethod(true,"getLongitude"))))));
 BA.debugLineNum = 81;BA.debugLine="lblSpeed.Text = NumberFormat(1.60934*60/25* Locat";
Debug.ShouldStop(65536);
main.mostCurrent._lblspeed.runMethod(true,"setText",BA.ObjectToCharSequence(main.mostCurrent.__c.runMethod(true,"NumberFormat",(Object)(RemoteObject.solve(new RemoteObject[] {RemoteObject.createImmutable(1.60934),RemoteObject.createImmutable(60),RemoteObject.createImmutable(25),_location1.runMethod(true,"getSpeed")}, "*/*",0, 0)),(Object)(BA.numberCast(int.class, 2)),(Object)(BA.numberCast(int.class, 0)))));
 BA.debugLineNum = 82;BA.debugLine="End Sub";
Debug.ShouldStop(131072);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _gps_userenabled(RemoteObject _enabled) throws Exception{
try {
		Debug.PushSubsStack("GPS_UserEnabled (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,84);
if (RapidSub.canDelegate("gps_userenabled")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","gps_userenabled", _enabled);}
Debug.locals.put("Enabled", _enabled);
 BA.debugLineNum = 84;BA.debugLine="Sub GPS_UserEnabled (Enabled As Boolean)";
Debug.ShouldStop(524288);
 BA.debugLineNum = 85;BA.debugLine="ToastMessageShow(\"GPS device enabled = \" & Enable";
Debug.ShouldStop(1048576);
main.mostCurrent.__c.runVoidMethod ("ToastMessageShow",(Object)(BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("GPS device enabled = "),_enabled))),(Object)(main.mostCurrent.__c.getField(true,"True")));
 BA.debugLineNum = 86;BA.debugLine="End Sub";
Debug.ShouldStop(2097152);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _lblmap_click() throws Exception{
try {
		Debug.PushSubsStack("lblMap_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,110);
if (RapidSub.canDelegate("lblmap_click")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","lblmap_click");}
RemoteObject _p = RemoteObject.declareNull("anywheresoftware.b4a.phone.Phone.PhoneIntents");
 BA.debugLineNum = 110;BA.debugLine="Private Sub lblMap_Click";
Debug.ShouldStop(8192);
 BA.debugLineNum = 111;BA.debugLine="Dim p As PhoneIntents";
Debug.ShouldStop(16384);
_p = RemoteObject.createNew ("anywheresoftware.b4a.phone.Phone.PhoneIntents");Debug.locals.put("p", _p);
 BA.debugLineNum = 112;BA.debugLine="StartActivity(p.OpenBrowser(\"https://www.google.c";
Debug.ShouldStop(32768);
main.mostCurrent.__c.runVoidMethod ("StartActivity",main.processBA,(Object)((_p.runMethod(false,"OpenBrowser",(Object)(RemoteObject.createImmutable("https://www.google.com/maps/@?api=1&map_action=map&hl=es-419"))))));
 BA.debugLineNum = 113;BA.debugLine="End Sub";
Debug.ShouldStop(65536);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _messagefromdevice(RemoteObject _msg) throws Exception{
try {
		Debug.PushSubsStack("MessageFromDevice (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,141);
if (RapidSub.canDelegate("messagefromdevice")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","messagefromdevice", _msg);}
RemoteObject _nivelbat = RemoteObject.createImmutable(0);
Debug.locals.put("msg", _msg);
 BA.debugLineNum = 141;BA.debugLine="Public Sub MessageFromDevice(msg As String)";
Debug.ShouldStop(4096);
 BA.debugLineNum = 142;BA.debugLine="Dim NivelBat= Round2(msg/5*100,0) As Int";
Debug.ShouldStop(8192);
_nivelbat = BA.numberCast(int.class, main.mostCurrent.__c.runMethod(true,"Round2",(Object)(RemoteObject.solve(new RemoteObject[] {BA.numberCast(double.class, _msg),RemoteObject.createImmutable(5),RemoteObject.createImmutable(100)}, "/*",0, 0)),(Object)(BA.numberCast(int.class, 0))));Debug.locals.put("NivelBat", _nivelbat);Debug.locals.put("NivelBat", _nivelbat);
 BA.debugLineNum = 144;BA.debugLine="lblMessage.Text=\"BATTERY LEVEL \"&CRLF&NivelBat &\"";
Debug.ShouldStop(32768);
main.mostCurrent._lblmessage.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("BATTERY LEVEL "),main.mostCurrent.__c.getField(true,"CRLF"),_nivelbat,RemoteObject.createImmutable(" %"))));
 BA.debugLineNum = 145;BA.debugLine="Log(lblMessage.Text)";
Debug.ShouldStop(65536);
main.mostCurrent.__c.runVoidMethod ("LogImpl","2786436",main.mostCurrent._lblmessage.runMethod(true,"getText"),0);
 BA.debugLineNum = 146;BA.debugLine="Log(msg)";
Debug.ShouldStop(131072);
main.mostCurrent.__c.runVoidMethod ("LogImpl","2786437",_msg,0);
 BA.debugLineNum = 149;BA.debugLine="End Sub";
Debug.ShouldStop(1048576);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main_subs_0._process_globals();
starter_subs_0._process_globals();
xuiviewsutils_subs_0._process_globals();
main.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.main");
asyncstreamstext.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.asyncstreamstext");
starter.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.starter");
animatedcounter.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.animatedcounter");
anotherprogressbar.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.anotherprogressbar");
b4xbreadcrumb.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xbreadcrumb");
b4xcolortemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xcolortemplate");
b4xcombobox.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xcombobox");
b4xdatetemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xdatetemplate");
b4xdialog.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xdialog");
b4xfloattextfield.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xfloattextfield");
b4ximageview.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4ximageview");
b4xinputtemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xinputtemplate");
b4xlisttemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xlisttemplate");
b4xloadingindicator.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xloadingindicator");
b4xlongtexttemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xlongtexttemplate");
b4xplusminus.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xplusminus");
b4xradiobutton.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xradiobutton");
b4xsearchtemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xsearchtemplate");
b4xseekbar.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xseekbar");
b4xsignaturetemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xsignaturetemplate");
b4xswitch.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xswitch");
b4xtimedtemplate.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xtimedtemplate");
madewithlove.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.madewithlove");
b4xformatter.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.b4xformatter");
roundslider.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.roundslider");
scrollinglabel.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.scrollinglabel");
swiftbutton.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.swiftbutton");
xuiviewsutils.myClass = BA.getDeviceClass ("anywheresoftware.b4a.samples.gps.xuiviewsutils");
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static RemoteObject  _process_globals() throws Exception{
 //BA.debugLineNum = 11;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 12;BA.debugLine="Dim GPS1 As GPS";
main._gps1 = RemoteObject.createNew ("anywheresoftware.b4a.gps.GPS");
 //BA.debugLineNum = 13;BA.debugLine="Private audioStreamer As AudioStreamer 'Para capt";
main._audiostreamer = RemoteObject.createNew ("anywheresoftware.b4a.audio.AudioStreamer");
 //BA.debugLineNum = 15;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _recbutton_click() throws Exception{
try {
		Debug.PushSubsStack("recButton_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,196);
if (RapidSub.canDelegate("recbutton_click")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","recbutton_click");}
 BA.debugLineNum = 196;BA.debugLine="Sub recButton_Click";
Debug.ShouldStop(8);
 BA.debugLineNum = 197;BA.debugLine="audioStreamer.StartRecording ' Inicia la graba";
Debug.ShouldStop(16);
main._audiostreamer.runVoidMethod ("StartRecording");
 BA.debugLineNum = 198;BA.debugLine="filterLabel.Text = \"Grabando...\"";
Debug.ShouldStop(32);
main.mostCurrent._filterlabel.runMethod(true,"setText",BA.ObjectToCharSequence("Grabando..."));
 BA.debugLineNum = 199;BA.debugLine="End Sub";
Debug.ShouldStop(64);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _setdesconectar() throws Exception{
try {
		Debug.PushSubsStack("SetDesconectar (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,137);
if (RapidSub.canDelegate("setdesconectar")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","setdesconectar");}
 BA.debugLineNum = 137;BA.debugLine="Public Sub SetDesconectar";
Debug.ShouldStop(256);
 BA.debugLineNum = 138;BA.debugLine="progressBar1.Visible = Starter.connected";
Debug.ShouldStop(512);
main.mostCurrent._progressbar1.runMethod(true,"setVisible",main.mostCurrent._starter._connected /*RemoteObject*/ );
 BA.debugLineNum = 139;BA.debugLine="End Sub";
Debug.ShouldStop(1024);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _setstate() throws Exception{
try {
		Debug.PushSubsStack("SetState (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,116);
if (RapidSub.canDelegate("setstate")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","setstate");}
RemoteObject _status = RemoteObject.createImmutable("");
 BA.debugLineNum = 116;BA.debugLine="Public Sub SetState";
Debug.ShouldStop(524288);
 BA.debugLineNum = 118;BA.debugLine="btnConnect.Enabled = Not(Starter.connected)";
Debug.ShouldStop(2097152);
main.mostCurrent._btnconnect.runMethod(true,"setEnabled",main.mostCurrent.__c.runMethod(true,"Not",(Object)(main.mostCurrent._starter._connected /*RemoteObject*/ )));
 BA.debugLineNum = 119;BA.debugLine="progressBar1.Visible = Starter.connecting";
Debug.ShouldStop(4194304);
main.mostCurrent._progressbar1.runMethod(true,"setVisible",main.mostCurrent._starter._connecting /*RemoteObject*/ );
 BA.debugLineNum = 120;BA.debugLine="Dim status As String";
Debug.ShouldStop(8388608);
_status = RemoteObject.createImmutable("");Debug.locals.put("status", _status);
 BA.debugLineNum = 121;BA.debugLine="If Starter.connected Then";
Debug.ShouldStop(16777216);
if (main.mostCurrent._starter._connected /*RemoteObject*/ .<Boolean>get().booleanValue()) { 
 BA.debugLineNum = 122;BA.debugLine="status = \"connected\"";
Debug.ShouldStop(33554432);
_status = BA.ObjectToString("connected");Debug.locals.put("status", _status);
 }else 
{ BA.debugLineNum = 125;BA.debugLine="else if Starter.connecting Then";
Debug.ShouldStop(268435456);
if (main.mostCurrent._starter._connecting /*RemoteObject*/ .<Boolean>get().booleanValue()) { 
 BA.debugLineNum = 126;BA.debugLine="status = \"connecting\"";
Debug.ShouldStop(536870912);
_status = BA.ObjectToString("connecting");Debug.locals.put("status", _status);
 BA.debugLineNum = 127;BA.debugLine="lblStatus.Color=232";
Debug.ShouldStop(1073741824);
main.mostCurrent._lblstatus.runVoidMethod ("setColor",BA.numberCast(int.class, 232));
 }else {
 BA.debugLineNum = 129;BA.debugLine="status = \"disconnected\"";
Debug.ShouldStop(1);
_status = BA.ObjectToString("disconnected");Debug.locals.put("status", _status);
 BA.debugLineNum = 131;BA.debugLine="lblStatus.Color=325";
Debug.ShouldStop(4);
main.mostCurrent._lblstatus.runVoidMethod ("setColor",BA.numberCast(int.class, 325));
 }}
;
 BA.debugLineNum = 133;BA.debugLine="lblStatus.Text = $\" ${status}\"$";
Debug.ShouldStop(16);
main.mostCurrent._lblstatus.runMethod(true,"setText",BA.ObjectToCharSequence((RemoteObject.concat(RemoteObject.createImmutable(" "),main.mostCurrent.__c.runMethod(true,"SmartStringFormatter",(Object)(BA.ObjectToString("")),(Object)((_status))),RemoteObject.createImmutable("")))));
 BA.debugLineNum = 135;BA.debugLine="End Sub";
Debug.ShouldStop(64);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _stopbutton_click() throws Exception{
try {
		Debug.PushSubsStack("stopButton_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,202);
if (RapidSub.canDelegate("stopbutton_click")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","stopbutton_click");}
 BA.debugLineNum = 202;BA.debugLine="Sub stopButton_Click";
Debug.ShouldStop(512);
 BA.debugLineNum = 203;BA.debugLine="audioStreamer.StopRecording ' Detiene la graba";
Debug.ShouldStop(1024);
main._audiostreamer.runVoidMethod ("StopRecording");
 BA.debugLineNum = 204;BA.debugLine="filterLabel.Text = \"Grabación detenida\"";
Debug.ShouldStop(2048);
main.mostCurrent._filterlabel.runMethod(true,"setText",BA.ObjectToCharSequence("Grabación detenida"));
 BA.debugLineNum = 205;BA.debugLine="End Sub";
Debug.ShouldStop(4096);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _swiftmap_click() throws Exception{
try {
		Debug.PushSubsStack("SwiftMap_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,105);
if (RapidSub.canDelegate("swiftmap_click")) { return anywheresoftware.b4a.samples.gps.main.remoteMe.runUserSub(false, "main","swiftmap_click");}
RemoteObject _p = RemoteObject.declareNull("anywheresoftware.b4a.phone.Phone.PhoneIntents");
 BA.debugLineNum = 105;BA.debugLine="Private Sub SwiftMap_Click";
Debug.ShouldStop(256);
 BA.debugLineNum = 106;BA.debugLine="Dim p As PhoneIntents";
Debug.ShouldStop(512);
_p = RemoteObject.createNew ("anywheresoftware.b4a.phone.Phone.PhoneIntents");Debug.locals.put("p", _p);
 BA.debugLineNum = 107;BA.debugLine="StartActivity(p.OpenBrowser(\"https://www.google.c";
Debug.ShouldStop(1024);
main.mostCurrent.__c.runVoidMethod ("StartActivity",main.processBA,(Object)((_p.runMethod(false,"OpenBrowser",(Object)(RemoteObject.createImmutable("https://www.google.com/maps/@?api=1&map_action=map&hl=es-419"))))));
 BA.debugLineNum = 108;BA.debugLine="End Sub";
Debug.ShouldStop(2048);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}