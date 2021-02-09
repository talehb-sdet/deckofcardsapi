package deckofcardsapi;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DrawCardsTest {
    private static final String BASE_URL="https://deckofcardsapi.com";
    private static final String JOCKERS_ENABLED="jokers_enabled";
    private static final String NEW_DECK="/api/deck/new";
    private static final String DECK_ID= "deck_id";
    private static final String SUCCESS="success";
    private static final String DRAW_CARD="/api/deck/{deck_id}/draw?count={count}";
    private static final String REMAINING="remaining";
    private static final int SUCCESS_STATUS_CODE=200;
    private static String deckId;


    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI=BASE_URL;
        deckId=RestAssured.get(NEW_DECK).andReturn().jsonPath().get(DECK_ID);
    }

    @Test
    public void drawFourCards(){
        Response response=RestAssured.given().log().all().get(DRAW_CARD,deckId,4).andReturn();
        checkDrawCardIfSuccess(response,48,deckId);
        Response response2=RestAssured.given().log().all().get(DRAW_CARD,deckId,50).andReturn();
        checkDrawCardIfFails(response2,deckId);

    }

    private static void checkDrawCardIfSuccess(Response response,int numberOfCards,String deckId){
        Assert.assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
        JsonPath jsonPathEV=response.jsonPath();
        Assert.assertEquals(true,jsonPathEV.get(SUCCESS));
        Assert.assertEquals(numberOfCards,jsonPathEV.get(REMAINING));
        Assert.assertEquals(deckId,jsonPathEV.get(DECK_ID));
    }

    private static void checkDrawCardIfFails(Response response, String deckId){
        Assert.assertEquals(SUCCESS_STATUS_CODE, response.getStatusCode());
        JsonPath jsonPathEV=response.jsonPath();
        Assert.assertEquals(false,jsonPathEV.get(SUCCESS));
        Assert.assertEquals(deckId,jsonPathEV.get(DECK_ID));
    }

}
