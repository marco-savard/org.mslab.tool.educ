package org.mslab.tool.educ.client.core.system;

import com.google.gwt.user.client.Window.Navigator;

public abstract class UserAgent {
	
	//unit test
	public static void main(String[] args) {
		String USER_AGENT = "mozilla/5.0 (linux; u; android 4.1.2; en-us; nexus s build/jzo54k) applewebkit/534.30 (khtml, like gecko) version /4.0 mobile safari/534.30";
		
		UserAgent info = UserAgent.parse(USER_AGENT);
		String text = info.toString(); 
		System.out.println(text);
	}
	
	private static UserAgent _info = null;
	protected String _userAgent = "?"; 
	
	public static UserAgent getInstance() {
		String userAgent = Navigator.getUserAgent().toLowerCase();
		UserAgent info = parse(userAgent);
		return info;
	}
	
	//browser
	public enum Browser {ANDROID_BROWSER, CHROME, CHROME_IOS, FIREFOX, IE, OPERA, OPERA_MINI, SAFARI, UNKNOWN};
	protected Browser _browser = Browser.UNKNOWN;
	
	//os
	public enum OS {ANDROID, BB, BE_OS, DRAGONFLY, FREE_BSD, IOS, LINUX, NET_BSD, OPEN_BSD, MAC_OS, SUN_OS, SYMBIAN, UNIX, WINDOWS, UNKNOWN};
	private String[] OS_NAMES = new String[] {"Android", "BlackBerry", "Be OS", "DragonFly", "Free BSD", "iOS", "Linux", "Net BSD", "Open BSD", "Mac OS", "SunOS", "Symbian", "Unix", "Windows", "Unknown"};
	protected OS _os = OS.UNKNOWN;
	protected String _osVersion = "?";
	
	//computer
	public enum ComputerType {ANDROID_MOBILE, BB, IPAD, IPHONE, IPOD, MACINTOSH, PC, SYMBIAN_MOBILE, WINDOWS_MOBILE, UNKNOWN};
	private String[] COMPUTER_TYPE_NAMES = new String[] {"Android", "BlackBerry", "iPad", "iPhone", "iPod", "Macintosh", "PC", "Symbian Mobile", "Windows Mobile", "Unknown"};
	protected ComputerType _computerType = ComputerType.UNKNOWN;
	private String _computerName = null;
	
	protected UserAgent(String userAgent) {
		_userAgent = userAgent;
	}
	
	@Override 
	public String toString() {
		String txt = getComputerTypeName() + getComputerName();
		return txt;
	}
	
	public static UserAgent parse(String userAgent) {
		if (_info == null) {
			//String userAgent = Navigator.getUserAgent().toLowerCase(); 
			int idx = userAgent.indexOf("opera mini");
			
			if (idx != -1) {
				_info = new OperaMiniBrowserInfo(userAgent);
			}
		
			if (_info == null) {
				idx = userAgent.indexOf("opera");
				_info = (idx == -1) ? null : new OperaBrowserInfo(userAgent); 
			}
		
			if (_info == null) {
				idx = userAgent.indexOf("chrome");
				_info = (idx == -1) ? null : new ChromeBrowserInfo(userAgent); 
			}
			
			if (_info == null) {
				idx = userAgent.indexOf("crios");
				_info = (idx == -1) ? null : new ChromeIosBrowserInfo(userAgent); 
			}
			
			if (_info == null) {
				int i1 = userAgent.indexOf("mobile safari");
				int i2 = userAgent.indexOf("linux");
				_info = ((i1 == -1) || (i2 == -1)) ? null : new AndroidBrowserInfo(userAgent); 
			}
		
			if (_info == null) {
				idx = userAgent.indexOf("safari");
				_info = (idx == -1) ? null : new SafariBrowserInfo(userAgent); 
			}
			
			if (_info == null) {
				idx = userAgent.indexOf("firefox");
				_info = (idx == -1) ? null : new FirefoxBrowserInfo(userAgent); 
			}
		
			if (_info == null) {
				idx = userAgent.indexOf("msie");
				_info = (idx == -1) ? null : new IEBrowserInfo(userAgent); 
			}
			
			if (_info == null) {
				_info = new UnknownBrowserInfo(userAgent);
			}
		
			try {
				_info.detectOS();
				_info.detectComputerType();
			} catch (RuntimeException ex) {
				//let _info to default values
			}
		}
		
		return _info;
	}

