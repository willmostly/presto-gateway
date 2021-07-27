package com.lyft.data.proxyserver;

import javax.servlet.ServletInputStream;

public class SampleRwritingCreateMultiReadServeletRequest
    extends RewritingCreateMultiReadServeletRequest {
  @Override
  ServletInputStream customizeBody(String body) {
    String newBody = body + " /* Mr Body, in the library, with the knife! */";
    return stringToServeletInputStream(newBody);
  }
}
