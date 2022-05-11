package com.tw.customer_tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class HelloTag extends BodyTagSupport {

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = getJspWriter();

        try {
            out.println("Hello Custom Tag!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    JspWriter getJspWriter() {
        return pageContext.getOut();
    }
}
