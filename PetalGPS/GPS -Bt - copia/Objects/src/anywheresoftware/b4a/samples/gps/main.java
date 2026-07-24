package anywheresoftware.b4a.samples.gps;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new anywheresoftware.b4a.ShellBA(this.getApplicationContext(), null, null, "anywheresoftware.b4a.samples.gps", "anywheresoftware.b4a.samples.gps.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "anywheresoftware.b4a.samples.gps", "anywheresoftware.b4a.samples.gps.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "anywheresoftware.b4a.samples.gps.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create " + (isFirst ? "(first time)" : "") + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }



public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}

private static BA killProgramHelper(BA ba) {
    if (ba == null)
        return null;
    anywheresoftware.b4a.BA.SharedProcessBA sharedProcessBA = ba.sharedProcessBA;
    if (sharedProcessBA == null || sharedProcessBA.activityBA == null)
        return null;
    return sharedProcessBA.activityBA.get();
}
public static void killProgram() {
     {
            Activity __a = null;
            if (main.previousOne != null) {
				__a = main.previousOne.get();
			}
            else {
                BA ba = killProgramHelper(main.mostCurrent == null ? null : main.mostCurrent.processBA);
                if (ba != null) __a = ba.activity;
            }
            if (__a != null)
				__a.finish();}

BA.applicationContext.stopService(new android.content.Intent(BA.applicationContext, starter.class));
}
public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.gps.GPS _gps1 = null;
public static anywheresoftware.b4a.audio.AudioStreamer _audiostreamer = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllon = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblspeed = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsatellites = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnconnect = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblstatus = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblmessage = null;
public anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public anywheresoftware.b4a.samples.gps.b4ximageview _b4ximageview2 = null;
public anywheresoftware.b4a.samples.gps.b4xswitch _b4xswitch1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _recbutton = null;
public anywheresoftware.b4a.objects.ButtonWrapper _stopbutton = null;
public anywheresoftware.b4a.objects.LabelWrapper _filterlabel = null;
public b4a.example.dateutils _dateutils = null;
public anywheresoftware.b4a.samples.gps.starter _starter = null;
public anywheresoftware.b4a.samples.gps.xuiviewsutils _xuiviewsutils = null;
public static String  _activity_create(boolean _firsttime) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "activity_create", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "activity_create", new Object[] {_firsttime}));}
RDebugUtils.currentLine=131072;
 //BA.debugLineNum = 131072;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
RDebugUtils.currentLine=131073;
 //BA.debugLineNum = 131073;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
RDebugUtils.currentLine=131074;
 //BA.debugLineNum = 131074;BA.debugLine="GPS1.Initialize(\"GPS\")";
_gps1.Initialize("GPS");
RDebugUtils.currentLine=131075;
 //BA.debugLineNum = 131075;BA.debugLine="Log(\"GPS\")";
anywheresoftware.b4a.keywords.Common.LogImpl("2131075","GPS",0);
 };
RDebugUtils.currentLine=131079;
 //BA.debugLineNum = 131079;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
RDebugUtils.currentLine=131080;
 //BA.debugLineNum = 131080;BA.debugLine="Activity.LoadLayout(\"2\")";
mostCurrent._activity.LoadLayout("2",mostCurrent.activityBA);
RDebugUtils.currentLine=131083;
 //BA.debugLineNum = 131083;BA.debugLine="audioStreamer.Initialize(\"audioStreamer\", 44100,";
_audiostreamer.Initialize(processBA,"audioStreamer",(int) (44100),anywheresoftware.b4a.keywords.Common.True,(int) (16),(int) (1));
RDebugUtils.currentLine=131088;
 //BA.debugLineNum = 131088;BA.debugLine="recButton.Initialize(\"recButton\")";
mostCurrent._recbutton.Initialize(mostCurrent.activityBA,"recButton");
RDebugUtils.currentLine=131089;
 //BA.debugLineNum = 131089;BA.debugLine="stopButton.Initialize(\"stopButton\")";
