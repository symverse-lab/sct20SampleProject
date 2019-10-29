package com.symverse.common.model;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.utils.Numeric;



/**
 * @author 재현
 *
 */
@Resource(name="commUtil")
public class CommUtil
{
	
    static final Logger logger = LoggerFactory.getLogger(CommUtil.class);

	/**
     *<pre>
     *Object NVL
     *</pre>
     * @param src
     * @param target
     * @return
     */
    public static Object nvl(Object src, Object target) {
        if(src == null) return target;
        else return src;
    }

    /**
     *<pre>
     *String NVL
     *</pre>
     * @param src
     * @param target
     * @return
     */
    public static String nvl(String src, String target) {
        if(src == null) return target;
        else return src;
    }
    
    
    public static String objToString(Object obj) {
    	String val = "";
    	if(obj == null || obj.equals("") ) {
			val = "";
		} else if(obj instanceof Number) {
			val = ((Number) obj).toString();
		} else if(obj instanceof String) {
			val = (String)obj;
		} else if(obj instanceof byte[]) {
			val = new String((byte[])obj);
		} else if(obj instanceof char[]) {
			val = new String((char[])obj);
		} else if(obj instanceof Date) {
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			val = formatter.format(obj);
		}
    	return val;
    }
    

    @Deprecated
    public static String messageRedirector(String msg, String url) {
        StringBuffer sb = new StringBuffer("");

        sb.append("<html>     \n");
        //sb.append("<head>     \n");
        sb.append("<script language=javascript>     \n");
        if(!"".equals(CommUtil.nvl(msg, ""))) {
        	sb.append("alert('").append(msg).append("');     \n");
        }
        sb.append("location.href = '").append(url).append("';     \n");
        sb.append("</script>     \n");
        //sb.append("</head>     \n");
        sb.append("</html>     \n");

        return sb.toString();
    }

    @Deprecated
    public static String messageRedirector(String msg, String url, Map<String, String> params) {
        StringBuffer sb = new StringBuffer("");

        sb.append("<html>     \n");
        sb.append("<base target='_self'>     \n");
        sb.append("<head>     \n");
        sb.append("<script language=javascript>     \n");
        sb.append("function onload() { \n");
        if(!CommUtil.nvl(msg, "").equals("")) {
        	sb.append("    alert('").append(msg).append("');     \n");
        }
        sb.append("    document.getElementById('frm').submit();     \n");
        sb.append("} \n");
        sb.append("</script>     \n");
        sb.append("</head>     \n");
        sb.append("<body onload='onload();'>     \n");
        sb.append("<form id='frm' name='frm' method='post' action='").append(url).append("'>");
        
        if(params != null) {
        	Iterator<Entry<String, String>> its = params.entrySet().iterator();
        	while(its != null && its.hasNext()) {
        		Entry<String, String> entry = (Entry<String, String>)its.next();
        		sb.append("    <input type='hidden' id='").append(entry.getKey()).append("' name='").append(entry.getKey()).append("' value='").append(entry.getValue()).append("' />");
        	}
        }
        
        
        
        for(int i = 0; params != null && i < params.size(); i++) {
        }
        sb.append("</form>     \n");
        sb.append("</body>     \n");
        sb.append("</html>     \n");

        return sb.toString();
    }

    @Deprecated
    public static String messagePopCloser(String msg) {
        StringBuffer sb = new StringBuffer("");

        sb.append("<html>     \n");
        //sb.append("<head>     \n");
        sb.append("<script language=javascript>     \n");
        sb.append("window.focus(); \n");
        sb.append("var opener2 = window.dialogArguments;     \n");
        sb.append("alert('" + msg + "');     \n");
        //sb.append("alert('").append(msg).append("');     \n");
        //sb.append("window.opener.openMenu('/pay/index.jsp');     \n");
        sb.append("window.close();     \n");
        sb.append("</script>     \n");
        //sb.append("</head>     \n");
        sb.append("</html>     \n");

        return sb.toString();
    }


    @Deprecated
    public static String messageHistoryBack(String msg) {
        StringBuffer sb = new StringBuffer("");

        sb.append("<html>     \n");
        //sb.append("<head>     \n");
        sb.append("<script language=javascript>     \n");
        sb.append("alert('").append(msg).append("');     \n");
        sb.append("history.back();     \n");
        sb.append("</script>     \n");
        //sb.append("</head>     \n");
        sb.append("</html>     \n");

        return sb.toString();
    }