	public Browser getBrowser() {
		return _browser;
	}
	
	public abstract String getBrowserName();
	public abstract String getBrowserVersion();

	protected void detectOS() {
		_os = OS.UNKNOWN; 
		int i1 = _userAgent.indexOf("dragonfly");
		
		if (i1 != -1) {
			_os = OS.DRAGONFLY;
		} 
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("sunos");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.SUN_OS;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("freebsd");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.FREE_BSD;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("openbsd");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.OPEN_BSD;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("netbsd");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.NET_BSD;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("beos");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.BE_OS;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("android");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.ANDROID;
			_computerType = ComputerType.ANDROID_MOBILE;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("linux");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.LINUX;
			_computerType = ComputerType.PC;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("windows");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.WINDOWS;
			detectWindowsVersion();
			_computerType = ComputerType.PC;
		}
		
		if (_os == OS.UNKNOWN) {
			i1 = _userAgent.indexOf("macintosh");
			_os = (i1 == -1) ? OS.UNKNOWN : OS.MAC_OS;
			detectMacVersion();
			_computerType = ComputerType.MACINTOSH;
		}
	}
	
	private void detectMacVersion() {
		int i1 = _userAgent.indexOf(MAC_OS_X);
		
		if (i1 > -1) {
			i1 += MAC_OS_X.length();
			int i2 = _userAgent.indexOf(";", i1+1);
			String v = _userAgent.substring(i1, i2);
			v = v.trim(); 
			v = v.replaceAll("_", ".");
			_osVersion = v;
		}
	}
	private static final String MAC_OS_X = "mac os x";

	private void detectWindowsVersion() {
		int i1 = _userAgent.indexOf(WINDOWS_NT);
		
		if (i1 > -1) {
			i1 += WINDOWS_NT.length(); 
			int i2 = _userAgent.indexOf(";", i1+1);
			String v = _userAgent.substring(i1, i2);
			v = v.trim(); 
			
			if ("6.3".equals(v)) {
				_osVersion = "Windows 8.1";
			} else if ("6.2".equals(v)) {
				_osVersion = "Windows 8";
			} else if ("6.1".equals(v)) {
				_osVersion = "Windows 7";
			} else if ("6.0".equals(v)) {
				_osVersion = "Windows Vista";
			} else if ("5.2".equals(v)) {
				_osVersion = "Windows XP x64";
			} else if ("5.1".equals(v)) {
				_osVersion = "Windows XP";
			} else if ("5.0".equals(v)) {
				_osVersion = "Windows 2000";
			} else if ("4.0".equals(v)) {
				_osVersion = "Windows NT";
			} else {
				_osVersion = v;
			}
		}
	}
	private static final String WINDOWS_NT = "windows nt";
	
	public String getUserAgent() {
		return _userAgent;
	}
	
	public OS getOS() {
		return _os;
	}

	public String getOsName() {
		int osIdx = _os.ordinal();
		String osName = OS_NAMES[osIdx];
		return osName;
	}
	
	public String getOsVersion() {
		return _osVersion;
	}
	
	public ComputerType getComputerType() {
		return _computerType;
	}
	
	//may be null
	public String getComputerName() {
		String computerName = _computerName;
				
		if (_computerType == ComputerType.PC) {
			computerName = "PC";
		}
		
		return computerName;
	}
	
	public String getComputerTypeName() {
		int idx = _computerType.ordinal();
		String type = COMPUTER_TYPE_NAMES[idx];
		
		if (_computerType == ComputerType.PC) {
			
		}
	
		return type;
	}
	
	private static final String CPU_OS = "cpu os";
	private static final String CPU_IPHONE_OS = "cpu iphone os";
	private static final String VERSION = "version/"; 
	
	protected abstract void detectComputerType();
	
	protected void detectMobile() {
		int i = _userAgent.indexOf("ipad");
		
		if (i != -1) {
			_computerType = ComputerType.IPAD;
			_computerName = "iPad";
		} 
		
		if (_computerType == ComputerType.UNKNOWN) {
			i = _userAgent.indexOf("ipod");
			if (i != -1) {
				_computerType = ComputerType.IPOD;
				_computerName = "iPod";
			}
		}
		
		if (_computerType == ComputerType.UNKNOWN) {
			i = _userAgent.indexOf("iphone");
			if (i != -1) {
				_computerType = ComputerType.IPHONE;
				_computerName = "iPhone";
			}
		}
		
		i = _userAgent.indexOf("nexus s");
		if (i != -1) {
			_computerType = ComputerType.ANDROID_MOBILE;
			_computerName = "Nexus S";
		}
		
		i = _userAgent.indexOf("htc");
		if (i != -1) {
			_computerType = ComputerType.ANDROID_MOBILE;
			_computerName = "HTC";
		}
		
		i = _userAgent.indexOf("series 60");
		if (i != -1) {
			_computerType = ComputerType.SYMBIAN_MOBILE;
		}
		
		i = _userAgent.indexOf("blackberry");
		if (i != -1) {
			_computerType = ComputerType.BB;
		}
		
		i = _userAgent.indexOf("windows mobile");
		if (i != -1) {
			_computerType = ComputerType.WINDOWS_MOBILE;
		}
		
		if (_computerType == ComputerType.UNKNOWN) {
			i = _userAgent.indexOf("android");
			if (i != -1) {
				_computerType = ComputerType.ANDROID_MOBILE;
			}
		}
	}
	
	protected void detectDesktop() {
		int i = _userAgent.indexOf("ppc");
		
		if (i != -1) {
			_computerType = ComputerType.MACINTOSH;
			_computerName = "Power PC";
		} 
		
		if (_computerType == ComputerType.UNKNOWN) {
			i = _userAgent.indexOf("macintosh");
			if (i != -1) {
				_computerType = ComputerType.MACINTOSH;
			}
		}
		
		//last option
		if (_computerType == ComputerType.UNKNOWN) {
			_computerType = ComputerType.PC;
		}
	}
	
	//
	// inner classes 
	//
	private static class OperaMiniBrowserInfo extends UserAgent {

		public OperaMiniBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.OPERA_MINI;
		}
		
		@Override
		protected void detectOS() {
			detectComputerType();
			
			if ((_computerType == ComputerType.IPAD) || (_computerType == ComputerType.IPHONE) || (_computerType == ComputerType.IPOD)) {
				_os = OS.IOS;
			} else if (_computerType == ComputerType.ANDROID_MOBILE) {
				_os = OS.ANDROID;
			} else if (_computerType == ComputerType.BB) {
				_os = OS.BB;
			} else if (_computerType == ComputerType.SYMBIAN_MOBILE) {
				_os = OS.SYMBIAN;
			} else if (_computerType == ComputerType.WINDOWS_MOBILE) {
				_os = OS.WINDOWS;
			}
		}
		
		@Override
		public void detectComputerType() {
			detectMobile();
		}

		@Override
		public String getBrowserName() {
			return "Opera Mini";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			
			if (_userAgent.startsWith("opera")) {
				int i1 = _userAgent.indexOf(OPERA_MINI);
				
				if (i1 < -1) {
					i1 += OPERA_MINI.length();
					int i2 = _userAgent.indexOf(" ", i1+1);
					int i3 = _userAgent.indexOf("/", i1+1);
					
					i2 = (i2 < i3) ? i2 : i3;
					version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
					version = version.trim();
				}
			} 
			
			return version;
		}
		private static final String OPERA_MINI = "opera mini";
	}
	
	private static class OperaBrowserInfo extends UserAgent {

		public OperaBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.OPERA;
		}

		@Override
		public String getBrowserName() {
			return "Opera";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			
			if (_userAgent.startsWith("opera")) {
				int i1 = _userAgent.indexOf(VERSION);
				
				if (i1 > -1) {
					i1 += VERSION.length();
					int i2 = _userAgent.indexOf(" ", i1);
					version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
					version = version.trim();
				}
			} else if (_userAgent.startsWith("mozilla")) {
				int i1 = _userAgent.indexOf(OPERA) + 6;
				
				if (i1 > -1) {
					i1 += OPERA.length();
					int i2 = _userAgent.indexOf(" ", i1+1);
					version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
					version = version.trim();
				}
			} 
			
			return version;
		}
		private static final String OPERA = "opera"; 
		
		@Override
		public void detectComputerType() {
			detectDesktop();
			detectMobile();
		}
	}
	
	private static class ChromeBrowserInfo extends UserAgent {

		public ChromeBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.CHROME;
		}

		@Override
		public String getBrowserName() {
			return "Chrome";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			int i1 = _userAgent.indexOf(CHROME);
			
			if (i1 > -1) {
				i1 += CHROME.length();
				int i2 = _userAgent.indexOf(" ", i1);
				version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
				version.trim();
			}
			
			return version;
		}
		private static final String CHROME = "chrome/"; 
		
		@Override
		public void detectComputerType() {
			detectDesktop();
			detectMobile();
		}
	}
	
	private static abstract class WebKitBrowserInfo extends UserAgent {

		protected WebKitBrowserInfo(String userAgent) {
			super(userAgent);
		}	
	}
	
	private static class ChromeIosBrowserInfo extends WebKitBrowserInfo {

		public ChromeIosBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.CHROME_IOS;
		}
		
		@Override
		protected void detectOS() {
			_os = OS.IOS;
			
			int i1 = _userAgent.indexOf(CPU_OS);
			
			if (i1 > -1) {
				i1 += CPU_OS.length();
			} else {
				i1 = _userAgent.indexOf(CPU_IPHONE_OS);
				
				if (i1 > -1) {
					i1 += CPU_IPHONE_OS.length();
				}
			}
			
			if (i1 > -1) {
				int i2 = _userAgent.indexOf(" ", i1+1);
				String v = _userAgent.substring(i1, i2);
				v = v.replaceAll("_", ".");
				v = v.trim();
				_osVersion = v;
			}
		}
		
		@Override
		public void detectComputerType() {
			detectMobile();
		}
	
		@Override
		public String getBrowserName() {
			return "Chrome iOS";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			int i1 = _userAgent.indexOf(CRIOS);
			
			if (i1 > -1) {
				i1 += CRIOS.length();
				int i2 = _userAgent.indexOf(" ", i1);
				version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
				version = version.trim();
			}
			
			return version;
		}
		private static final String CRIOS = "crios/";
	}
	
	private static class AndroidBrowserInfo extends WebKitBrowserInfo {
		public AndroidBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.ANDROID_BROWSER;
		}
		
		@Override
		protected void detectOS() {
			_os = OS.ANDROID;
			_computerType = ComputerType.ANDROID_MOBILE;
			
			int i1 = _userAgent.indexOf(ANDROID);
			
			if (i1 > -1) {
				i1 += ANDROID.length();
				int i2 = _userAgent.indexOf(";", i1+1);
				String v = _userAgent.substring(i1, i2);
				v = v.replaceAll("_", ".");
				v = v.trim();
				_osVersion = v;
			}
		}
		private static final String ANDROID = "android";
		
		@Override
		public void detectComputerType() {
			detectMobile();
		}

		@Override
		public String getBrowserName() {
			return "Android Browser";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			int i1 = _userAgent.indexOf(VERSION);
			
			if (i1 > -1) {
				i1 += VERSION.length();
				int i2 = _userAgent.indexOf(" ", i1);
				version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
				version.trim();
			}
			
			return version;
		}
	}
	
	private static class SafariBrowserInfo extends WebKitBrowserInfo {

		public SafariBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.SAFARI;
		}
		
		@Override
		protected void detectOS() {
			detectComputerType();
			
			if ((_computerType == ComputerType.IPAD) || (_computerType == ComputerType.IPHONE) || (_computerType == ComputerType.IPOD)) {
				_os = OS.IOS;
				
				int i1 = _userAgent.indexOf(CPU_OS);
				
				if (i1 > -1) {
					i1 += CPU_OS.length();
					int i2 = _userAgent.indexOf(" ", i1+1);
					String v = _userAgent.substring(i1, i2);
					v = v.replaceAll("_", ".");
					v = v.trim();
					_osVersion = v;
				} else {
					i1 = _userAgent.indexOf(CPU_IPHONE_OS);
					
					if (i1 > -1) {
						i1 += CPU_IPHONE_OS.length();
						int i2 = _userAgent.indexOf(" ", i1+1);
						String v = _userAgent.substring(i1, i2);
						v = v.replaceAll("_", ".");
						v = v.trim();
						_osVersion = v;
					}
				}
			} //end if
		} //end detectOS()
		
		@Override
		public void detectComputerType() {
			detectDesktop();
			detectMobile();
		}

		@Override
		public String getBrowserName() {
			return "Safari";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			int i1 = _userAgent.indexOf(VERSION) + 8;
			
			if (i1 > -1) {
				i1 += VERSION.length();
				int i2 = _userAgent.indexOf(" ", i1);
				version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
				version.trim();
			}
			
			return version;
		}
	}
	
	private static class FirefoxBrowserInfo extends UserAgent {

		public FirefoxBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.FIREFOX;
		}

		@Override
		public String getBrowserName() {
			return "Firefox";
		}
		
		@Override
		public void detectComputerType() {
			detectDesktop();
			detectMobile();
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			int i1 = _userAgent.indexOf(FIREFOX);
			
			if (i1 > -1) {
				i1 += FIREFOX.length();
				int i2 = _userAgent.indexOf(" ", i1);
				version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
				version = version.trim();
			}
			
			return version;
		}
		private static final String FIREFOX = "firefox/";
	}
	
	private static class IEBrowserInfo extends UserAgent {

		public IEBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.IE;
		}

		@Override
		public String getBrowserName() {
			return "Internet Explorer";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			int i1 = _userAgent.indexOf(MSIE);
			
			if (i1 > -1) {
				i1 += MSIE.length();
				int i2 = _userAgent.indexOf(";", i1+1);
				i2 = (i2 != -1) ? i2 : _userAgent.indexOf(")", i1+1); 
				version = (i2 == -1) ? _userAgent.substring(i1) : _userAgent.substring(i1, i2);
				version = version.trim();
			}
			
			return version;
		}
		private static final String MSIE = "msie";
		
		@Override
		public void detectComputerType() {
			detectDesktop();
			detectMobile();
		}
	}
	
	private static class UnknownBrowserInfo extends UserAgent {
		public UnknownBrowserInfo(String userAgent) {
			super(userAgent);
			_browser = Browser.IE;
		}

		@Override
		public String getBrowserName() {
			return "Generic Browser";
		}

		@Override
		public String getBrowserVersion() {
			String version = "?"; 
			return version;
		}
		
		@Override
		public void detectComputerType() {
			detectDesktop();
			detectMobile();
		}
	}

	public int getPixelDensity() {
		int pixelDensity = 96;
		
		if (_computerType == ComputerType.IPAD) {
			pixelDensity = 132; 
		} else if (_computerType == ComputerType.IPHONE) {
			pixelDensity = 163; 
		} else if (_computerType == ComputerType.ANDROID_MOBILE) {
			pixelDensity = 200; 
		}
		
		return pixelDensity;
	}

	
	



}