mostCurrent._stopbutton.Initialize(mostCurrent.activityBA,"stopButton");
RDebugUtils.currentLine=131090;
 //BA.debugLineNum = 131090;BA.debugLine="filterLabel.Initialize(\"filterLabel\")";
mostCurrent._filterlabel.Initialize(mostCurrent.activityBA,"filterLabel");
RDebugUtils.currentLine=131092;
 //BA.debugLineNum = 131092;BA.debugLine="recButton.Text = \"Grabar\"";
mostCurrent._recbutton.setText(BA.ObjectToCharSequence("Grabar"));
RDebugUtils.currentLine=131093;
 //BA.debugLineNum = 131093;BA.debugLine="stopButton.Text = \"Detener\"";
mostCurrent._stopbutton.setText(BA.ObjectToCharSequence("Detener"));
RDebugUtils.currentLine=131094;
 //BA.debugLineNum = 131094;BA.debugLine="filterLabel.Text = \"Preparado para grabar audio\"";
mostCurrent._filterlabel.setText(BA.ObjectToCharSequence("Preparado para grabar audio"));
RDebugUtils.currentLine=131095;
 //BA.debugLineNum = 131095;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
RDebugUtils.currentModule="main";
RDebugUtils.currentLine=262144;
 //BA.debugLineNum = 262144;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
RDebugUtils.currentLine=262145;
 //BA.debugLineNum = 262145;BA.debugLine="GPS1.Stop";
_gps1.Stop();
RDebugUtils.currentLine=262146;
 //BA.debugLineNum = 262146;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "activity_resume", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "activity_resume", null));}
RDebugUtils.currentLine=196608;
 //BA.debugLineNum = 196608;BA.debugLine="Sub Activity_Resume";
RDebugUtils.currentLine=196609;
 //BA.debugLineNum = 196609;BA.debugLine="If GPS1.GPSEnabled = False Then";
if (_gps1.getGPSEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
RDebugUtils.currentLine=196610;
 //BA.debugLineNum = 196610;BA.debugLine="ToastMessageShow(\"Please enable the GPS device.\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please enable the GPS device."),anywheresoftware.b4a.keywords.Common.True);
RDebugUtils.currentLine=196611;
 //BA.debugLineNum = 196611;BA.debugLine="StartActivity(GPS1.LocationSettingsIntent) 'Will";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_gps1.getLocationSettingsIntent()));
 }else {
RDebugUtils.currentLine=196613;
 //BA.debugLineNum = 196613;BA.debugLine="GPS1.Start(0, 0) 'Listen to GPS with no filters.";
_gps1.Start(processBA,(long) (0),(float) (0));
 };
RDebugUtils.currentLine=196616;
 //BA.debugLineNum = 196616;BA.debugLine="End Sub";
return "";
}
public static String  _audiostreamer_recordbuffer(byte[] _buffer) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "audiostreamer_recordbuffer", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "audiostreamer_recordbuffer", new Object[] {_buffer}));}
RDebugUtils.currentLine=1114112;
 //BA.debugLineNum = 1114112;BA.debugLine="Sub audioStreamer_RecordBuffer (Buffer() As Byte)";
RDebugUtils.currentLine=1114119;
 //BA.debugLineNum = 1114119;BA.debugLine="End Sub";
return "";
}
public static void  _b4xswitch1_valuechanged(boolean _value) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "b4xswitch1_valuechanged", false))
	 {Debug.delegate(mostCurrent.activityBA, "b4xswitch1_valuechanged", new Object[] {_value}); return;}
