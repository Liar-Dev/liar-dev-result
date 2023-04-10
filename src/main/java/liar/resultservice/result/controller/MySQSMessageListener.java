package liar.resultservice.result.controller;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import liar.resultservice.result.controller.dto.request.SaveResultRequest;
import liar.resultservice.result.controller.util.RequestMapperFactory;
import liar.resultservice.result.service.ResultFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class MySQSMessageListener {

    private final AmazonSQS amazonSQS;
    private final ObjectMapper objectMapper;
    private final ResultFacadeService resultFacadeService;

    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @PostConstruct
    public void startListening() {
        new Thread(() -> {

            while (true) {
                List<Message> messages = amazonSQS.receiveMessage(new ReceiveMessageRequest(queueUrl)
                                .withWaitTimeSeconds(60)
                                .withMaxNumberOfMessages(10)
                                .withVisibilityTimeout(60))
                        .getMessages();

                messages.stream()
                        .forEach(message -> {
                            try {
                                SaveResultRequest request = objectMapper.readValue(message.getBody(), SaveResultRequest.class);
                                resultFacadeService.saveAllResultOfGame(RequestMapperFactory.mapper(request));

                                String receiptHandle = message.getReceiptHandle();
                                amazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, receiptHandle));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

            }
        }).start();
    }
}
