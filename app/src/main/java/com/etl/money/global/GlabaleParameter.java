package com.etl.money.global;

/**
 * Created by Administrator on 31/08/2015.
 */
public class GlabaleParameter {
    public static  String keygen = "123456789012345678901234";
    public static final String PREFS_MSISDN = "msisdn";
    public static final String PREFS_MY_PREF_AFTER_LOGIN = "MyPrefAfertLogin";
    public static final String PREFS_MY_PREF_PROFILE_STORE = "MyPrefAfertLogin";
    public static final String PREFS_SUBCODID = "subcodid";
    public static final String PREFS_FORCE_LOGOUT = "forcelogout";
    public static final String PREFS_SESSION = "loginsession";
    //  public static final String IpServerAddress = "http://202.62.111.100:8118";
    public static final String PREFS_NUMBER_OF_FILE = "file_num";
    public static final String PREFS_JWT_TOKEN = "etljwttoken";
    public static final String PREFS_JWT_TOKEN_RESET_PASS = "etljwttokenpass";
    public static final String PREFS_HISTORY_DETAIL = "prefs_history_detail";

    public static final String PAYMENT_TPYE = "PaymentType";

    public static final String PREFS_QR_CODE_GALLERY = "qrcodegallery";
    public static final String PREFS_CASHINREQUEST = "cashinrequest";
    // public static final String IpServerAddressImage = "http://202.62.111.100:8118/android/advertisement/";


    // public static final String PREFS_MSISDN = "msisdn";
    // public static final String PREFS_SESSION = "loginsession";
    //   public static final String IpServerAddress_portal = "http://202.62.111.100:8118/sale_portal/";

    //public static final String PREFS_USER_ID = "userid";
    public static final String SHARE_BUY_STATUS="status_sale";



    public static final String PREFS_USER_INFO_MOBILE = "mobile";
    public static final String PREFS_USER_INFO_NAME = "names";
    public static final String PREFS_USER_INFO_SURE_NAME = "sure_names";
    public static final String PREFS_USER_INFO_DOB = "date_of_birth";
    public static final String PREFS_USER_INFO_VILLAGE = "village";
    public static final String PREFS_USER_INFO_DISTRICT = "district";
    public static final String PREFS_USER_INFO_PROVINCE = "province";
    public static final String PREFS_NOTIFICATION = "notification";



    //    public static final String IpServerAddressHttps = "https://202.62.111.100/android/";
//1 min = 60000 milliseconds (1 minute to milliseconds)
    //public static final int TIME_LOGOUT_AUTO = 300000; //auto logout in 300000=5 minutes;
    public static final int TIME_LOGOUT_AUTO = 15*60000; //auto logout in 10 minutes;




    //   "result":"405000000|1|0|0|1602|0|405000000|2048|0|2048|0|0 |1602|0"} Post paid have data
//###################################################################################################################
//                                    responStr = resultCode0 + "|" + subType1 + "|" + ppstBalance2 + "|" + postReponseCode3 + "|" + posDebit4
//                                            + "|" + posAvd5 + "|" + dataQueryResponeCode6 + "|" + totalFreeResourceData7 + "|" + usageData8
//                                            + "|" + remainsData9 + "|" + score10 + "|" + usingNow11 + "|" + totalDebtOrAdvPostpaid12 + "|"
//                                            + totalRemainDataForPrepaid13 + "|" + strCustomerName14 + "|" + expireDateForPrepaid15 + "|"
//                                            + expireDatePackageData16 + "|" + suspendDate17 + "|" + disableDate18 + "|" + osStats19 + "|" + offerName20
//                                            + "|" + dataTotal21 + "|" + datausage22 + "|" + dataremain23 +"|" + dataDetail24;
//###################################################################################################################


    public static final String PREFS_PAYMENT_TPYE = "paymentType";
    public static final String PREFS_USER_INFO_SUBTPYE = "subscriberType";
    public static final String PREFS_USER_INFO_CUSTOMER_NAME= "customerName";
    public static final String PREFS_USER_INFO_PPREAPAID_EXPIRE_DATE= "prepaidExpireDateMainBalance";
    public static final String PREFS_USER_INFO_PPREAPAID_SUSPEND_DATE= "prepaidsuspendDateMainBalance";
    public static final String PREFS_USER_INFO_PPREAPAID_DISABLE_DATE= "prepaidDisableDateMainBalance";
    public static final String PREFS_USER_INFO_PPREAPAID_NUMBER_STATE= "prepaidOSStateDateMainBalance";
    public static final String PREFS_USER_INFO_OFFER_NAME= "prepaidOfferNameMainBalance";

