package com.koinx.test;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import io.restassured.response.Response;

/**
 * ApiTestCases
 * @author Aarti Gorde
 *
 */
public class ApiTestCases {

	/**
	 * This test case called /transaction api to get response message
	 * @param context
	 * @throws Exception
	 */
	@Test(priority = 1)
	public void verifyTransactionAPI(ITestContext context) throws Exception {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("coin1", "INR");
		data.put("coin2", "USDT");
		data.put("coin1Amount", "300");
		data.put("coin2Amount", "2");

		Response response = given().contentType("application/json").urlEncodingEnabled(false).body(data).when()
				.post("https://x8ki-letl-twmt.n7.xano.io/api:gHPd8le5/transaction");

		JSONObject responseObject = new JSONObject(response.asString());
		int i = responseObject.getInt("id");
		System.out.println("post request response = " + responseObject);
		context.setAttribute("userid", i);
		Assert.assertEquals(response.statusCode(), 200);
	}

	/**
	 * This test case calls /transaction api for requested id 
	 * @param context
	 * @throws Exception
	 */
	@Test(priority = 2, dependsOnMethods = "verifyTransactionAPI")
	public void verifyTractionAPIForId(ITestContext context) throws Exception {
		int j = (Integer) context.getAttribute("userid");

		Response response = given().contentType("application/json").urlEncodingEnabled(false).pathParam("id", +j).when()
				.get("https://x8ki-letl-twmt.n7.xano.io/api:gHPd8le5/transaction/{id}");

		JSONObject responseObject = new JSONObject(response.asPrettyString());
		System.out.println("get request response = " + responseObject);
		System.out
				.println("Response :: receivedCoinMarketPrice: " + responseObject.getDouble("receivedCoinMarketPrice"));
		System.out.println("Calcualted : receivedCoinMarketPrice: "
				+ responseObject.getDouble("sentCoinAmount") / responseObject.getDouble("receivedCoinAmount"));

		Double expectedResult = responseObject.getDouble("receivedCoinMarketPrice");

		Double actualResult = responseObject.getDouble("sentCoinAmount")
				/ responseObject.getDouble("receivedCoinAmount");

		Assert.assertEquals(expectedResult, actualResult);
	}

}
