package deckofcardsapi;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class NewDeckofCardsTest {
    private static final String BASE_URL="https://deckofcardsapi.com";
    private static final String JOCKERS_ENABLED="jokers_enabled";
    private static final String NEW_DECK="/api/deck/new";
    private static final String DECK_ID= "deck_id";
    private static final String SUCCESS="success";
    private static final String SHUFFLED="shuffled";
    private static final String REMAINING="remaining";
    private static final int SUCCESS_STATUS_CODE=200;


    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI=BASE_URL;
    }

    @Test
    public void createNewDeck(){
        Response response=RestAssured.given().log().all().queryParam(JOCKERS_ENABLED,"false").request(Method.GET,NEW_DECK);
        checkNewDeck(response,52);


        Response response2=RestAssured.given().log().all().request(Method.GET,NEW_DECK);
        checkNewDeck(response2,52);



    }

    @Test
    public void addNewDeckJokers(){
        Response response=RestAssured.given().log().all().queryParam(JOCKERS_ENABLED,"true").request(Method.GET,NEW_DECK);
        checkNewDeck(response,54);

    }


    private static void checkNewDeck(Response response,int numberOfCards){
        Assert.assertEquals(SUCCESS_STATUS_CODE,response.getStatusCode());
        JsonPath jsonPathEV=response.jsonPath();
        Assert.assertEquals(true,jsonPathEV.get(SUCCESS));
        Assert.assertEquals(numberOfCards,jsonPathEV.get(REMAINING));
        Assert.assertEquals(false,jsonPathEV.get(SHUFFLED));
        Assert.assertNotNull(jsonPathEV.get(DECK_ID));
    }


}