    public static final String PREFS_INFO_DATA_DETAIL= "dataDetailWithExpireDate";

    public static final String PREFS_ETL_DEVICEINFO= "deviceInfo";
    public static final String PREFS_ETL_LANGGAUGE= "etl_language";
    public static final String PREFS_ETL_MAIN_BALANCE= "etl_mainBalance";
    public static final String PREFS_ETL_SUB1_BALANCE= "etl_subAccountBalance";
    public static final String PREFS_ETL_VERIFY_TYPE= "etl_verifyType";
    public static final String PREFS_ETL_TOKEN_WALLET_API= "etl_wallet_token_api";
    public static final String PREFS_SMS_SETTING = "etl_wallet_SMSSetting";
    public static final String PREFS_ETL_DASHBOARD= "etl_wall_dashboard";
    public static final String PREFS_ETL_PROFILE_PHOTOS= "etl_wall_profile_photos";
    public static final String PREFS_ETL_PROFILE_ID= "etl_wall_profile_ID";
    public static final String PREFS_ETL_PROFILE_PHOTOS_STORE= "etl_wall_profile_photos_store";
    public static final String PREFS_ETL_PROFILE_ID_STORE= "etl_wall_profile_ID_store";
    public static final String PREFS_ETL_CUSTOMER_NAME= "etl_wall_customer_name";
    public static final String PREFS_ETL_CUSTOMER_LAST_NAME= "etl_wall_customer_lastName";
    public static final String PREFS_ETL_REMEMBER_WALLET_NUMBER= "etl_remenber_wallet_number";
    public static final String PREFS_ETL_HISTORY_DETAIL = "prefs_etl_history_detail";
    public static final String PREFS_ETL_PROMOTION_DETAIL = "prefs_etl_promotion_detail";
    public static final String PREFS_QR_CODE_GALLERY_NUMBER = "qrcodegallerynumber";

    public static final String PREFS_NOTIFICATION_TITLE = "notificationtitle";
    public static final String PREFS_NOTIFICATION_DESCRIPTION = "notificationdescription";
    public static final String PREFS_NOTIFICATION_DATETIME = "notificationdatetime";

    public static final String PREFS_ETL_IS_END_USER_ENABLE= "etl_wall_is_end_user_enable";
    public static final String PREFS_ETL_IS_AGENT_ENABLE= "etl_wall_is_agent_enable";
    public static final String PREFS_ETL_IS_MERCHANT_ENABLE= "etl_wall_is_merchant_enable";
    public static final String PREFS_ETL_END_USER_MENU_LIST= "etl_wall_end_user_menu_list";
    public static final String PREFS_ETL_AGENT_MENU_LIST= "etl_wall_agent_menu_list";
    public static final String PREFS_ETL_MERCHANT_MENU_LIST= "etl_wall_merchant_menu_list";



    public static final String PREFS_ETL_END_USER_MENU_LIST_SHOWMORE= "etl_wall_end_user_menu_list_showmore";
    public static final String PREFS_ETL_AGENT_MENU_LIST_SHOWMORE= "etl_wall_agent_menu_list_showmore";
    public static final String PREFS_ETL_MERCHANT_MENU_LIST_SHOWMORE= "etl_wall_merchant_menu_list_showmore";



    public static final String PREFS_ETL_END_USER_MENU_LIST_ฺCOUNT= "etl_wall_end_user_menu_list_basic";
    public static final String PREFS_ETL_AGENT_MENU_LIST_COUNT= "etl_wall_agent_menu_list_first_basic";
    public static final String PREFS_ETL_MERCHANT_MENU_LIST_COUNT= "etl_wall_merchant_menu_list_basic";
    public static final String PREFS_ETL_AUT_LOGOOT= "etl_wall_auto_logout";
    public static final String PREFS_RESUM_LAST_DATE= "etl_last_resum_last_date";
    public static final String PREFS_RESUM_CHECK_BALANCE_AFTER_CHARGE= "etl_check_balance_after_charge";

    public static final String PREFS_etlPreFix = "(202|302|202|203|202).*";






}