    @Deprecated
    public static String messageHistoryGo(String msg, int count) {
        StringBuffer sb = new StringBuffer("");

        sb.append("<html>     \n");
        //sb.append("<head>     \n");
        sb.append("<script language=javascript>     \n");
        sb.append("alert('").append(msg).append("');     \n");
        sb.append("history.go(").append(new Integer(count).toString()).append(");     \n");
        sb.append("</script>     \n");
        //sb.append("</head>     \n");
        sb.append("</html>     \n");

        return sb.toString();
    }
    
    /**
     * getAddComma                                        <BR>
     * 컴마 추가<BR>
     * @param    args          String 컴마추가할 문자열
     * @return   Str           String 컴마추가된 문자열
     */
     public static String getAddComma(String args) {
    	 if(args == null) args = "";
         args.trim();
         String symbol = "";

         if(args.equals(""))
          return args;

         symbol = args.substring(0,1);

         if(symbol.equals("-")) {//마이너스일때
             String va = "";
             if(args.length() <= 4)
                 return args;
             va = args.substring(1);
             String value = "";
             int i = 1;
             int k = va.length();
             for(int j=va.length(); j > 0; j--) {
                 if(i%3 == 0 && i != 1 && i !=k ) {
                     value = ","+va.charAt(j-1)+value;
                 }else {
                     value = va.charAt(j-1)+value;
                 }
                     i++;
             }
             return  (symbol.concat(value));
           }//마이너스일때
          else {

         if(args.length() <= 3)
             return args;
         String val = args;
         String value = "";
         int i = 1;
         int k = val.length();
         for(int j=val.length(); j > 0; j--) {
             if(i%3 == 0 && i != 1 && i !=k ) {
                 value = ","+val.charAt(j-1)+value;
             }else {
                 value = val.charAt(j-1)+value;
             }
                 i++;
         }
         return  value;
       }
     }
     
     public static String getAddComma(long args) {
    	 return CommUtil.getAddComma(new Long(args).toString());
     }

     public static String getAddComma(Long args) {
    	 return CommUtil.getAddComma(args.toString());
     }

     public static String getAddComma(int args) {
    	 return CommUtil.getAddComma(new Integer(args).toString());
     }

     public static String getAddComma(Integer args) {
    	 return CommUtil.getAddComma(args.toString());
     }

     /**
      * 현재(한국기준) 시간정보를 얻는다.<BR>
      * (예) 입력파리미터인 format string에 "yyyyMMddhh"를 셋팅하면 1998121011과 같이 Return.<BR>
      * (예) format string에 "yyyyMMddHHmmss"를 셋팅하면 19990114232121과 같이
      *      0~23시간 타입으로 Return. <BR>
      *      String CurrentDate = CommUtil.getKST("yyyyMMddHHmm");<BR>
      * @param    format      얻고자하는 현재시간의 Type
      * @return   str         현재 한국 시간.
      */
      public static String getKST(String format)
      {
          //1hour(ms) = 60s * 60m * 1000ms
          int millisPerHour = 60 * 60 * 1000;
          SimpleDateFormat fmt= new SimpleDateFormat(format);

          SimpleTimeZone timeZone = new SimpleTimeZone(9*millisPerHour,"KST");
          fmt.setTimeZone(timeZone);

          long time=System.currentTimeMillis();
          String str=fmt.format(new java.util.Date(time));
          return str;
      }

      /*
       * 세션값 가죠오기
       */
      public static String getSession_Val(HttpServletRequest req,String se_value)
      {
            HttpSession session     = req.getSession(false);
            String send_value ="";

            if(se_value ==null || se_value.equals("")){
            
                send_value= "";
            
            }else{

                send_value =(String)session.getAttribute(se_value);

                if(send_value ==null || send_value.equals("")){
                    
                     send_value= "";

                    if(se_value.equals("SCL_REG_NO") && send_value.length() != 13){
                     send_value= "";
                    }

                }
            }

            return send_value;
      }
    
      /**
       * 클라이언트 IP를 얻는다.<BR>
       * @param    format      req 객체
       * @return   ip         접속 IP
       */
       public static String getClientIP(HttpServletRequest req)
       {
           String clientIP = "";

           try
           {
               Enumeration<?> em = req.getHeaderNames(); 

               while (em.hasMoreElements()) 
               { 
                   String headerName = (String)em.nextElement();

                   if(headerName.equalsIgnoreCase("x-forwarded-for") == true)
                   {
                       clientIP = req.getHeader(headerName);
                   } 
               }

               if(clientIP.length()==0)
                   clientIP  = req.getRemoteAddr();

               return clientIP;
           }
           catch(Exception e)
           {
               return clientIP;
           }
       }
    
