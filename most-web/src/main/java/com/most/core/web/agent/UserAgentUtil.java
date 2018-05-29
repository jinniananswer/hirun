package com.most.core.web.agent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class UserAgentUtil{

	private boolean _webkiet;
	private boolean _mobile;
	private boolean _android;
	private boolean _androidICS;
	private boolean _ipad;
	private boolean _iphone;
	private boolean _ios7;
	private boolean _windowsPhone;
	private boolean _webos;
	private boolean _touchpad;
	private boolean _playbook;
	private boolean _blackberry10;
	private boolean _blackberry;
	private boolean _chrome;
	private boolean _opera;
	private boolean _fennec;
	private boolean _edge;
	private boolean _ie;
	private boolean _ieTouch;
	private boolean _tizen;
	private boolean _kindle;
	
	private boolean _pad;
	private boolean _phone;
	
	private int _ieVersion;
	private int _ieTrident;
	
	public UserAgentUtil(HttpServletRequest request){
		this(request.getHeader("User-Agent"));
	}
	
	public UserAgentUtil(String userAgent){
		detectUA(userAgent);
	}
	
	void detectUA(String UA){	
		if(UA == null || "".equals(UA))
			return;
		
		_webkiet = Pattern.compile("WebKit(\\/)?([\\d.]+)").matcher(UA).find();
		_mobile = UA.indexOf("Mobile") > -1;
		_android = Pattern.compile("(Android)\\s+([\\d.]+)").matcher(UA).find() || UA.indexOf("Silk-Accelerated") > -1;
		_androidICS = _android && Pattern.compile("(Android)\\s4").matcher(UA).find();
		_ipad = Pattern.compile("(iPad).*OS\\s([\\d_]+)").matcher(UA).find();
		_iphone = !_ipad && Pattern.compile("(iPhone\\sOS)\\s([\\d_]+)").matcher(UA).find();
		_windowsPhone = Pattern.compile("Windows Phone ").matcher(UA).find();
		_chrome = Pattern.compile("Chrome").matcher(UA).find();
		_opera = Pattern.compile("Opera").matcher(UA).find();
		_edge = Pattern.compile("(edge)[ \\/]([\\w.]+)").matcher(UA).find();
		
		//IE浏览器处理
		Pattern p = Pattern.compile("MSIE ([\\w.]+)");
		Matcher m = p.matcher(UA);
		if(m.find()){
			_ie = true;
			_ieVersion = (int)(Double.parseDouble(m.group(1)));
		}else{
			//_ie = Pattern.compile("Trident\\/7").matcher(UA).find();
			p = Pattern.compile("rv:([^\\)]+)\\) like Gecko");
			m = p.matcher(UA);
			if(m.find()){
				_ie = true;
				_ieVersion = (int)(Double.parseDouble(m.group(1)));
			}
		}
		_ieTouch = _ie && Pattern.compile("touch").matcher(UA.toLowerCase()).find();
		
		p = Pattern.compile("(Trident|WadeTrid)\\/([\\d.]+)");
		m = p.matcher(UA);
		if(m.find()){
			_ieTrident = (int)(Double.parseDouble(m.group(2)));
		}
		
		if(_ie){
			
		}
		
		if(_android){
			_pad = !_mobile;
			_phone = _mobile;
		}else if(_ipad || _iphone){
			_pad = _ipad;
			_phone = _iphone;
		}
	}
	
	public boolean webkit(){
		return _webkiet;
	}
	
	public boolean mobile(){
		return _mobile;
	}
	
	public boolean android(){
		return _android;
	}
	
	public boolean androidICS(){
		return _androidICS;
	}
	
	public boolean ipad(){
		return _ipad;
	}
	
	public boolean iphone(){
		return _iphone;
	}
	
	public boolean ios(){
		return _ipad || _iphone;
	}
	
	public boolean chrome(){
		return _chrome;
	}
	
	public boolean opera(){
		return _opera;
	}
	
	public boolean edge(){
		return _edge;
	}
	
	public boolean ie(){
		return _ie;
	}
	
	public int ieVersion(){
		return _ieVersion;
	}
	
	public int ieTrident(){
		return _ieTrident;
	}
	
	public boolean ieTouch(){
		return _ieTouch;
	}
	
	public boolean pad(){
		return _pad;
	}
	
	public boolean phone(){
		return _phone;
	}
	
	public static void main(String[] args){
		//String ua = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; rv:11.0) like Gecko";
		//String ua = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
		String ua = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
		//String ua = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)";
			
		UserAgentUtil util = new UserAgentUtil(ua);
		Pattern p = Pattern.compile("MSIE ([\\w.]+)");
		
		Matcher m = p.matcher(ua);
		if(m.find()){
			System.out.println(m.group(1));
		}
		
		System.out.println(util.ie() + "|" + util.ieVersion());
	}
}