ResumableSub_B4XSwitch1_ValueChanged rsub = new ResumableSub_B4XSwitch1_ValueChanged(null,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_B4XSwitch1_ValueChanged extends BA.ResumableSub {
public ResumableSub_B4XSwitch1_ValueChanged(anywheresoftware.b4a.samples.gps.main parent,boolean _value) {
this.parent = parent;
this._value = _value;
}
anywheresoftware.b4a.samples.gps.main parent;
boolean _value;
String _permission = "";
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{
RDebugUtils.currentModule="main";

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
RDebugUtils.currentLine=917505;
 //BA.debugLineNum = 917505;BA.debugLine="If Value=True Then";
if (true) break;

case 1:
//if
this.state = 12;
if (_value==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}else {
this.state = 11;
}if (true) break;

case 3:
//C
this.state = 4;
RDebugUtils.currentLine=917506;
 //BA.debugLineNum = 917506;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_ACCESS_COARSE_L";
parent.mostCurrent._rp.CheckAndRequest(processBA,parent.mostCurrent._rp.PERMISSION_ACCESS_COARSE_LOCATION);
RDebugUtils.currentLine=917507;
 //BA.debugLineNum = 917507;BA.debugLine="Log(\"Esperando\")";
anywheresoftware.b4a.keywords.Common.LogImpl("2917507","Esperando",0);
RDebugUtils.currentLine=917508;
 //BA.debugLineNum = 917508;BA.debugLine="Wait For Activity_PermissionResult (Permission A";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, new anywheresoftware.b4a.shell.DebugResumableSub.DelegatableResumableSub(this, "main", "b4xswitch1_valuechanged"), null);
this.state = 13;
return;
case 13:
//C
this.state = 4;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
RDebugUtils.currentLine=917509;
 //BA.debugLineNum = 917509;BA.debugLine="If Result Then";
if (true) break;

case 4:
//if
this.state = 9;
if (_result) { 
this.state = 6;
}else {
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
RDebugUtils.currentLine=917510;
 //BA.debugLineNum = 917510;BA.debugLine="CallSub(Starter, \"Connect\")";
anywheresoftware.b4a.keywords.Common.CallSubDebug(processBA,(Object)(parent.mostCurrent._starter.getObject()),"Connect");
 if (true) break;

case 8:
//C
this.state = 9;
RDebugUtils.currentLine=917512;
 //BA.debugLineNum = 917512;BA.debugLine="ToastMessageShow(\"No permission\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No permission"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 9:
//C
this.state = 12;
;
 if (true) break;

case 11:
//C
this.state = 12;
RDebugUtils.currentLine=917515;
 //BA.debugLineNum = 917515;BA.debugLine="Starter.connected=False";
parent.mostCurrent._starter._connected /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
RDebugUtils.currentLine=917516;
 //BA.debugLineNum = 917516;BA.debugLine="Log(\"desconectado\")";
anywheresoftware.b4a.keywords.Common.LogImpl("2917516","desconectado",0);
RDebugUtils.currentLine=917517;
 //BA.debugLineNum = 917517;BA.debugLine="SetDesconectar";
_setdesconectar();
RDebugUtils.currentLine=917518;
 //BA.debugLineNum = 917518;BA.debugLine="CallSub(Starter, \"disConnect\")";
anywheresoftware.b4a.keywords.Common.CallSubDebug(processBA,(Object)(parent.mostCurrent._starter.getObject()),"disConnect");
 if (true) break;

case 12:
//C
this.state = -1;
;
RDebugUtils.currentLine=917521;
 //BA.debugLineNum = 917521;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _setdesconectar() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "setdesconectar", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "setdesconectar", null));}
RDebugUtils.currentLine=720896;
 //BA.debugLineNum = 720896;BA.debugLine="Public Sub SetDesconectar";
RDebugUtils.currentLine=720897;
 //BA.debugLineNum = 720897;BA.debugLine="progressBar1.Visible = Starter.connected";
mostCurrent._progressbar1.setVisible(mostCurrent._starter._connected /*boolean*/ );
RDebugUtils.currentLine=720898;
 //BA.debugLineNum = 720898;BA.debugLine="End Sub";
return "";
}
public static String  _btnchat_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "btnchat_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "btnchat_click", null));}
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
RDebugUtils.currentLine=1179648;
 //BA.debugLineNum = 1179648;BA.debugLine="Private Sub BtnChat_Click";
RDebugUtils.currentLine=1179649;
 //BA.debugLineNum = 1179649;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
RDebugUtils.currentLine=1179650;
 //BA.debugLineNum = 1179650;BA.debugLine="StartActivity(p.OpenBrowser(\"https://chatgpt.com/";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_p.OpenBrowser("https://chatgpt.com/share/678fb915-d3d0-8006-ac60-aeb54a0efa2e")));
RDebugUtils.currentLine=1179652;
 //BA.debugLineNum = 1179652;BA.debugLine="End Sub";
return "";
}
public static void  _btnconnect_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "btnconnect_click", false))
	 {Debug.delegate(mostCurrent.activityBA, "btnconnect_click", null); return;}
