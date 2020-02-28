import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Instance implements Comparable<Instance>, Cloneable{
    private LinkedList<Attribute> attributes;
    private String entityName;
    private long timestamp;
    private Attribute key;

    /**
     * Constructor pentru instanta
     *
     * Fiecare instanta are o lista de atribute care va fi populata pe baza listei de
     * atribute data ca parametru
     *
     * Se va face convertirea la atributul specific pe baza tipurilor de atribute din entitatea model
     *
     * La fiecare creare a instantei aceasta va primi timestamp-ul curent la care se face initializarea acesteia
     *
     * @param entityName
     * @param attributesList
     */
    public Instance(String entityName, LinkedList<String> attributesList) {
        this.entityName = entityName;
        this.timestamp = System.nanoTime();
        this.attributes = new LinkedList<Attribute>();

        Entity entity = DB.getEntity(entityName);

        //Se creeaza fiecare atribut in functie de tipul acestuia si se adauga in lista de atribute
        for (int i = 0; i < attributesList.size(); i++) {
            String attrValue = attributesList.get(i);
            String attrType = entity.getAttributeType(i);
            Attribute attribute = new Attribute(attrValue, attrType);
            attributes.add(attribute);
        }

        //Cheia specifica acestei instante va fi chiar primul atribut din lista
        key = attributes.get(0);
    }

    public String getEntityName() {
        return entityName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        this.timestamp = System.nanoTime();
    }

    public Attribute getKey() {
        return key;
    }

    /**
     * Aceasta metoda afiseaza informatiile corespunzatoare instantei in fisierul
     * de output
     *
     * Daca scrierea esueaza se va arunca exceptia IOException
     *
     * @param output
     * @throws IOException
     */
    public void display(BufferedWriter output) throws IOException {
        String display = this.getEntityName();

        for(int i = 0; i < attributes.size(); i++){
            Attribute attribute = attributes.get(i);
            Entity entity = DB.getEntity(this.getEntityName());
            String attrName = entity.getAttributeName(i);
            display = display + " " + attrName + ":" + attribute.getValue();
        }

        output.write(display + "\n");
    }

    /**
     * Aceasta metoda va actualiza lista de atribute a instantei curente
     * De asemenea la fiecare actualizare a instantei se va reinnoi timestamp-ul
     * Dupa actualizarea atributelor cheia se va actualiza devenind primul atribut din lista
     *
     * @param newAttributesName
     * @param newAttributesValue
     */
    public void update(LinkedList<String> newAttributesName, LinkedList<String> newAttributesValue) {
        this.setTimestamp();
        Entity entity = DB.getEntity(this.getEntityName());
        LinkedList<String> attributesName = entity.getAttributeNameList();

        int newIndex = 0;
        for(int i = 0; i < attributes.size() && newIndex != newAttributesName.size(); i++){
            Attribute attribute = attributes.get(i);
            String attrName = attributesName.get(i);
            String newName = newAttributesName.get(newIndex);
            String newValue = newAttributesValue.get(newIndex);
            if(attrName.equals(newName)){
                attribute.update(newValue);
                newIndex++;
            }
        }

        //Actualizare cheie
        this.key = attributes.get(0);
    }

    /**
     * Metoda de comparatie cu alta instanta
     * Aceasta comparatie se realizeaza pe baza timestamp-ului celor 2 instante
     *
     * @param o2
     * @return
     */
    @Override
    public int compareTo(Instance o2) {
        if(this.getTimestamp() > o2.getTimestamp()){
            return 1;
        }else if(this.getTimestamp() < o2.getTimestamp()){
            return -1;
        }else{
            return 0;
        }
    }

    /**
     * Aceasta metoda de clonare este suprascrisa pentru a putea face un deep clone
     * Se va clona instanta impreuna cu lista de instante + fiecare atribut din lista
     *
     * Daca clonarea va esua atunci se va afisa in consola mesajul de eroare de mai jos
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Instance clone(){
        Instance clone = null;
        try{
            clone = (Instance)super.clone();
            clone.setTimestamp();
            clone.attributes = (LinkedList<Attribute>) attributes.clone();
        }catch(CloneNotSupportedException e){
            System.out.println("Instance not clonned");
        }

        return clone;
    }
}
