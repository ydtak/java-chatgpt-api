package ydtak.chatgpt.platform;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ChatGptApiTest {

  private ChatGptApi chatGptApi;

  @Before
  public void setUp() {
    chatGptApi = ChatGptApi.getInstance("config.properties");
  }

  @Test
  public void test() {
    ChatGptRequest request =
        ChatGptRequest.newBuilder()
            .addMessages(
                ChatGptRequest.Message.newBuilder()
                    .setRole("user")
                    .setContent("What are some common halloween candy?"))
            .setModel("gpt-4")
            .setTemperature(0.7f)
            .build();

    ChatGptResponse result = chatGptApi.sendChatGptRequest(request);

    System.out.println(result);
    assertThat(result.getObject()).contains("chat.completion");
  }
}
