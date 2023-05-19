package com.org.codeverse.producer;

import com.org.codeverse.dto.SubmissionQueueDTO;
import com.org.codeverse.model.Submission;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubmissionProducer {
    @Value("${rabbitmq.queue.name}")
    private String QUEUE_NAME;
    @Value("${rabbitmq.queue.exchange}")
    private String EXCHANGE_NAME;
    @Value("${rabbitmq.queue.routing_key}")
    private String ROUTING_KEY;
    private RabbitTemplate rabbitTemplate;
    public SubmissionProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendJsonMessage(Submission submission, Long questionId) {
        System.out.println("Producer submission " + submission.getId());
        SubmissionQueueDTO submissionQueueDTO = new SubmissionQueueDTO();
        submissionQueueDTO.setSubmission(submission);
        submissionQueueDTO.setQuestionId(questionId);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, submissionQueueDTO);
    }
}