ResumableSub_btnConnect_Click rsub = new ResumableSub_btnConnect_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnConnect_Click extends BA.ResumableSub {
public ResumableSub_btnConnect_Click(anywheresoftware.b4a.samples.gps.main parent) {
this.parent = parent;
}
anywheresoftware.b4a.samples.gps.main parent;
String _permission = "";
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{
RDebugUtils.currentModule="main";

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
RDebugUtils.currentLine=851969;
 //BA.debugLineNum = 851969;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_ACCESS_COARSE_LO";
parent.mostCurrent._rp.CheckAndRequest(processBA,parent.mostCurrent._rp.PERMISSION_ACCESS_COARSE_LOCATION);
RDebugUtils.currentLine=851970;
 //BA.debugLineNum = 851970;BA.debugLine="Log(\"Esperando\")";
anywheresoftware.b4a.keywords.Common.LogImpl("2851970","Esperando",0);
RDebugUtils.currentLine=851971;
 //BA.debugLineNum = 851971;BA.debugLine="Wait For Activity_PermissionResult (Permission As";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, new anywheresoftware.b4a.shell.DebugResumableSub.DelegatableResumableSub(this, "main", "btnconnect_click"), null);
this.state = 7;
return;
case 7:
//C
this.state = 1;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
RDebugUtils.currentLine=851972;
 //BA.debugLineNum = 851972;BA.debugLine="If Result Then";
if (true) break;

case 1:
//if
this.state = 6;
if (_result) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
RDebugUtils.currentLine=851973;
 //BA.debugLineNum = 851973;BA.debugLine="CallSub(Starter, \"Connect\")";
anywheresoftware.b4a.keywords.Common.CallSubDebug(processBA,(Object)(parent.mostCurrent._starter.getObject()),"Connect");
 if (true) break;

case 5:
//C
this.state = 6;
RDebugUtils.currentLine=851976;
 //BA.debugLineNum = 851976;BA.debugLine="ToastMessageShow(\"No permission\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No permission"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 6:
//C
this.state = -1;
;
RDebugUtils.currentLine=851978;
 //BA.debugLineNum = 851978;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _btnlogo_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "btnlogo_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "btnlogo_click", null));}
anywheresoftware.b4a.phone.Phone.PhoneIntents _t = null;
RDebugUtils.currentLine=1245184;
 //BA.debugLineNum = 1245184;BA.debugLine="Private Sub BtnLogo_Click";
RDebugUtils.currentLine=1245185;
 //BA.debugLineNum = 1245185;BA.debugLine="Dim t As PhoneIntents";
_t = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
RDebugUtils.currentLine=1245186;
 //BA.debugLineNum = 1245186;BA.debugLine="StartActivity(t.OpenBrowser(\"https://ridepetal.co";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_t.OpenBrowser("https://ridepetal.com/")));
RDebugUtils.currentLine=1245188;
 //BA.debugLineNum = 1245188;BA.debugLine="End Sub";
return "";
}
public static String  _gps_gnssstatus(anywheresoftware.b4a.objects.collections.List _satellites) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "gps_gnssstatus", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "gps_gnssstatus", new Object[] {_satellites}));}
int _i = 0;
anywheresoftware.b4a.gps.GpsSatelliteWrapper _satellite = null;
RDebugUtils.currentLine=458752;
 //BA.debugLineNum = 458752;BA.debugLine="Sub GPS_GnssStatus (Satellites As List)";
