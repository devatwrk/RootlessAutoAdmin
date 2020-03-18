package aara.tech.rootless_auto_admin.repository.model;

public class CountryCodeData {

    private String countries_isd_code;
    private String country_id;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCountries_isd_code() {
        return countries_isd_code;
    }

    public void setCountries_isd_code(String countries_isd_code) {
        this.countries_isd_code = countries_isd_code;
    }
}
