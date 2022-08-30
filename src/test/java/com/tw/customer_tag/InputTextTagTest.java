package com.tw.customer_tag;

import org.junit.Test;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class InputTextTagTest {

    @Test
    public void should_write_input_text() throws JspException, IOException {
        // given
        JspWriter jspWriter = mock(JspWriter.class);
        InputTextTag inputTextTag = spy(InputTextTag.class);
        inputTextTag.setId("id1");
        inputTextTag.setName("name1");
        inputTextTag.setValue("val1");
        doReturn(jspWriter).when(inputTextTag).getJspWriter();

        // when
        inputTextTag.doStartTag();

        // then
        verify(jspWriter).println("<input type=\"text\"  id=\"id1\"  name=\"name1\"  value=\"val1\" >");
    }

    @Test
    public void should_write_input_text_without_value() throws JspException, IOException {
        // given
        JspWriter jspWriter = mock(JspWriter.class);
        InputTextTag inputTextTag = spy(InputTextTag.class);
        inputTextTag.setId("id1");
        inputTextTag.setName("name1");
        doReturn(jspWriter).when(inputTextTag).getJspWriter();

        // when
        inputTextTag.doStartTag();

        // then
        verify(jspWriter).println("<input type=\"text\"  id=\"id1\"  name=\"name1\" >");
    }

    @Test
    public void should_write_onblur() throws JspException,IOException {
        // given
        JspWriter jspWriter = mock(JspWriter.class);
        InputTextTag inputTextTag = spy(InputTextTag.class);
        inputTextTag.setId("id1");
        inputTextTag.setName("name1");
        inputTextTag.setOnblur("onblur()");
        doReturn(jspWriter).when(inputTextTag).getJspWriter();

        // when
        inputTextTag.doStartTag();

        // then
        verify(jspWriter).println("<input type=\"text\"  id=\"id1\"  name=\"name1\" onblur=\"onblur()\" >");
    }


}