    /***
     * 내꺼 끝
     ***/
    
      
      
     /**
     *<pre>
     *사업자등록번호 양식 만들어주기
     *</pre>
     * @param bizNo
     * @return
     */
     public static String makeBizNoFormat(String bizNo) {
    	 if(CommUtil.nvl(bizNo, "").length() == 10) {
    		 return bizNo.substring(0, 3) + "-" + bizNo.substring(3, 5) + "-" + bizNo.substring(5, 10);
    	 } else {
    		 return bizNo;
    	 }
     }
    
     
     /**
     *<pre>
     *법인등록번호 양식 만들어주기
     *</pre>
     * @param bizNo
     * @return
     */
	public static String makeCorpNoFormat(String coNo) {
		if (CommUtil.nvl(coNo, "").length() == 13) {
			return coNo.substring(0, 6) + "-" + coNo.substring(6, 13);
		} else {
			return coNo;
		}
	}
	
	public static String convertToHTML(String s) {
		return nvl(s, "").replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&qout;");
	}
    
	public static String convertFromHTML(String s) {
		return nvl(s, "").replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&qout;");
	}
    
	
	/**
	 * <pre>
	 * 파라미터 필터 (스크립트 차단)
	 * 
	 */
    public static String filterParam(String parameterValue){
    	String param = CommUtil.nvl(parameterValue, "");
    	Matcher matcher = Pattern.compile("script").matcher(param);
    	if(matcher.find()){
    		return matcher.replaceAll("");
    	}
    	param = param.replaceAll("\'","\"");
    	return param;
    }

	/**
	 *<pre>
	 *휴대폰번호 형식이 맞는지 검사
	 *</pre>
	 * @param string
	 * @return
	 */
	public static boolean isValdMphnNo(String mphnNo) {
		return Pattern.compile("^01[016789][-| ]?\\d{3,4}[-| ]?\\d{4}?").matcher(mphnNo).matches();
	}
	
	
	
	/**
	 *<pre>
	 *여러가지 객체를 int로 
	 *</pre>
	 * @param obj
	 * @return
	 */
	public static int makeInt(Object obj) {
		int val = 0;
		
		try {
			if(obj instanceof Number) {
				val = ((Number) obj).intValue();
			} else if(obj instanceof String) {
				val = Integer.parseInt((String)obj);
			} else if(obj instanceof byte[]) {
				val = Integer.parseInt(new String((byte[])obj));
			} else if(obj instanceof char[]) {
				val = Integer.parseInt(new String((char[])obj));
			}
		} catch (Exception e) {
			return 0;
		}
		
		return val;
	}
	
	
	/**
	 *<pre>
	 *여러가지 객체를 long 으로
	 *</pre>
	 * @param obj
	 * @return
	 */
	public static long makeLong(Object obj) {
		long val = 0;
		
		try {
			if(obj instanceof Number) {
				val = ((Number) obj).longValue();
			} else if(obj instanceof String) {
				val = Long.parseLong((String)obj);
			} else if(obj instanceof byte[]) {
				val = Long.parseLong(new String((byte[])obj));
			} else if(obj instanceof char[]) {
				val = Long.parseLong(new String((char[])obj));
			}
		} catch (Exception e) {
			return 0;
		}
		
		return val;
	}
	
	/**
	 *<pre>
	 *여러가지 객체를 String 으로
	 *</pre>
	 * @param obj
	 * @return
	 */
	public static String makeString(Object obj) {
		String val = "";
		
		try {
			if(obj instanceof Number) {
				val = ((Number) obj).toString();
			} else if(obj instanceof String) {
				return (String)obj;
			} else if(obj instanceof byte[]) {
				val = new String((byte[])obj);
			} else if(obj instanceof char[]) {
				val = new String((char[])obj);
			}
		} catch (Exception e) {
			return "";
		}
		
		return val;
	}
	
	

