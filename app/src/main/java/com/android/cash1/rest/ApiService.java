package com.android.cash1.rest;

import com.android.cash1.model.Cash1LocationsNumber;
import com.android.cash1.model.CollectionsPhoneNumbers;
import com.android.cash1.model.CustomerSupportPhoneNumbers;
import com.android.cash1.model.DialogContents;
import com.android.cash1.model.FaqItem;
import com.android.cash1.model.OfficeDetails;
import com.android.cash1.model.Preferences;
import com.android.cash1.model.Office;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface ApiService {

    @GET("/CheckDeviceReg")
    void checkDeviceRegistration(@Query("Device_UID") String devId, Callback<JsonObject> callback);

    @GET("/CheckUserReg")
    void checkUserReg(@Query("Device_UID") String devId, Callback<JsonObject> callback);

    @GET("/RegisterDeviceandLink")
    void registerDevice(@Query("Device_UID") String devId, @Query("Device_Name") String devName,
                        @Query("Device_Type") String devType, @Query("Device_Manufacturer_Name") String manufacturer,
                        @Query("Device_Model_Name") String modName, @Query("Device_Model_Number") String modNumber,
                        @Query("Device_System_Name") String sysName, @Query("Device_System_Version") String sysVersion,
                        @Query("Device_Software_Version") String softwareVersion, @Query("Device_Platform_Version") String platformVersion,
                        @Query("Device_Firmware_Version") String firmwareVersion, @Query("Device_OS") String os,
                        @Query("Device_Timezone") String timezone, @Query("Language_Used_On_Device") String language,
                        @Query("Has_Camera") String hasCamera, @Query("Is_Backlight_On") String isBacklightOn,
                        @Query("Is_Battery_Removable") String modelName, @Query("UserId") String userId,
                        @Query("UserName") String userName, @Query("CustomerId") String customerId,
                        Callback<JsonObject> callback);

    @GET("/GetMessageToDisplay")
    void getDialogContents(@Query("Configuration_Type_Id") int id,
                           @Query("Configuration_Message_Type") String msgType,
                           Callback<DialogContents> callback);

    @GET("/CheckCustomerforRegistration")
    void checkEmailReg(@Query("Email") String email, Callback<JsonObject> callback);

    @GET("/SendCustomerTempPassword")
    void sendTempPass(@Query("Email") String email, Callback<JsonObject> callback);

    @GET("/CheckCustomerTempPassword")
    void checkTempPass(@Query("Email") String email,
                       @Query("CustomerID") int userId,
                       @Query("Password") String password,
                       Callback<JsonObject> callback);

    @GET("/GetValidateCustomerSPLogin")
    void login(@Query("Username") String username,
               @Query("PassWord") String password,
               Callback<JsonObject> callback);

    @POST("/ChangePassword")
    void changePassword(@Query("CustomerId") int userId,
                        @Query("Password") String password,
                        Callback<JsonObject> callback);

    @GET("/GetAllSecurityQuestion")
    void listSecurityQuestions(Callback<List<HashMap<String, String>>> callback);

    @GET("/SaveCustSecurityQandA")
    void saveSecurityQuestion(@Query("questionid") String questionId,
                              @Query("answer") String asnwer,
                              @Query("CustomerId") int userId,
                              Callback<JsonObject> callback);

    @GET("/CheckUserDeviceLink")
    void checkUserDevice(@Query("Device_UID") String deviceId,
                         @Query("Username") String username,
                         @Query("CustomerID") int userId,
                         Callback<JsonObject> callback);

    @GET("/SaveUserDeviceLink")
    void saveUserDevice(@Query("Device_UID") String deviceId,
                        @Query("Username") String username,
                        @Query("CustomerID") int userId,
                        Callback<JsonObject> callback);

    @POST("/GetCustSecurityQuestion")
    void getSecurityQuestion(@Query("UserName") String username,
                             Callback<JsonObject> callback);

    @POST("/ValidateCustSecurityQuestion")
    void validateSecurityAnswer(@Query("CustomerId") int userId,
                                @Query("Answer") String answer,
                                @Query("IsEmailUserName") boolean isEmailUsername,
                                @Query("ISEmailPassword") boolean isEmailPassword,
                                @Query("IsTextMsgUserName") boolean isTextUsername,
                                @Query("ISTextMsgPassword") boolean isTextPassword,
                                Callback<JsonObject> callback);

    @GET("/GetESignAgreement")
    void getAgreementText(Callback<HashMap<String, String>> callback);

    @POST("/SaveCustESignAgreement")
    void saveAgreement(@Query("CustomerId") int userId,
                       Callback<JsonObject> callback);

    @POST("/CheckCashAdvanceRequest")
    void checkCashAdvance(@Query("Customerid") int userId, Callback<JsonObject> callback);

    @POST("/GetStoreInfoforLoan")
    void getPhoneForStore(@Query("Storeid") int storeId, Callback<JsonObject> callback);

    @POST("/AcctSummaryforheader")
    void getAccountDetails(@Query("Customerid") int userId, Callback<JsonObject> callback);

    @POST("/SaveCashAdvanceRequest")
    void sendCashRequest(@Query("Customerid") int userId,
                         @Query("LoanRequestAmt") String amountStr,
                         Callback<JsonObject> callback);

    @POST("/SaveContactNotes")
    void saveRequestNote(@Query("CustomerId") int userId,
                         @Query("Notice") String notice,
                         Callback<JsonObject> callback);

    @POST("/AcctActivitylast60days")
    void getAccountStatement(@Query("CustomerId") int userId, Callback<List<StatementRow>> callback);

    @POST("/AcctSummaryforheader")
    void getAccountHeaderDetails(@Query("CustomerId") int userId, Callback<JsonObject> callback);

    @GET("/Log")
    void log(@Query("Thread") String thread,
             @Query("Context") String context,
             @Query("Level") String level,
             @Query("Logger") String logger,
             @Query("Message") String message,
             @Query("Exception") String exception,
             @Query("Device_UID") String deviceId,
             @Query("ScreenName") String screenName,
             ResponseCallback callback);

    @POST("/GetQandA?QandA_Type_Id=1&QandA_Type=FAQ")
    void listQuestionsAndAnswers(Callback<List<FaqItem>> callback);

    @POST("/MakeaPayment")
    void submitPayment(@Query("CustomerId") int userId, @Query("BankId") int bankId,
                       @Query("StoreId") int storeId, @Query("PaymentFrom") String paymentFrom,
                       @Query("Amount") String amount, @Query("Date") String date,
                       @Query("IsAmountDue") boolean isAmountDue, @Query("AmountDueDate") String amountDueDate,
                       @Query("IsPayOffBalance") boolean isPayOffBalance, @Query("PayOffBalance") String payOffBalance,
                       @Query("IsOtherAmount") boolean isOtherAmount, @Query("OtherAmount") String otherAmount,
                       Callback<JsonObject> callback);

    @POST("/GetContactInfo?Contact_Type=CustomerSupport")
    void getCustomerSupportPhoneNumbers(Callback<CustomerSupportPhoneNumbers> callback);

    @POST("/GetContactInfo?Contact_Type=Collections")
    void getCollectionsPhoneNumbers(Callback<CollectionsPhoneNumbers> callback);

    @POST("/GetContactInfo?Contact_Type=Cash1locations")
    void getCash1LocationsPhoneNumber(Callback<Cash1LocationsNumber> callback);

    @POST("/GetSettingsInfo")
    void getPreferences(@Query("Username") String userEmail, @Query("CustomerID") int userId,
                        Callback<Preferences> callback);

    @POST("/SaveSettingsInfo")
    void setPreferences(@Query("Device_UID") String deviceId, @Query("Username") String userEmail,
                        @Query("CustomerID") int userId, @Query("Notice") String notice,
                        @Query("IsON") boolean useCurrentLocation, Callback<JsonObject> callback);

    @GET("/GetStoreInfo")
    void listAllOffices(Callback<List<Office>> callback);

    @POST("/GetStoreDetails")
    void getStoreDetails(@Query("StoreName") String storeId, Callback<OfficeDetails> callback);

    @GET("/GetStoreInfoByState")
    void listOfficesInState(@Query("State") String stateAbbreviation, Callback<List<Office>> callback);
}
