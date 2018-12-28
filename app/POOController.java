package app;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import java.io.OutputStream;
// import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
// import java.io.ByteArrayOutputStream;

import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import static utils.JsonUtils.jsonMake;
import static utils.JsonUtils.jsonArray;
import static utils.JsonUtils.jsonField;


public abstract class POOController implements HttpHandler {
  private HttpExchange conn;
  private HashMap<String, String> parameters;

  @Override
  public void handle(HttpExchange t) throws IOException {
    this.conn = t;
    this.parameters = null;
    ArrayList<String> parts = getPath(0);
    if (parts.size() >= 1) {
      String method = this.routes().get(parts.get(0));
      if (method != null) {
        try {
          // System.out.println(this.getClass().getMethod(method, new Class[]{String[].class}));
          if (parts.size() > 1) {
            parts.remove(0);
            String[] args = parts.toArray(new String[0]);
            this.getClass().getMethod(method, new Class[]{String[].class}).invoke(this, new Object[]{args});
            return;
          }
          this.getClass().getMethod(method).invoke(this);
        } catch (Exception ex) {
          System.out.println(ex);
        }
      }
    }
  }

  // alterado baseado em https://stackoverflow.com/questions/42864946/java-http-server-response-in-utf8
  public void status(int status, String response) {
    try {
      conn.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
      conn.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
      byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
      conn.sendResponseHeaders(status, bytes.length);

      OutputStream os = conn.getResponseBody();
      os.write(bytes);
      os.close();
    } catch (IOException ex) {
      System.out.println(ex);
    }
  }

  public ArrayList<String> getPath(int ignoreDepth) {
    String path = conn.getRequestURI().getPath();
    ArrayList<String> parts = new ArrayList<String>(Arrays.asList(path.split("/")));
      parts.remove(0); // remove a parte vazia
      parts.remove(0); // remove a primeira parte do path
      for (int i = 0; i < ignoreDepth; i++) {
        parts.remove(0);
      }
      return parts;
    }

    // adapted from original https://gist.github.com/williamsandytoes/82437e08c4bdb1bc37130a259ee96822
    // other reference: https://stackoverflow.com/questions/4964640/reading-inputstream-as-utf-8
    // and this: https://stackoverflow.com/questions/6138127/how-to-do-url-decoding-in-java
    public HashMap<String, String> getParameters() {
      if (parameters != null) {
        return parameters;
      }
      HashMap<String, String> parameters = new HashMap<>();
      // InputStream inputStream = conn.getRequestBody();
      try {
        InputStreamReader inputStream = new InputStreamReader(conn.getRequestBody(),"UTF-8");
        BufferedReader reader = new BufferedReader(inputStream);
        // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // byte[] buffer = new byte[2048];
        StringBuilder buffer = new StringBuilder();
        int read = 0;

        while ((read = reader.read()) != -1) {
          // byteArrayOutputStream.write(buffer, 0, read);
          buffer.append((char) read);
        }

        String[] keyValuePairs = buffer.toString().split("&");
        for (String keyValuePair : keyValuePairs) {
          String[] keyValue = keyValuePair.split("=");
          if (keyValue.length != 2) {
            continue;
          }
          parameters.put(keyValue[0], URLDecoder.decode(keyValue[1], "UTF-8"));
        }
      } catch (IOException ex) {
        System.out.println(ex);
      } catch (Exception ex) {
        System.out.println(ex);
      }
      this.parameters = parameters;
      return parameters;
    }

    public String getParameter(String name) {
      String value = getParameters().get(name);
      if (value == null) {
        return "";
      }
      return value;
    }
  
    public void ok(String response) {
      System.out.println(response);
      status(200, response);
    }

    public void ok() {
      ok("");
    }

    public void ok(String key, Object object) {
      String response = jsonMake(
        jsonField(key, object)
      );
      ok(response);
    }
    public void ok(String key, Object object , String key2, Object object2) {
      String response = jsonMake(
        jsonField(key, object),
        jsonField(key2, object2)
      );
      ok(response);
    }
   

    public abstract HashMap<String, String> routes  ();
  }