	/**
	 * 2013-11-21	
	 * <pre> 
	 * 날자형식인지 체크 YYYYMMDD 형식
	 * </pre>
	 * @param dt
	 * @return boolean
	 */
	public static boolean checkDt(String dt)
	{

		try{
			
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setLenient(true);			
			java.util.Date date = df.parse(dt);
			
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 2013-11-21	
	 * <pre> 
	 * 시간형식인지체크 시간이라면  입력된시간을, 아니라면 000000 리턴
	 * </pre>
	 * @param dt
	 * @return tm
	 */
	public static String checkTm(String tm)
	{
		if(tm.length() != 6)
			return "000000";

		try{
			
			DateFormat df = new SimpleDateFormat("HHmmss");
			df.setLenient(false);			
			java.util.Date date = df.parse(tm);
			
		}catch (Exception e) {
			return "000000";
		}
		return tm;
	}
	
	 /**
	 * <pre>
	 * 휴대폰,계좌번호 * 처리
	 * 
	 */
    public static String setAsteria(String value ,String  cat){
    	String asteriaValue = "";
    	String setAsteriaChar= "****";
    	
    	
    	
    	if(value == null || value.equals("") )
    	{
    		return asteriaValue = CommUtil.nvl(value,"");
    	}
    	
    	if("ACCT_NO".equals(cat) ){
    		if(value instanceof String){
    				if(value.length() < 4){
    					return value;
    				}else if(value.length() >= 7){
    					return asteriaValue = value.substring(0,3)+"****"+value.substring(7);
	    			}else{
	    				String temp ="";
	    				for(int j = 0 ; j < value.length() - 3 ; j++ ){
							temp = temp + "*";
						}
	    				return asteriaValue = value.substring(0,3) + temp;
	    			}
    		}else{
    			return asteriaValue;
    		}
    	}else if("MPHN".equals(cat)){
			if(value instanceof String){
				if(value.length() >= 7  && value.indexOf("-") ==  -1 ){
					return asteriaValue = value.substring(0,3)+"****"+value.substring(7);
				}else{
					if(value.indexOf("-") >  -1 ){
						String[] arrs = value.split("-");
						String temp = "";
						if(arrs.length >  2){
							for(int i = 0 ; arrs!=null && i < arrs.length ; i++  ){
								if(i == 1){
									for(int j = 0 ; j < arrs[1].length()  ; j++ ){
										temp = temp + "*";
									}
								}else{
									temp = temp + arrs[i];
								}
								if(arrs.length -1 != i){
									temp = temp + "-";
								}
							}
							return temp;
						}else {
							if(value.length() > 7){
								return asteriaValue = value.substring(0,3)+"****"+value.substring(7);
							}else{
								return value;
							}
						}
					}else{
						if(value.length() >= 3){
							String temp ="";
							for(int j = 0 ; j < value.length() - 3 ; j++ ){
								temp = temp + "*";
							}
							return asteriaValue = value.substring(0,3) + temp;
						}else{
							return value;
						}
					}
    			}
    		}else{
    			return asteriaValue;
    		}
    	}
    	return asteriaValue;

    }
	

    /**
	 * 시작 위치를 뒤에서 부터 시작
	 * @param startFromEnd
	 * @return
	 */
	public static String substringFromEnd(int startNum, String target) {
		return target.substring(target.length() - startNum, target.length());
	}
    
	public static String preHexRemove(String param) {
		return param.replaceAll("0x","");
	}
	
	
	public static String preHex(String param) {
		return "0x"+param;
	}
	
	// hexvalue 숫자를 10진수 숫자 string 으로 바꾼다.
	public static String hexValueToDecimalNumberString (String parma) {
		long v = Long.parseLong(Numeric.cleanHexPrefix( parma ), 16);
		return String.valueOf(v);
	}
	
    
	public static String strintDecimaltoHexValueConvert(String param) {
   	    String hex = Integer.toHexString(Integer.parseInt(param));
	    System.out.println("return strintDecimaltoHexValueConvert"+preHex(hex));
	    return preHex(hex);
	}

	
	private static char[] VALID_CHARACTERS =
		    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456879".toCharArray();
			// cs = cryptographically secure
public static String csRandomAlphaNumericString(int numChars) {
    SecureRandom srand = new SecureRandom();
    Random rand = new Random();
    char[] buff = new char[numChars];

    for (int i = 0; i < numChars; ++i) {
      // reseed rand once you've used up all available entropy bits
      if ((i % 10) == 0) {
          rand.setSeed(srand.nextLong()); // 64 bits of random!
      }
      buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
    }
    return new String(buff);
}


    
} 