RDebugUtils.currentLine=458753;
 //BA.debugLineNum = 458753;BA.debugLine="lblSatellites.Text = \"Satellites:\" & CRLF";
mostCurrent._lblsatellites.setText(BA.ObjectToCharSequence("Satellites:"+anywheresoftware.b4a.keywords.Common.CRLF));
RDebugUtils.currentLine=458754;
 //BA.debugLineNum = 458754;BA.debugLine="For i = 0 To Satellites.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (_satellites.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
RDebugUtils.currentLine=458755;
 //BA.debugLineNum = 458755;BA.debugLine="Dim Satellite As GPSSatellite";
_satellite = new anywheresoftware.b4a.gps.GpsSatelliteWrapper();
RDebugUtils.currentLine=458756;
 //BA.debugLineNum = 458756;BA.debugLine="Satellite = Satellites.Get(i)";
_satellite = (anywheresoftware.b4a.gps.GpsSatelliteWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.gps.GpsSatelliteWrapper(), (android.location.GpsSatellite)(_satellites.Get(_i)));
RDebugUtils.currentLine=458757;
 //BA.debugLineNum = 458757;BA.debugLine="lblSatellites.Text = lblSatellites.Text & CRLF &";
mostCurrent._lblsatellites.setText(BA.ObjectToCharSequence(mostCurrent._lblsatellites.getText()+anywheresoftware.b4a.keywords.Common.CRLF+BA.NumberToString(_satellite.getPrn())+" "+BA.NumberToString(_satellite.getSnr())+" "+BA.ObjectToString(_satellite.getUsedInFix())+" "+BA.NumberToString(_satellite.getAzimuth())+" "+BA.NumberToString(_satellite.getElevation())));
 }
};
RDebugUtils.currentLine=458761;
 //BA.debugLineNum = 458761;BA.debugLine="End Sub";
return "";
}
public static String  _gps_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "gps_locationchanged", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "gps_locationchanged", new Object[] {_location1}));}
RDebugUtils.currentLine=327680;
 //BA.debugLineNum = 327680;BA.debugLine="Sub GPS_LocationChanged (Location1 As Location)";
RDebugUtils.currentLine=327681;
 //BA.debugLineNum = 327681;BA.debugLine="lblLat.Text = \"Lat = \" & Location1.ConvertToMinut";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence("Lat = "+_location1.ConvertToMinutes(_location1.getLatitude())));
RDebugUtils.currentLine=327682;
 //BA.debugLineNum = 327682;BA.debugLine="lblLon.Text = \"Lon = \" & Location1.ConvertToMinut";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence("Lon = "+_location1.ConvertToMinutes(_location1.getLongitude())));
RDebugUtils.currentLine=327685;
 //BA.debugLineNum = 327685;BA.debugLine="lblSpeed.Text = NumberFormat(1.60934*60/25* Locat";
mostCurrent._lblspeed.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.NumberFormat(1.60934*60/(double)25*_location1.getSpeed(),(int) (2),(int) (0))));
RDebugUtils.currentLine=327686;
 //BA.debugLineNum = 327686;BA.debugLine="End Sub";
return "";
}
public static String  _gps_userenabled(boolean _enabled) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "gps_userenabled", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "gps_userenabled", new Object[] {_enabled}));}
RDebugUtils.currentLine=393216;
 //BA.debugLineNum = 393216;BA.debugLine="Sub GPS_UserEnabled (Enabled As Boolean)";
