package com.lyft.data.proxyserver;

import com.google.common.io.CharStreams;
import com.lyft.data.proxyserver.wrapper.MultiReadHttpServletRequest;

import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RewritingCreateMultiReadServeletRequest
    implements CreateMultiReadHttpServeletRequest {
  @Override
  public MultiReadHttpServletRequest create(HttpServletRequest request)
      throws IOException {
    SettableInputStreamRequestWrapper rewrittenRequest =
        new SettableInputStreamRequestWrapper(request);
    rewrittenRequest.setInputStream(customizeBody(CharStreams.toString(request.getReader())));
    return new MultiReadHttpServletRequest(rewrittenRequest);
  }

  ServletInputStream customizeBody(String body) {
    return stringToServeletInputStream(body);
  }

  ServletInputStream stringToServeletInputStream(String s) {
    return new ServletInputStream() {
      private int lastIndexRetrieved = -1;
      private ReadListener readListener = null;

      @Override
      public boolean isFinished() {
        return (lastIndexRetrieved == s.length() - 1);
      }

      @Override
      public boolean isReady() {
        return (!isFinished());
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        this.readListener = readListener;
        if (!isFinished()) {
          try {
            readListener.onDataAvailable();
          } catch (IOException e) {
            readListener.onError(e);
          }
        } else {
          try {
            readListener.onAllDataRead();
          } catch (IOException e) {
            readListener.onError(e);
          }
        }
      }

      @Override
      public int read()
          throws IOException {
        int i;
        if (!isFinished()) {
          i = s.charAt(lastIndexRetrieved + 1);
          lastIndexRetrieved++;
          if (isFinished() && (readListener != null)) {
            try {
              readListener.onAllDataRead();
            } catch (IOException ex) {
              readListener.onError(ex);
              throw ex;
            }
          }
          return i;
        } else {
          return -1;
        }
      }
    };
  }

  static class SettableInputStreamRequestWrapper
      extends HttpServletRequestWrapper {
    private ServletInputStream userInputStream;

    public SettableInputStreamRequestWrapper(HttpServletRequest request)
        throws IOException {
      super(request);
    }

    public ServletInputStream getInputStream() {
      return userInputStream;
    }

    public void setInputStream(ServletInputStream inputStream) {
      this.userInputStream = inputStream;
    }
  }
}
