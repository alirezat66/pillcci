package greencode.ir.pillcci.retrofit;

public enum MyMethods {
    SignUp("SignUp", 0),
    SetPass("Register", 1),
    ResendSMS("ResendSMS", 2),
    Login("Login", 3),
    ForgetPassStepOne("ForgetPassStepOne", 4),
    ForgetPassStepTwo("ForgetPassStepTwo", 5),
    ForgetPassStepThree("ForgetPassStepThree", 6),
    SetProfile("GetLoanUser", 7),
    AddDrug("addDrug", 8),
    AddUsage("addUsage", 9),
    AddUsageAll("addUsageAll", 10),
    GetDrugs("getDrugs", 11),
    DeletePillUsage("DeletePillUsage", 12),
    DeletePillObject("DeletePillObject", 13),
    AddPhone("AddPhone", 14),
    RemoveAccount("removeAccount", 15),
    GetBotList("GetBotList", 16),
    AddToBot("AddToBot", 17),
    DeleteFromBot("DeleteFromBot", 18),
    UpdateToken("UpdateToken", 19),
    DeleteToken("deleteToken", 20),
    UserInfo("UserInfo", 17),
    ChangePassword("ChangePassword", 18),
    UpdateProfile("UpdateProfile", 19),
    SendMessage("SendMessage", 20),
    Upload("Upload", 21),
    GetUserStatistic("GetUserStatistics" , 22),
    CreateInvoiceOffline("CreateInvoiceOffline" , 23),
    GetMessage("GetMessage" , 23),
    GetUserConversation("getUserConversation" , 24),
    ResetPassword("ResetPassword" , 25)
    ,GetPaymentReport("PaymetReport" , 26)
    ,CreateLottery("CreateLottory" , 27)
    ,PrepareLottery("PrepareLottory" , 28)
    ,GetLotteryGeneral("GetLotteryGeneral" , 29)
    ,PrepareWithdrawal("PrepareWithdrawal" , 30)
    ,setTransactionResult("setTransactionResult" , 31)
    ,CreateWithdrawal("CreateWithdrawal" , 32)
    ,GetWithdrawalDetails("GetWithdrawalDetails" , 32)
    ,ResetPasswordSetNewPassword("ResetPasswordSetNewPassword" , 33)
    ,ResetPasswordActiveCode("ResetPasswordActiveCode" , 34)
    ,GetAlaets("GetAlarts" , 35)
    ,GetAlaetsDetail("GetAlartsDetail" , 36)
    ,GetConversation("GetConversation" , 37)
    ,GetAnswerResult("GetAnswerResult" , 38)
    ,DeleteMessage("DeleteMessage" , 39)
    ,GetterWiner("GetterWinner" , 40)
    ,UnSeen("unseen" , 41)
    ;
    

    private String methodName;
    private int methodValue;

    public int getMethodValue() {
        return this.methodValue;
    }

    private MyMethods(String toString, int value) {
        this.methodName = toString;
        this.methodValue = value;
    }

    public String toString() {
        return this.methodName;
    }
}
