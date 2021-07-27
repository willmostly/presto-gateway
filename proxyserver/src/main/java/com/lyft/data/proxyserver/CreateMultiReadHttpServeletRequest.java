package com.lyft.data.proxyserver;

import com.lyft.data.proxyserver.wrapper.MultiReadHttpServletRequest;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public interface CreateMultiReadHttpServeletRequest {
  public MultiReadHttpServletRequest create(HttpServletRequest request)
          throws IOException;
}