RDebugUtils.currentLine=393217;
 //BA.debugLineNum = 393217;BA.debugLine="ToastMessageShow(\"GPS device enabled = \" & Enable";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("GPS device enabled = "+BA.ObjectToString(_enabled)),anywheresoftware.b4a.keywords.Common.True);
RDebugUtils.currentLine=393218;
 //BA.debugLineNum = 393218;BA.debugLine="End Sub";
return "";
}
public static String  _lblmap_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "lblmap_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "lblmap_click", null));}
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
RDebugUtils.currentLine=589824;
 //BA.debugLineNum = 589824;BA.debugLine="Private Sub lblMap_Click";
RDebugUtils.currentLine=589825;
 //BA.debugLineNum = 589825;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
RDebugUtils.currentLine=589826;
 //BA.debugLineNum = 589826;BA.debugLine="StartActivity(p.OpenBrowser(\"https://www.google.c";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_p.OpenBrowser("https://www.google.com/maps/@?api=1&map_action=map&hl=es-419")));
RDebugUtils.currentLine=589827;
 //BA.debugLineNum = 589827;BA.debugLine="End Sub";
return "";
}
public static String  _messagefromdevice(String _msg) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "messagefromdevice", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "messagefromdevice", new Object[] {_msg}));}
int _nivelbat = 0;
RDebugUtils.currentLine=786432;
 //BA.debugLineNum = 786432;BA.debugLine="Public Sub MessageFromDevice(msg As String)";
RDebugUtils.currentLine=786433;
 //BA.debugLineNum = 786433;BA.debugLine="Dim NivelBat= Round2(msg/5*100,0) As Int";
_nivelbat = (int) (anywheresoftware.b4a.keywords.Common.Round2((double)(Double.parseDouble(_msg))/(double)5*100,(int) (0)));
RDebugUtils.currentLine=786435;
 //BA.debugLineNum = 786435;BA.debugLine="lblMessage.Text=\"BATTERY LEVEL \"&CRLF&NivelBat &\"";
mostCurrent._lblmessage.setText(BA.ObjectToCharSequence("BATTERY LEVEL "+anywheresoftware.b4a.keywords.Common.CRLF+BA.NumberToString(_nivelbat)+" %"));
RDebugUtils.currentLine=786436;
 //BA.debugLineNum = 786436;BA.debugLine="Log(lblMessage.Text)";
anywheresoftware.b4a.keywords.Common.LogImpl("2786436",mostCurrent._lblmessage.getText(),0);
RDebugUtils.currentLine=786437;
 //BA.debugLineNum = 786437;BA.debugLine="Log(msg)";
anywheresoftware.b4a.keywords.Common.LogImpl("2786437",_msg,0);
RDebugUtils.currentLine=786440;
 //BA.debugLineNum = 786440;BA.debugLine="End Sub";
return "";
}
public static String  _recbutton_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "recbutton_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "recbutton_click", null));}
RDebugUtils.currentLine=983040;
 //BA.debugLineNum = 983040;BA.debugLine="Sub recButton_Click";
RDebugUtils.currentLine=983041;
 //BA.debugLineNum = 983041;BA.debugLine="audioStreamer.StartRecording ' Inicia la graba";
_audiostreamer.StartRecording();
RDebugUtils.currentLine=983042;
 //BA.debugLineNum = 983042;BA.debugLine="filterLabel.Text = \"Grabando...\"";
mostCurrent._filterlabel.setText(BA.ObjectToCharSequence("Grabando..."));
RDebugUtils.currentLine=983043;
 //BA.debugLineNum = 983043;BA.debugLine="End Sub";
return "";
}
public static String  _setstate() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "setstate", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "setstate", null));}
String _status = "";
RDebugUtils.currentLine=655360;
 //BA.debugLineNum = 655360;BA.debugLine="Public Sub SetState";
RDebugUtils.currentLine=655362;
 //BA.debugLineNum = 655362;BA.debugLine="btnConnect.Enabled = Not(Starter.connected)";
