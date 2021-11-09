package com.ahwers.marvin.applications.graphicalcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ahwers.marvin.framework.application.ResourceRepository;

// TODO: Adapt most of this to be a wolfram-alpha communication class that AlgebraicExpressionProcessor just uses.
public class AlgebraicExpressionProcessor {
	
	public static void main(String[] args) {
		AlgebraicExpressionProcessor processor = AlgebraicExpressionProcessor.getInstance();
		System.out.println(processor.processExpressionIntoAlgebraicExpression("y = 3x + 1"));
	}

	private static AlgebraicExpressionProcessor instance;
	
	public static AlgebraicExpressionProcessor getInstance() {
		if (instance == null) {
			instance = new AlgebraicExpressionProcessor();
		}
		
		return instance;
	}
	
	private Client client;
	private String wolframAppId;
	
	private AlgebraicExpressionProcessor() {
		client = ClientBuilder.newClient();
		wolframAppId = loadWolframAppId();
	}
	
	public String processExpressionIntoAlgebraicExpression(String expression) {
		WebTarget target = client.target("http://api.wolframalpha.com/v2/query");
		
		Response response = target.queryParam("appid", wolframAppId)
				.queryParam("input", expression)
				.queryParam("includepodid", "Input")
				.queryParam("output", "JSON")
				.request()
				.accept("application/json")
				.get();
		
		Map wolframResponse = response.readEntity(Map.class);

		System.out.println(wolframResponse.toString());
		
		Map queryResult = (Map) wolframResponse.get("queryresult");
		List pods = (List) queryResult.get("pods");
		Map inputPod = (Map) pods.get(0);
		List subPods = (List) inputPod.get("subpods");
		Map inputSubPod = (Map) subPods.get(0);
		String algebraicExpression = (String) inputSubPod.get("plaintext");
			
		return algebraicExpression;
	}

	private String loadWolframAppId() {
		String wolframAppIdResourceFilePath = ResourceRepository.getInstance().getResourcePath("wolfram_app_id.txt");
		
		String wolframAppId = null;
		try {
			Scanner txtReader = new Scanner(new File(wolframAppIdResourceFilePath));
			wolframAppId = txtReader.nextLine();
			txtReader.close();
		} catch (FileNotFoundException e) {
			throw new Error("The resource file wolfram_app_id does not exist."); // This should not be throwing this, the resource path repo should.
		}
		
		return wolframAppId;
	}
	
}
