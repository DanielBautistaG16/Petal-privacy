
package anywheresoftware.b4a.samples.gps;

import java.io.IOException;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.PCBA;
import anywheresoftware.b4a.pc.RDebug;
import anywheresoftware.b4a.pc.RemoteObject;
import anywheresoftware.b4a.pc.RDebug.IRemote;
import anywheresoftware.b4a.pc.Debug;
import anywheresoftware.b4a.pc.B4XTypes.B4XClass;
import anywheresoftware.b4a.pc.B4XTypes.DeviceClass;

public class main implements IRemote{
	public static main mostCurrent;
	public static RemoteObject processBA;
    public static boolean processGlobalsRun;
    public static RemoteObject myClass;
    public static RemoteObject remoteMe;
	public main() {
		mostCurrent = this;
	}
    public RemoteObject getRemoteMe() {
        return remoteMe;    
    }
    
	public static void main (String[] args) throws Exception {
		new RDebug(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
		RDebug.INSTANCE.waitForTask();

	}
    static {
        anywheresoftware.b4a.pc.RapidSub.moduleToObject.put(new B4XClass("main"), "anywheresoftware.b4a.samples.gps.main");
	}

public boolean isSingleton() {
		return true;
	}
     public static RemoteObject getObject() {
		return myClass;
	 }

	public RemoteObject activityBA;
	public RemoteObject _activity;
    private PCBA pcBA;

	public PCBA create(Object[] args) throws ClassNotFoundException{
		processBA = (RemoteObject) args[1];
		activityBA = (RemoteObject) args[2];
		_activity = (RemoteObject) args[3];
        anywheresoftware.b4a.keywords.Common.Density = (Float)args[4];
        remoteMe = (RemoteObject) args[5];
		pcBA = new PCBA(this, main.class);
        main_subs_0.initializeProcessGlobals();
		return pcBA;
	}
public static RemoteObject __c = RemoteObject.declareNull("anywheresoftware.b4a.keywords.Common");
public static RemoteObject _gps1 = RemoteObject.declareNull("anywheresoftware.b4a.gps.GPS");
public static RemoteObject _audiostreamer = RemoteObject.declareNull("anywheresoftware.b4a.audio.AudioStreamer");
public static RemoteObject _lbllon = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _lbllat = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _lblspeed = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _lblsatellites = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _btnconnect = RemoteObject.declareNull("anywheresoftware.b4a.objects.ButtonWrapper");
public static RemoteObject _progressbar1 = RemoteObject.declareNull("anywheresoftware.b4a.objects.ProgressBarWrapper");
public static RemoteObject _progressbar2 = RemoteObject.declareNull("anywheresoftware.b4a.objects.ProgressBarWrapper");
public static RemoteObject _lblstatus = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _lblmessage = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _rp = RemoteObject.declareNull("anywheresoftware.b4a.objects.RuntimePermissions");
public static RemoteObject _b4ximageview2 = RemoteObject.declareNull("anywheresoftware.b4a.samples.gps.b4ximageview");
public static RemoteObject _b4xswitch1 = RemoteObject.declareNull("anywheresoftware.b4a.samples.gps.b4xswitch");
public static RemoteObject _recbutton = RemoteObject.declareNull("anywheresoftware.b4a.objects.ButtonWrapper");
public static RemoteObject _stopbutton = RemoteObject.declareNull("anywheresoftware.b4a.objects.ButtonWrapper");
public static RemoteObject _filterlabel = RemoteObject.declareNull("anywheresoftware.b4a.objects.LabelWrapper");
public static RemoteObject _dateutils = RemoteObject.declareNull("b4a.example.dateutils");
public static anywheresoftware.b4a.samples.gps.starter _starter = null;
public static anywheresoftware.b4a.samples.gps.xuiviewsutils _xuiviewsutils = null;
  public Object[] GetGlobals() {
		return new Object[] {"Activity",main.mostCurrent._activity,"audioStreamer",main._audiostreamer,"B4XImageView2",main.mostCurrent._b4ximageview2,"b4xSwitch1",main.mostCurrent._b4xswitch1,"btnConnect",main.mostCurrent._btnconnect,"DateUtils",main.mostCurrent._dateutils,"filterLabel",main.mostCurrent._filterlabel,"GPS1",main._gps1,"lblLat",main.mostCurrent._lbllat,"lblLon",main.mostCurrent._lbllon,"lblMessage",main.mostCurrent._lblmessage,"lblSatellites",main.mostCurrent._lblsatellites,"lblSpeed",main.mostCurrent._lblspeed,"lblStatus",main.mostCurrent._lblstatus,"progressBar1",main.mostCurrent._progressbar1,"progressBar2",main.mostCurrent._progressbar2,"recButton",main.mostCurrent._recbutton,"rp",main.mostCurrent._rp,"Starter",Debug.moduleToString(anywheresoftware.b4a.samples.gps.starter.class),"stopButton",main.mostCurrent._stopbutton,"XUIViewsUtils",Debug.moduleToString(anywheresoftware.b4a.samples.gps.xuiviewsutils.class)};
}
}