mostCurrent._btnconnect.setEnabled(anywheresoftware.b4a.keywords.Common.Not(mostCurrent._starter._connected /*boolean*/ ));
RDebugUtils.currentLine=655363;
 //BA.debugLineNum = 655363;BA.debugLine="progressBar1.Visible = Starter.connecting";
mostCurrent._progressbar1.setVisible(mostCurrent._starter._connecting /*boolean*/ );
RDebugUtils.currentLine=655364;
 //BA.debugLineNum = 655364;BA.debugLine="Dim status As String";
_status = "";
RDebugUtils.currentLine=655365;
 //BA.debugLineNum = 655365;BA.debugLine="If Starter.connected Then";
if (mostCurrent._starter._connected /*boolean*/ ) { 
RDebugUtils.currentLine=655366;
 //BA.debugLineNum = 655366;BA.debugLine="status = \"connected\"";
_status = "connected";
 }else 
{RDebugUtils.currentLine=655369;
 //BA.debugLineNum = 655369;BA.debugLine="else if Starter.connecting Then";
if (mostCurrent._starter._connecting /*boolean*/ ) { 
RDebugUtils.currentLine=655370;
 //BA.debugLineNum = 655370;BA.debugLine="status = \"connecting\"";
_status = "connecting";
RDebugUtils.currentLine=655371;
 //BA.debugLineNum = 655371;BA.debugLine="lblStatus.Color=232";
mostCurrent._lblstatus.setColor((int) (232));
 }else {
RDebugUtils.currentLine=655373;
 //BA.debugLineNum = 655373;BA.debugLine="status = \"disconnected\"";
_status = "disconnected";
RDebugUtils.currentLine=655375;
 //BA.debugLineNum = 655375;BA.debugLine="lblStatus.Color=325";
mostCurrent._lblstatus.setColor((int) (325));
 }}
;
RDebugUtils.currentLine=655377;
 //BA.debugLineNum = 655377;BA.debugLine="lblStatus.Text = $\" ${status}\"$";
mostCurrent._lblstatus.setText(BA.ObjectToCharSequence((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("",(Object)(_status))+"")));
RDebugUtils.currentLine=655379;
 //BA.debugLineNum = 655379;BA.debugLine="End Sub";
return "";
}
public static String  _stopbutton_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "stopbutton_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "stopbutton_click", null));}
RDebugUtils.currentLine=1048576;
 //BA.debugLineNum = 1048576;BA.debugLine="Sub stopButton_Click";
RDebugUtils.currentLine=1048577;
 //BA.debugLineNum = 1048577;BA.debugLine="audioStreamer.StopRecording ' Detiene la graba";
_audiostreamer.StopRecording();
RDebugUtils.currentLine=1048578;
 //BA.debugLineNum = 1048578;BA.debugLine="filterLabel.Text = \"Grabación detenida\"";
mostCurrent._filterlabel.setText(BA.ObjectToCharSequence("Grabación detenida"));
RDebugUtils.currentLine=1048579;
 //BA.debugLineNum = 1048579;BA.debugLine="End Sub";
return "";
}
public static String  _swiftmap_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "swiftmap_click", false))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "swiftmap_click", null));}
anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
RDebugUtils.currentLine=524288;
 //BA.debugLineNum = 524288;BA.debugLine="Private Sub SwiftMap_Click";
RDebugUtils.currentLine=524289;
 //BA.debugLineNum = 524289;BA.debugLine="Dim p As PhoneIntents";
_p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
RDebugUtils.currentLine=524290;
 //BA.debugLineNum = 524290;BA.debugLine="StartActivity(p.OpenBrowser(\"https://www.google.c";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_p.OpenBrowser("https://www.google.com/maps/@?api=1&map_action=map&hl=es-419")));
RDebugUtils.currentLine=524291;
 //BA.debugLineNum = 524291;BA.debugLine="End Sub";
return "";
}
}