package ydtak.chatgpt.platform;

import com.google.protobuf.util.JsonFormat;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Properties;

/** A minimal interface for interacting with OpenAI's ChatGPT rest API. */
public class ChatGptApi {

  /** Returns an instance of {@link ChatGptApi}. */
  public static ChatGptApi getInstance(String configPropertiesFilepath) {
    return new ChatGptApi(readApiKey(configPropertiesFilepath));
  }

  private static final String CHATGPT_API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String OPENAI_API_KEY_PROPERTY = "openai.apikey";

  private final String apiKey;

  private ChatGptApi(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   * Sends a request to the ChatGPT API. See https://platform.openai.com/docs/api-reference/chat.
   */
  public ChatGptResponse sendChatGptRequest(ChatGptRequest request) {
    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection) URI.create(CHATGPT_API_URL).toURL().openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Authorization", "Bearer " + apiKey);
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setDoOutput(true);

      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
      outputStreamWriter.write(JsonFormat.printer().print(request));
      outputStreamWriter.flush();
      outputStreamWriter.close();

      ChatGptResponse.Builder chatGptResponse = ChatGptResponse.newBuilder();
      String response = readInputStream(connection.getInputStream());
      JsonFormat.parser().merge(response, chatGptResponse);
      return chatGptResponse.build();
    } catch (IOException e) {
      if (connection != null) {
        System.err.println(readInputStream(connection.getErrorStream()));
      }
      throw new IllegalStateException("Failed to connect to OpenAI.", e);
    }
  }

  /** Returns a string for the contents of the given {@code inputStream}. */
  private static String readInputStream(InputStream inputStream) {
    StringBuilder result = new StringBuilder();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    try {
      while ((line = bufferedReader.readLine()) != null) {
        result.append(line);
        result.append('\n');
      }
      bufferedReader.close();
      return result.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to read input stream.", e);
    }
  }

  /**
   * Reads the OpenAI Api key from {@code configPropertiesFilepath}. API keys can be managed at
   * https://platform.openai.com/account/api-keys.
   */
  private static final String readApiKey(String configPropertiesFilepath) {
    Properties properties = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream(configPropertiesFilepath)) {
      properties.load(fileInputStream);
      return properties.getProperty(OPENAI_API_KEY_PROPERTY);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Failed to read API key from file: " + configPropertiesFilepath);
    }
  }
}
