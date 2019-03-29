package restapi;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AppleStoreDownAPI {

	private static final String NO_NAME = "No name";
	private static final String BOOLEAN_Y = "Y";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_STATUS = "status";

	@Test
	@Parameters("url")
	public void GetStoreDetails(String url) {
		// Specify the base URL to the RESTful web service
		RestAssured.baseURI = url;

		// To avoid ssl certificate error
		RestAssured.useRelaxedHTTPSValidation();

		RequestSpecification httpRequest = RestAssured.given();

		// calling request method and returning the response.
		Response response = httpRequest.request(Method.GET);

		if (response != null && response.getStatusCode() == HttpStatus.SC_OK) {
			
			// Converting to String object
			String responseBody = response.getBody().asString();

			// Creating JsonObject
			JSONObject document = new JSONObject(responseBody);

			// iterated over the keys (country code)
			document.keySet().forEach(countryCode -> {

				JSONObject countryProperties = (JSONObject) document.get(countryCode);

				// failing the test if the country status is is "Y"
				if (countryProperties.has(ATTR_STATUS)
						&& countryProperties.getString(ATTR_STATUS).equalsIgnoreCase(BOOLEAN_Y)) {

					// checking if country name present then print name
					String countryName = countryProperties.has(ATTR_NAME) ? countryProperties.getString(ATTR_NAME)
							: NO_NAME;

					System.out.println(countryName);
					Assert.fail();
				}

			});

		} else {
			Assert.fail("Response code is not 200.");
		}
	}

}
