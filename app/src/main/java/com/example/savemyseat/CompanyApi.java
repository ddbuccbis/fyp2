package com.example.savemyseat;

public class CompanyApi {

    private static final String ROOT_URL = "http://192.168.50.39/SaveMySeat2/v1/companyApi.php?apicall=";

    public static final String URL_CREATE_COMPANY = ROOT_URL + "createCompany";
    public static final String URL_READ_COMPANIES = ROOT_URL + "getCompanies";
    public static final String URL_UPDATE_COMPANY = ROOT_URL + "updateCompany";
    public static final String URL_DELETE_COMPANY = ROOT_URL + "deleteCompany&compID=";


}
