# Java ChatGPT API

A Maven Java library for querying OpenAI's [Chat completions (ChatGPT) API](https://platform.openai.com/docs/guides/gpt/chat-completions-api). See OpenAI's [API Reference](https://platform.openai.com/docs/api-reference/chat) for additional details on the API.

## Setup

### OpenAI

To use this library, we need to first create an [OpenAI](https://openai.com/) account. We'll then need to create an API key to access OpenAI's APIs. We can create one for our account on the [API Keys page](https://platform.openai.com/account/api-keys).

For extensive API usage, we'll need to setup [billing](https://platform.openai.com/account/billing/overview) for our OpenAI account.

### Configuration

For this library to successfully connect to OpenAI's API, we'll need to create a `.properties` file to store our OpenAI [API Key](https://platform.openai.com/account/api-keys). The library will read the API key from the `openai.apikey` property to access the API.

```properties
# config.properties

openai.apikey=<API_KEY>
```

## Usage

See [ChatGptApiTest](javatests/ydtak/chatgpt/platform/ChatGptApiTest.java) for a complete usage example.

To call [Chat completions (ChatGPT) API](https://platform.openai.com/docs/guides/gpt/chat-completions-api), we'll first need to create an instance of [ChatGptApi](java/ydtak/chatgpt/platform/ChatGptApi.java) passing it our `.properties` file containing the OpenAI API key. See [Configuration](#configuration) for instructions on creating the file.

```java
ChatGptApi api = ChatGptApi.getInstance("config.properties");
```

Construct a [ChatGptRequest](proto/ydtak/chatgpt/platform/chatgpt_request.proto) and send it using a [ChatGptApi](./java/ydtak/chatgpt/platform/ChatGptApi.java) object.

```java
ChatGptRequest request =
    ChatGptRequest.newBuilder()
        .addMessages(
            ChatGptRequest.Message.newBuilder()
                .setRole("user")
                .setContent("What are some common halloween candy?"))
        .setModel("gpt-4")
        .setTemperature(0.7f)
        .build();
String response = chatGptApi.sendChatGptRequest(request);
```

This will return a response from OpenAI similar to the example response below.

```json
{
  "id": "<ID>",
  "object": "chat.completion",
  "created": <TIMESTAMP>,
  "model": "gpt-4-0613",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "1. Snickers\n2. Reese's Peanut Butter Cups\n3. Kit Kat\n4. M&Ms\n5. Twix\n6. Butterfingers\n7. Milky Way\n8. Hershey's Chocolate Bars\n9. Candy Corn\n10. Skittles\n11. Starburst\n12. Tootsie Rolls\n13. Sour Patch Kids\n14. Almond Joy\n15. Jolly Ranchers\n16. Nerds\n17. Smarties\n18. Twizzlers\n19. 3 Musketeers\n20. Baby Ruth\n21. Junior Mints\n22. Life Savers\n23. Haribo Gummy Bears\n24. Swedish Fish\n25. Airheads."
      },
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 15,
    "completion_tokens": 149,
    "total_tokens": 164
  }
}
```
