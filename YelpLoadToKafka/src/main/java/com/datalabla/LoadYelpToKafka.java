package com.datalabla;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.File;

import java.util.Properties;

import org.apache.commons.io.IOUtils;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class LoadYelpToKafka {
	String fileDir = "/home/cloudera/yelp/business.json";
	
	public void run() {
		try {
			Properties props = new Properties();
//			props.put("metadata.broker.list", "sandbox.hortonworks.com:6667");
//			props.put("zk.connect", "localhost:2181");
			props.put("metadata.broker.list", "localhost:9092");
			props.put("zk.connect", "localhost:2181,localhost:2181,localhost:2181");
			props.put("serializer.class", "kafka.serializer.StringEncoder");
			props.put("request.required.acks", "1");

			//setup Producer Config using this prop
			String TOPIC = "yelp_reviews2";
			ProducerConfig config = new ProducerConfig(props);

			
			
			//setup Producer using this producer config
			Producer<String, String> producer = new Producer<String, String>(config);
			
			InputStream in = new FileInputStream(new File(fileDir));
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	System.out.println(line);
	        	KeyedMessage<String, String> data = new KeyedMessage<String, String>(TOPIC, line);
				producer.send(data);
	        }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		new LoadYelpToKafka().run();
	}
}
