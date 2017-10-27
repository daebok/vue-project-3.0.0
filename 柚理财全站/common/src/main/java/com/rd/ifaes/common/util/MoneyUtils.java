package com.rd.ifaes.common.util;

import java.math.BigDecimal;
/**
 * 资金工具类 
 * @author lh
 * @desc 提供简单的资金计算和转换操作
 *
 */
public class MoneyUtils {
	
	private static int DEF_DIV_SCALE = 10; // 默认精确的小数位
	
	/**
	 * 将元转换成分
	 * @param money
	 * @return
	 */
	public static String yuanToFen(double money) {
		double fenMoney = mul(money, 100d);
		return String.valueOf((long) fenMoney);
	}
	
	/**
	 * 将元转换成分
	 * @param money
	 * @return
	 */
	public static String yuanToFen(String money) {
		double fenMoney = mul(NumberUtils.toDouble(money), 100d);
		return String.valueOf((long) fenMoney);
	}
	
	/**
	 * 将分转换成元为单位的字符串
	 * @param money
	 * @return
	 */
	public static String fenToYuan(String money) {
		double fenMoney = NumberUtils.toDouble(money);
		return NumberUtils.format2Str(div(fenMoney, 100d));
	}
	
	
	/**
	 * 将分转换成元为单位的字符串
	 * @param money
	 * @return
	 */
	public static String fenToYuan(double money) {
		return NumberUtils.format2Str(div(money, 100));
	}
	
	/*==============================================
	 * 			提供简单的加减乘除运算
	 ==============================================*/
	
	/**
	 * 提供加法运算
	 * @param dbs
	 * @return
	 */
	public static double add(double... dbs){
		BigDecimal b1 = BigDecimal.ZERO;
		for (double d : dbs) {
			b1 = b1.add(BigDecimal.valueOf(d));
		}
		return b1.doubleValue();
	}
	
	/**
	 * 提供加法运算
	 * @param dbs
	 * @return
	 */
	public static double add(String... dbs){
		BigDecimal b1 = BigDecimal.ZERO;
		for (String d : dbs) {
			b1 = b1.add(BigDecimal.valueOf(NumberUtils.toDouble(d)));
		}
		return b1.doubleValue();
	}
	
	/**
	 * 提供减法运算
	 * @param dbs
	 * @return
	 */
	public static double sub(double...dbs){
		BigDecimal bd = BigDecimal.valueOf(dbs[0]);
		for (int i = 1; i < dbs.length; i++) {
			bd = bd.subtract(BigDecimal.valueOf(dbs[i]));
		}
		return bd.doubleValue();
	}
	
	/**
	 * 提供减法运算
	 * @param dbs
	 * @return
	 */
	public static double sub(String... dbs){
		BigDecimal bd = new BigDecimal(dbs[0]);
		for (int i = 1; i < dbs.length; i++) {
			bd = bd.subtract(BigDecimal.valueOf(NumberUtils.toDouble(dbs[i])));
		}
		return bd.doubleValue();
	}
	
	
	/**
	 * 提供乘法运算
	 * @param dbs
	 * @return
	 */
	public static double mul(double... dbs){
		BigDecimal b1 = BigDecimal.ONE;
		for (double db : dbs) {
			b1 = b1.multiply(BigDecimal.valueOf(db));
		}
		return b1.doubleValue();
	}
	
	/**
	 * 提供乘法运算
	 * @param dbs
	 * @return
	 */
	public static double mul(String... dbs){
		BigDecimal b1 = BigDecimal.ONE;
		for (String db : dbs) {
			b1 = b1.multiply(BigDecimal.valueOf(NumberUtils.toDouble(db)));
		}
		return b1.doubleValue();
	}
	
