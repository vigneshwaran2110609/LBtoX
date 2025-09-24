package com.example.LBtoX.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.example.LBtoX.utils.*;

@Component
public class RssFeedTaskProducer {

	@Autowired
    private JmsTemplate jmsTemplate;
	
	public void processFeed(String queueName, String message) {
		
		long queueSize = QueueUtils.getQueueSize(queueName);
		
		if (queueSize < 100) {
			jmsTemplate.convertAndSend(queueName, message);
		}
    }

}
