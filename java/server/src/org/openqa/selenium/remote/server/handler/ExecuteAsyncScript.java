// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.internal.ArgumentConverter;
import org.openqa.selenium.remote.server.handler.internal.ResultConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecuteAsyncScript extends WebDriverHandler<Object> {
  private volatile String script;
  private final List<Object> args = new ArrayList<>();

  public ExecuteAsyncScript(Session session) {
    super(session);
  }

  @Override
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    super.setJsonParameters(allParameters);
    script = (String) allParameters.get("script");

    List<?> params = (List<?>) allParameters.get("args");

    params.stream().map(new ArgumentConverter(getKnownElements())).forEach(args::add);
  }

  @Override
  public Object call() {

    Object value;
    if (args.size() > 0) {
      value = ((JavascriptExecutor) getDriver()).executeAsyncScript(script, args.toArray());
    } else {
      value = ((JavascriptExecutor) getDriver()).executeAsyncScript(script);
    }

    return new ResultConverter(getKnownElements()).apply(value);
  }

  @Override
  public String toString() {
    return String.format("[execute async script: %s, %s]", script, args);
  }
}