	/**
	 * 提供除法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}
	
	/**
	 * 提供除法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double div(double v1, double v2, int scale) {
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	 static final String big = "零壹贰叁肆伍陆柒捌玖";     //大写  
	 static final String[] units = {"仟佰拾", "角分"};    //单位  
	  
	      
	    /** 
	     * 双精度浮点数转换成字符串 
	     * 注： 
	     * 1、如果直接用String.toString(double d)方法，超大数额会出现科学计数法的字符串； 
	     * 2、如果整数部分超过15位数，低位部分可能出现误差，所以不支持超过15位整数的数值， 
	     *   一般数据库设计金额字段时都不会超过15位整数，如oracle用Number(18,3)的类型表示，整数部分最多15位，小数点后保留3位有效数字。 
	     */  
	    public static String getDecimalStr(double d){  
	        //设置小数点后的精度，保留两位  
	        String str = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).toString();  
	        //如果结果是整数，则去掉尾巴  
	        if(str.endsWith(".00")){  
	            str = str.replace(".00", "");  
	        }  
	        return str;  
	    }  
	      
	      
	    /** 
	     * 金额转换成大写字 
	     * 1、double数值转换成数值字符串 
	     * 2、处理整数部分： 
	     *   填充到16位，不足16位则前面补'0'，然后右起分成四组，每组根据"x仟x佰x拾x"的规则转换成大写，若该组为"0000"则结果是"零"； 
	     *   对这四组结果从高位到低位拼接起来，规则：[组4]万[组3]亿[组2]万[组1]圆。 
	     * 3、处理小数部分（不多说） 
	     */  
	    public static String transform(BigDecimal moneyNum){
	    	if(BigDecimal.ZERO.compareTo(moneyNum) == 0){
	    		return "零圆整";
	    	}
	    	String moneyNumStr = moneyNum.stripTrailingZeros().toPlainString();//去除多余的零
	        String[] parts = moneyNumStr.split("\\."); //区别整数、小数部分  
	        String result = "";  
	          
	        //处理整数部分  
	        int length = parts[0].length(); //整数部分的位数  
	        if(length>15){  
	            return "金额太大,不能处理整数部分超过15位的金额！";  
	        }  
	        String intPart = parts[0];  
	          
	        //填充到16位，因为是分4组，每组4个数字  
	        while(intPart.length()<16){  
	            intPart = '0' + intPart;  
	        }  
	        //共分四组,右起四位一组,例如：0001,2003,0030,3400  
	        String[] groups = new String[4];   
	        for(int i=0; i < groups.length; i++){  
	            int start = intPart.length()-(i+1)*4;   //开始位置  
	            int end = intPart.length()-i*4;         //结束位置  
	            groups[i] = intPart.substring(start, end);  
	            groups[i] = transformGroup(groups[i]);  //当前组的四位数字转换成大写  
	        }  
	          
	        //对这四组结果从高位到低位拼接起来，规则：[组4]万[组3]亿[组2]万[组1]圆  
	        for(int i=groups.length-1; i>=0; i--){  
	            if(i==3){   //第四组：万亿级  
	                if(!"零".equals(groups[i])){  
	                    result += groups[i] + "万";  
	                }  
	            }else if(i==2){ //第三组：亿级  
	                if(!"零".equals(groups[i])){  
	                    result += groups[i] + "亿";  
	                }else{  
	                    if(result.length()>0){  
	                        result += "亿";  
	                    }  
	                }  
	            }else if(i==1){ //第二组：万级  
	                if(!"零".equals(groups[i])){  
	                    result += groups[i] + "万";  
	                }else if(!groups[i].startsWith("零")){  
	                    result += groups[i];  
	                }   
	            }else{  //第一组：千级  
	                if(!"零".equals(groups[i]) || result.length()==0){  
	                    result += groups[i];  
	                }  
	                result += "圆";  
	            }  
	        }  
	        if(!"零圆".equals(result) && result.startsWith("零")){  
	            result = result.substring(1, result.length()); //最前面的可能出现的“零”去掉  
	        }  
	        //处理小数部分  
	        if(parts.length==2){  
	            String decimalPart = parts[1];  //小数部分  
	            //小数部门只处理2位小数
	            if(decimalPart.length() > 2){
	            	decimalPart = decimalPart.substring(0, 2);
	            }
	            for(int i=0; i < decimalPart.length();i++){  
	                int num = Integer.valueOf(decimalPart.charAt(i) + "");  //提取数字，左起  
	                result += big.charAt(num) + "" + units[1].charAt(i);    //数字变大写加上单位  
	            }  
	            result = result.replace("零角", "零"); //去掉"零角"的"角"  
	            result = result.replace("零分", "");  //去掉"零分"  
	        }else{  
	            result += "整";  //没有小数部分，则加上“整”  
	        }  
	        return result;  
	    }  
	      
	    /** 
	     * 处理整数部分的组，右起每四位是一组 
	     * @param group 四位数字字符串 
	     */  
	    public static String transformGroup(String group){  
	        String result = "";  
	        int length = group.length();  
	        for(int i=0; i < length; i++){  
	            int digit = Integer.valueOf(group.charAt(i)+"");    //单个数字，左起  
	            String unit = "";   //单位  
	            if(i!=length-1){  
	                unit = units[0].charAt(i) + "";   
	            }  
	            result += big.charAt(digit) + unit; //数字变大写加上单位  
	        }  
	        result = result.replace("零仟", "零");  
	        result = result.replace("零佰", "零");  
	        result = result.replace("零拾", "零");  
	        while(result.contains("零零")){  
	            result = result.replace("零零", "零"); //如果有“零零”则变成一个“零”  
	        }  
	        if(!"零".equals(result) && result.endsWith("零")){  
	            result = result.substring(0, result.length()-1); //最未尾的可能出现的“零”去掉  
	        }  
	        return result;  
	    }  
}
