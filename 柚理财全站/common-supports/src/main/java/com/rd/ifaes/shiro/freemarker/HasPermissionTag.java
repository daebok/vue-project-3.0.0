package com.rd.ifaes.shiro.freemarker;

/**
 * <p>Equivalent to {@link org.apache.shiro.web.tags.HasPermissionTag}</p>
 *
 * @since 0.1
 */
public class HasPermissionTag extends PermissionTag {
	private static final String PERMISSION_NAMES_DELIMETER = ",";
	
    protected boolean showTagBody(String p) {
    	if(p.contains(PERMISSION_NAMES_DELIMETER)){
    		String[] ps = p.split(PERMISSION_NAMES_DELIMETER);
    		for (String pm : ps) {
    			if(isPermitted(pm)){
    				return true;
    			}
			}
    		return false;
    	}else{
    		return isPermitted(p);    		
    	}
    }
}
