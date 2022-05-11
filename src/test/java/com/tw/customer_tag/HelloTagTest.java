package com.tw.customer_tag;

import org.junit.Test;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class HelloTagTest {

    @Test
    public void should_write_to_outstream() throws JspException, IOException {
        // given
        JspWriter jspWriter = mock(JspWriter.class);
        HelloTag helloTag = spy(HelloTag.class);
        doReturn(jspWriter).when(helloTag).getJspWriter();

        // when
        helloTag.doStartTag();

        // then
        verify(jspWriter).println("Hello Custom Tag!");
    }
}