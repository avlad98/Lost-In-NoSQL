import java.text.DecimalFormat;

public class Attribute implements Cloneable{
    private String attributeType = null;
    private String attribute = null;
    private String stringValue = null;
    private int intValue = 0;
    private float floatValue = 0f;

    /**
     * Constructorul pentru atribut
     *
     * Se vor initializa campurile corespunzatoare
     * pe baza tipului de atribut dorit
     *
     * @param attribute
     * @param attributeType
     */
    public Attribute(String attribute, String attributeType) {
        this.attributeType = attributeType;
        this.attribute = attribute;

        switch(this.attributeType){
            case "String":
                this.stringValue = attribute;
                break;
            case "Float":
                floatValue = Float.parseFloat(attribute);
                break;
            case "Integer":
                intValue = Integer.parseInt(attribute);
                break;
        }
    }

    /**
     *Aceasta metoda de clonare este suprascrisa pentru a face deep clone
     *
     * In cazul in care clonarea va esua se va afisa in consola mesajul de mai jos
     *
     * @return
     */
    public Attribute clone(){
        Attribute clone = null;
        try{
            clone = (Attribute)super.clone();
        }catch (CloneNotSupportedException e){
            System.out.println("Attribute not clonned");
        }
        return clone;
    }

    /**
     * Aceasta metoda returneaza valoarea atributului sub forma de String
     * In functie de tipul de atribut se va face convertirea mentionata
     *
     * @return
     */
    public String getValue() {
        switch (this.getAttributeType()){
            case "String":
                return this.getStringValue();
            case "Float":
                DecimalFormat df = new DecimalFormat("#.##");
                String outString = df.format((double) this.getFloatValue());
                outString.replace(",", ".");
                return outString;
            case "Integer":
                return Integer.toString(this.getIntValue());
        }
        return null;
    }

    /**
     * Aceasta metoda actualizeaza valoarea atributului
     *
     * @param newValue
     */
    public void update(String newValue) {
        this.setAttribute(newValue);
        switch (this.getAttributeType()){
            case "String":
                this.setStringValue(newValue);
                break;
            case "Float":
                this.setFloatValue(Float.parseFloat(newValue));
                break;
            case "Integer":
                this.setIntValue(Integer.parseInt(newValue));
                break;
        }
    }

    public String getAttributeType() {
        return attributeType;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

}