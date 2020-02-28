import java.util.LinkedList;

public class Entity{
    private LinkedList<String> attributesName;
    private LinkedList<String> attributesType;
    private String name;
    private int RF;
    private int numberOfAttributes;

    /**
     * Contructor pentru entitate
     * Fiecare entitate va avea 2 liste:
     *      O lista va contine numele atributelor
     *      Cealalta lista va contine tipul atributului
     *
     * @param name
     * @param RF
     * @param numberOfAttributes
     * @param attributesName
     * @param attributesType
     */
    public Entity(String name, int RF, int numberOfAttributes, LinkedList<String> attributesName, LinkedList<String> attributesType) {
        this.name = name;
        this.RF = RF;
        this.numberOfAttributes = numberOfAttributes;
        this.attributesName = attributesName;
        this.attributesType = attributesType;
    }

    public String getName() {
        return name;
    }

    public int getRF() {
        return RF;
    }

    public String getAttributeType(int index) {
        return attributesType.get(index);
    }

    public String getAttributeName(int index) {
        return attributesName.get(index);
    }

    public LinkedList<String> getAttributeNameList() {
        return this.attributesName;
    }
}