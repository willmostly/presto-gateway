package com.lyft.data.proxyserver;

import com.lyft.data.proxyserver.wrapper.MultiReadHttpServletRequest;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public class CopyCreateMultiReadServeletRequest
    implements CreateMultiReadHttpServeletRequest {
  @Override
  public MultiReadHttpServletRequest create(HttpServletRequest request)
      throws IOException {
    return new MultiReadHttpServletRequest(request);
  }
}
