package org.example.tests.integration;
import io.restassured.path.json.JsonPath;
import jdk.jfr.Description;
import junit.runner.BaseTestRunner;
import org.example.base.baseTest;
import org.example.endpoints.APIConstants;
import org.example.payloads.Booking;
import org.example.payloads.Bookingresponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.requestSpecification;
import static org.assertj.core.api.Assertions.assertThat;
public class TCintegration2  extends baseTest {

    // Create A Booking, Create a Token
    // Get booking
    // Update the Booking
    // Delete the Booking
    //

    // How to pass the data to one testcase to another.

//    @Test
//    public void createToken(){
//
//    }


    @Test( priority = 1)
    @Description("TC#INT1 - Step 1. Verify that the Booking can be Created")
    public void testCreatingBooking(ITestContext iTestContext) {
        // Post
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL);
        response = RestAssured.given().spec(requestSpecification)
                .when().body(payloadManager.createPayloadGSON()).post();
        validatableResponse = response.then().log().all();
        Bookingresponse bookingRespons = payloadManager.bookingResponseJava(response.asString());
        assertThat(bookingRespons.getBookingid()).isNotNull();
        iTestContext.setAttribute("bookingid", bookingRespons.getBookingid());
        iTestContext.setAttribute("token", getToken());
    }


    @Test( priority = 2)
    @Description("TC#INT1 - Step 2. Verify that the Booking By ID")
    public void testVerifyBookingId(ITestContext iTestContext) {
        // GET Req
        System.out.println(iTestContext.getAttribute("bookingid"));
        Assert.assertTrue(true);
    }

    @Test( priority = 3)

    @Description("TC#INT1 - Step 3. Verify Updated Booking by ID")
    public void testUpdateBookingByID(ITestContext iTestContext) {
        // PUT/ PATCH
        Integer bookingId = (Integer) iTestContext.getAttribute("bookingid");
        String token = (String) iTestContext.getAttribute("token");
        System.out.println("Booking Id Generated -> "+ bookingId);
        System.out.println("Token Generated -> "+token);

        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingId);
        response = RestAssured.given().spec(requestSpecification).cookie("token",token)
                .when().body(payloadManager.updatePayload()).put();

        validatableResponse = response.then().log().all();
        Booking booking = payloadManager.bookingResponsePUTReqJava(response.asString());
        assertThat(booking.getFirstname()).isEqualTo("Amit");

    }

    @Test( priority = 4)
    @Description("TC#INT1 - Step 4. Delete the Booking by ID")
    public void testDeleteBookingById(ITestContext iTestContext) {
        Integer bookingId = (Integer) iTestContext.getAttribute("bookingid");
        String token = (String) iTestContext.getAttribute("token");
        // DELETE Req.
        System.out.println(iTestContext.getAttribute("bookingid"));
        requestSpecification.basePath(APIConstants.CREATE_UPDATE_BOOKING_URL + "/" + bookingId).cookie("");
        validatableResponse = RestAssured.given().spec(requestSpecification)
                .when().delete().then().log().all();
        validatableResponse.statusCode(201);
    }

}
