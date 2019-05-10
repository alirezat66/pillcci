package greencode.ir.pillcci.retrofit;

public enum serverErrors {
    NullError("خطای دریافت اطلاعات.", 0),
    NotJsaonResponse("خطای نوع اطلاعات ورودی", 1),
    ServerError("اشکال در دسترسی به شبکه. لطفا دوباره تلاش کن!", 2),
    Authorized("401", 3);

    private String errorName;
    private int errorValue;

    public String getErrorName() {
        return this.errorName;
    }

    public int getErrorValue() {
        return this.errorValue;
    }

    private serverErrors(String toString, int value) {
        this.errorName = toString;
        this.errorValue = value;
    }
}
