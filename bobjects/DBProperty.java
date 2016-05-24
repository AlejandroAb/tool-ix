/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobjects;

/**
 *
 * @author Alejandro
 */
public class DBProperty {

    private String key;
    private String value;
    private boolean numeric = false;

    public String getKey() {
        return key;
    }

    public String toMongoString() {
        String mongoStr = "";
        if (numeric) {
            mongoStr = "\"" + key + "\":"  + value;
        } else {
            mongoStr = "\"" + key + "\":" + "\"" + value + "\"";
        }
        return mongoStr;

    }

    public DBProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public DBProperty(String key, String value, boolean numeric) {
        this.key = key;
        this.value = value;
        this.numeric = numeric;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean isNumeric) {
        this.numeric = isNumeric;
    }

}
