package com.tw.customer_tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

public class InputTextTag extends BodyTagSupport {

    private String id;
    private String name;
    private String value;
    private String onblur;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = getJspWriter();

        String inputTextString = buildInputText();
        try {
            out.println(inputTextString );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    private String buildInputText() {
        StringBuilder builder = new StringBuilder();
        builder.append("<input type=\"text\" ")
                .append(" id=\""+ id + "\" ")
                .append(" name=\""+ name + "\" ");

        if (null != value) {
            builder.append(" value=\""+ value + "\" ");
        }

        if (null != onblur) {
            builder.append("onblur=\"" + onblur + "\" ");
        }

        builder.append(">");
        return builder.toString();
    }

    JspWriter getJspWriter() {
        return pageContext.getOut();

    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setOnblur(String onblue) {
        this.onblur = onblue;
    }
}
