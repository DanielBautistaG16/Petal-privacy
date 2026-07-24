
package anywheresoftware.b4a.samples.gps;

import anywheresoftware.b4a.pc.PCBA;
import anywheresoftware.b4a.pc.RemoteObject;

public class asyncstreamstext {
    public static RemoteObject myClass;
	public asyncstreamstext() {
	}
    public static PCBA staticBA = new PCBA(null, asyncstreamstext.class);

public static RemoteObject __c = RemoteObject.declareNull("anywheresoftware.b4a.keywords.Common");
public static RemoteObject _mtarget = RemoteObject.declareNull("Object");
public static RemoteObject _meventname = RemoteObject.createImmutable("");
public static RemoteObject _astreams = RemoteObject.declareNull("anywheresoftware.b4a.randomaccessfile.AsyncStreams");
public static RemoteObject _charset = RemoteObject.createImmutable("");
public static RemoteObject _sb = RemoteObject.declareNull("anywheresoftware.b4a.keywords.StringBuilderWrapper");
public static RemoteObject _dateutils = RemoteObject.declareNull("b4a.example.dateutils");
public static anywheresoftware.b4a.samples.gps.main _main = null;
public static anywheresoftware.b4a.samples.gps.starter _starter = null;
public static anywheresoftware.b4a.samples.gps.xuiviewsutils _xuiviewsutils = null;
public static Object[] GetGlobals(RemoteObject _ref) throws Exception {
		return new Object[] {"astreams",_ref.getField(false, "_astreams"),"charset",_ref.getField(false, "_charset"),"DateUtils",_ref.getField(false, "_dateutils"),"mEventName",_ref.getField(false, "_meventname"),"mTarget",_ref.getField(false, "_mtarget"),"sb",_ref.getField(false, "_sb")};
}
}