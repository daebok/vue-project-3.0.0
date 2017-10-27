package com.rd.ifaes.shiro.freemarker;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;


/**
 * JSP tag that renders the tag body if the current user <em>is not</em> known to the system, either because they
 * haven't logged in yet, or because they have no 'RememberMe' identity.
 *
 * <p>The logically opposite tag of this one is the {@link UserTag}.  Please read that class's JavaDoc as it explains
 * more about the differences between Authenticated/Unauthenticated and User/Guest semantic differences.
 *
 * <p>Equivalent to {@link org.apache.shiro.web.tags.GuestTag}</p>
 * example :<@shiro.guest>Hello guest!</@shiro.guest>
 * @since 0.9
 */
public class GuestTag extends SecureTag {
    private static final Logger log = LoggerFactory.getLogger("GuestTag");

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        if (getSubject() == null || getSubject().getPrincipal() == null) {
                log.debug("Subject does not exist or does not have a known identity (aka 'principal').  " +
                        "Tag body will be evaluated.");

            renderBody(env, body);
        } else {
                log.debug("Subject exists or has a known identity (aka 'principal').  " +
                        "Tag body will not be evaluated.");
        }
    }
}
